package com.chorifa.shorturl.dataobject;

public class URLDataObject {
    private Long id;

    private String shortUrl;

    private String originUrl;

    public URLDataObject() {
    }

    public URLDataObject(Long id, String shortUrl, String originUrl) {
        this.id = id;
        this.shortUrl = shortUrl;
        this.originUrl = originUrl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getShortUrl() {
        return shortUrl;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public String getOriginUrl() {
        return originUrl;
    }

    public void setOriginUrl(String originUrl) {
        this.originUrl = originUrl;
    }
}