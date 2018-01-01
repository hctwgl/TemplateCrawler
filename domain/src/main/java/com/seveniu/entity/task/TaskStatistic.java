package com.seveniu.entity.task;


import com.seveniu.entity.base.BaseAuditableEntity;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Transient;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by seveniu on 5/15/16.
 * TaskStatistic
 */
@Entity
public class TaskStatistic extends BaseAuditableEntity {
    private static final int NETWORK_ERROR_URL_LIST_SIZE_MAX = 50;
    private static final int PARSE_ERROR_URL_LIST_SIZE_MAX = 50;
    private String taskId;
    private Date startTime;
    private Date endTime;
    private int startUrlCount = 0;
    private int createUrlCount;
    private int createTargetUrlCount;
    private int createNextUrlCount;
    private int successUrlCount;
    private int repeatUrlCount;
    private int netErrorUrlCount;
    private int doneUrlCount;
    private int parseErrorCount;
    private int createNodeCount;
    private int doneNodeCount;
    private int errorNodeCount;
    @Transient
    private List<String> downloadErrorUrlList;
    @Transient
    private List<ParseError> parseErrorList;
    @Lob
    private String downloadErrorUrls;
    @Lob
    private String parseErrors;

    // startUrlCount + createUrlCount = repeatUrlCount + successUrlCount + netErrorUrlCount
    // startUrlCount + createUrlCount = repeatUrlCount + createTargetUrlCount + createNextUrlCount
    // successUrlCount = doneUrlCount + parseErrorCount
    // createNodeCount = doneNodeCount + errorNodeCount

    public TaskStatistic(String taskId) {
        this.taskId = taskId;
    }


    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public int getStartUrlCount() {
        return startUrlCount;
    }

    public void setStartUrlCount(int startUrlCount) {
        this.startUrlCount = startUrlCount;
    }

    public int getCreateUrlCount() {
        return createUrlCount;
    }

    public void setCreateUrlCount(int createUrlCount) {
        this.createUrlCount = createUrlCount;
    }

    public int getCreateTargetUrlCount() {
        return createTargetUrlCount;
    }

    public void setCreateTargetUrlCount(int createTargetUrlCount) {
        this.createTargetUrlCount = createTargetUrlCount;
    }

    public int getCreateNextUrlCount() {
        return createNextUrlCount;
    }

    public void setCreateNextUrlCount(int createNextUrlCount) {
        this.createNextUrlCount = createNextUrlCount;
    }

    public int getSuccessUrlCount() {
        return successUrlCount;
    }

    public void setSuccessUrlCount(int successUrlCount) {
        this.successUrlCount = successUrlCount;
    }

    public int getRepeatUrlCount() {
        return repeatUrlCount;
    }

    public void setRepeatUrlCount(int repeatUrlCount) {
        this.repeatUrlCount = repeatUrlCount;
    }

    public int getNetErrorUrlCount() {
        return netErrorUrlCount;
    }

    public void setNetErrorUrlCount(int netErrorUrlCount) {
        this.netErrorUrlCount = netErrorUrlCount;
    }

    public int getDoneUrlCount() {
        return doneUrlCount;
    }

    public void setDoneUrlCount(int doneUrlCount) {
        this.doneUrlCount = doneUrlCount;
    }

    public int getParseErrorCount() {
        return parseErrorCount;
    }

    public void setParseErrorCount(int parseErrorCount) {
        this.parseErrorCount = parseErrorCount;
    }

    public int getCreateNodeCount() {
        return createNodeCount;
    }

    public void setCreateNodeCount(int createNodeCount) {
        this.createNodeCount = createNodeCount;
    }

    public int getDoneNodeCount() {
        return doneNodeCount;
    }

    public void setDoneNodeCount(int doneNodeCount) {
        this.doneNodeCount = doneNodeCount;
    }

    public int getErrorNodeCount() {
        return errorNodeCount;
    }

    public void setErrorNodeCount(int errorNodeCount) {
        this.errorNodeCount = errorNodeCount;
    }

    public List<String> getDownloadErrorUrlList() {
        return downloadErrorUrlList;
    }

    public void setDownloadErrorUrlList(List<String> downloadErrorUrlList) {
        this.downloadErrorUrlList = downloadErrorUrlList;
    }

    public synchronized void addParseErrorUrlList(ParseError parseError) {
        if (parseErrorList == null) {
            parseErrorList = new LinkedList<>();
        }
        if (parseErrorList.size() >= 100) {
            return;
        }
        parseErrorList.add(parseError);
    }

    public List<ParseError> getParseErrorList() {
        return parseErrorList;
    }

    public void setParseErrorList(List<ParseError> parseErrorList) {
        this.parseErrorList = parseErrorList;
    }

    public String getDownloadErrorUrls() {
        return downloadErrorUrls;
    }

    public synchronized void addDownloadErrorUrls(String url) {
        if (downloadErrorUrlList == null) {
            downloadErrorUrlList = new LinkedList<>();
        }
        if (downloadErrorUrlList.size() >= 100) {
            return;
        }
        downloadErrorUrlList.add(url);
    }

    public void setDownloadErrorUrls(String downloadErrorUrls) {
        this.downloadErrorUrls = downloadErrorUrls;
    }

    public String getParseErrors() {
        return parseErrors;
    }

    public void setParseErrors(String parseErrors) {
        this.parseErrors = parseErrors;
    }

    public static class ParseError {
        private String url;
        private Exception e;

        public ParseError() {
        }

        public ParseError(String url, Exception e) {
            this.url = url;
            this.e = e;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Exception getE() {
            return e;
        }

        public void setE(Exception e) {
            this.e = e;
        }
    }

}
