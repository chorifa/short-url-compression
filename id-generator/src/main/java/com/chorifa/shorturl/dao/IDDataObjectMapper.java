package com.chorifa.shorturl.dao;

import com.chorifa.shorturl.dataobject.IDDataObject;

public interface IDDataObjectMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IDDataObject record);

    int insertSelective(IDDataObject record);

    IDDataObject selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IDDataObject record);

    int updateByPrimaryKey(IDDataObject record);

    IDDataObject selectByTag(String tag); // has lock
}