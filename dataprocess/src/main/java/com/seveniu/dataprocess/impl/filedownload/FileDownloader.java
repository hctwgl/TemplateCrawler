package com.seveniu.dataprocess.impl.filedownload;

import com.seveniu.common.concurrent.BoundedExecutor;
import com.seveniu.common.concurrent.CountDownExecutor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by seveniu on 1/15/18.
 * *
 */
@Service
public class FileDownloader {
    @Autowired
    FileStorage fileStorage;

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private static final RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(10 * 1000).build();
    private int threadNum = 5;
    private CloseableHttpClient httpClient;
    private static AtomicInteger downloadCounter = new AtomicInteger();
    private ExecutorService executorService;

    public FileDownloader() {
        this.executorService = BoundedExecutor.getBoundedExecutor(0, threadNum, threadNum * 2);
    }

//    public Result download(String url) {
//        try {
//            TimeUnit.SECONDS.sleep(1);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    public Result download(String url) {
        CloseableHttpResponse response;
        HttpGet httpGet = null;

        InputStream inputStream = null;
        try {
            // 以get方法执行请求
            if (!url.startsWith("http")) {
                return null;
            }
            httpGet = new HttpGet(url);
            httpGet.setConfig(requestConfig);
            httpGet.addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
            httpGet.addHeader("referer", getReferer(url));

            // 获得服务器响应的所有信息
            response = getHttpClient().execute(httpGet);

            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            HttpEntity entity = response.getEntity();
            inputStream = entity.getContent();
            byte[] bytes = IOUtils.toByteArray(inputStream);
            downloadCounter.incrementAndGet();
            if (downloadCounter.get() % 100 == 0) {
                logger.info("download count : {}", downloadCounter.get());
            }
            String md5 = md5(bytes);
            Result result = getFileNameAndExtention(url, response, md5);
            fileStorage.save(bytes, result.getStorageName());
            return result;
        } catch (Exception e) {
            logger.warn("file download error url: {} , error : {}", url, e);
            throw new RuntimeException(e);
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (httpGet != null) {
                httpGet.releaseConnection();
            }
        }
    }

    public Map<String, Result> download(Collection<String> urls) throws InterruptedException {
        if (urls != null && urls.size() > 0) {

            Map<String, Result> resultMap = new HashMap<>(urls.size());
            List<Runnable> runnables = urls.stream().map(url -> (Runnable) () -> resultMap.put(url, download(url))).collect(Collectors.toList());
            CountDownExecutor<Runnable> countDownExecutor = new CountDownExecutor<>(executorService, runnables);
            countDownExecutor.execute(urls.size() * 10, TimeUnit.SECONDS);
            return resultMap;
        }
        return Collections.emptyMap();
    }

    private final Object lock = new Object();

    public CloseableHttpClient getHttpClient() {

        if (httpClient != null) {
            return this.httpClient;
        }
        synchronized (lock) {
            if (httpClient != null) {
                return this.httpClient;
            }
            PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(5, TimeUnit.MINUTES);
            cm.setMaxTotal(threadNum);
            cm.setDefaultMaxPerRoute(40);
            RequestConfig requestConfig = RequestConfig.custom()
                    .setConnectTimeout(4 * 1000) // 建立连接的时间
                    .setConnectionRequestTimeout(10 * 1000) // 等待数据的时间，在建立连接之后
                    .setSocketTimeout(10 * 1000).build(); // 从连接池中取连接等待的时间
            this.httpClient =
                    HttpClients.custom()
                            .setDefaultRequestConfig(requestConfig)
                            .setConnectionManager(cm).build();
            return this.httpClient;
        }
    }

    private static String getReferer(String url) {
        try {
            URL url1 = new URL(url);
            return url1.getProtocol() + "://" + url1.getHost();
        } catch (MalformedURLException e) {
            return url;
        }
    }

    private Result getFileNameAndExtention(String url, CloseableHttpResponse response, String md5) {
        Header cd = response.getLastHeader("Content-Disposition");
        String fileName = null;
        if (cd != null) {
            fileName = cd.getValue().replaceFirst("(?i)^.*filename=\"?([^\"]+)\"?.*$", "$1");
        }
        Header contentTypeHeader = response.getLastHeader("Content-Type");
        String contentType = null;
        if (contentTypeHeader != null) {
            contentType = getFileType(contentTypeHeader.getValue());
        }
        if (fileName == null) {
            fileName = getName(url);
        }
        if (contentType == null) {
            contentType = getExtension(url);
        }

        Result result = new Result();
        result.setUrl(url);
        result.setOriginName(fileName);
        result.setExtension(contentType);
        result.setStorageName(md5 + "." + contentType);
        return result;
    }


    private String getFileType(String fileType) {
        if (fileType != null && fileType.length() > 0) {
            return fileType.split("/")[1];
        }

        return null;
    }

    private String getName(String url) {
        String temp = url.substring(url.lastIndexOf("/") + 1).trim();
        int index = temp.lastIndexOf(".");
        if (index < 0) {
            return "";
        }
        return temp.substring(0, index);
    }

    private String getExtension(String url) {
        String temp = url.substring(url.lastIndexOf(".") + 1).trim();
        Pattern pattern = Pattern.compile("(\\w+)");
        Matcher m = pattern.matcher(temp);
        if (m.find()) {
            return m.group(1);
        }
        return "";
    }

    private String md5(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest messagedigest = MessageDigest.getInstance("MD5");
        if (messagedigest == null) {
            return null;
        }
        return new String(Hex.encodeHex(messagedigest.digest(bytes)));
    }
}
