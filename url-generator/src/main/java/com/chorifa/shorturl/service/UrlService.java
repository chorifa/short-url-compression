package com.chorifa.shorturl.service;

public interface UrlService {
    String ERROR = "INVALID_URL";

    String lookup(String key);

    String compressAndSave(String origin);

}
