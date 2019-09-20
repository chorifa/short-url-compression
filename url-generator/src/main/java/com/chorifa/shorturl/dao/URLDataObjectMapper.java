package com.chorifa.shorturl.dao;

import com.chorifa.shorturl.dataobject.URLDataObject;

public interface URLDataObjectMapper {
    int deleteByPrimaryKey(Long id);

    int insert(URLDataObject record);

    int insertSelective(URLDataObject record);

    URLDataObject selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(URLDataObject record);

    int updateByPrimaryKey(URLDataObject record);

    URLDataObject selectByShortUrl(String shortUrl);
}