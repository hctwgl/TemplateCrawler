package com.seveniu.dataprocess.impl.filedownload;

/**
 * Created by seveniu on 8/17/17.
 * *
 */
public class Result {
    private String url; // required
    private String storageName; // required
    private String originName; // required
    private String extension; // required

    public Result() {

    }

    public Result(String url, String storageName, String originName, String extension) {
        this.url = url;
        this.storageName = storageName;
        this.originName = originName;
        this.extension = extension;
    }

    public String getUrl() {
        return this.url;
    }


    public String getStorageName() {
        return this.storageName;
    }

    public String getOriginName() {
        return this.originName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setStorageName(String storageName) {
        this.storageName = storageName;
    }

    public void setOriginName(String originName) {
        this.originName = originName;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
}
