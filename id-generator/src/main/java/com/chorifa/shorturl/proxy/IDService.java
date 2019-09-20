package com.chorifa.shorturl.proxy;

import com.chorifa.shorturl.dao.IDDataObjectMapper;
import com.chorifa.shorturl.dataobject.IDDataObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
class IDService {

    private static final String tag = "short_url";

    @Autowired
    private IDDataObjectMapper mapper;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    String nextId(){
        IDDataObject id = mapper.selectByTag(tag); // has write lock. it's ok. also can try cas
        if(id == null) throw new RuntimeException("cannot find id for tag: "+ tag);
        String res = String.valueOf(id.getCurVal()).concat("_")+ id.getStep();
        id.setCurVal(id.getCurVal() + id.getStep());
        int cnt = mapper.updateByPrimaryKey(id);
        if(cnt != 1) throw new RuntimeException("update error: "+ res);
        return res;
    }

}
