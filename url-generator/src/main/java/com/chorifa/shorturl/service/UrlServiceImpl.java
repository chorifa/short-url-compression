package com.chorifa.shorturl.service;

import com.chorifa.minirpc.invoker.DefaultRPCInvokerFactory;
import com.chorifa.minirpc.invoker.reference.RPCReferenceManager;
import com.chorifa.minirpc.invoker.reference.ReferenceManagerBuilder;
import com.chorifa.shorturl.codec.UrlCodeC;
import com.chorifa.shorturl.dao.URLDataObjectMapper;
import com.chorifa.shorturl.dataobject.URLDataObject;
import com.chorifa.shorturl.proxy.DBProxy;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("UrlService")
public class UrlServiceImpl implements UrlService, DisposableBean, InitializingBean {

    private final DBProxy proxy;
    private final DefaultRPCInvokerFactory factory;
    private final URLDataObjectMapper mapper;

    @Autowired
    public UrlServiceImpl(URLDataObjectMapper mapper) {
        RPCReferenceManager manager = ReferenceManagerBuilder.init().forService(DBProxy.class).forAddress("localhost:8086").build();
        factory = manager.getInvokerFactory();
        proxy = manager.get();
        this.mapper = mapper;
    }

    @Override
    public void destroy() throws Exception {
        if(factory != null)
            factory.stop();
    }

    @Override
    public String lookup(String key) {
        URLDataObject urlDO = mapper.selectByShortUrl(key);
        if(urlDO == null) return ERROR;
        return urlDO.getOriginUrl();
    }

    @Override
    public String compressAndSave(String origin) {
        long id = proxy.nextId();
        String shortUrl = UrlCodeC.encode(id);
        int cnt = mapper.insertSelective(new URLDataObject(null,shortUrl,origin));
        if(cnt != 1) return ERROR;
        else return shortUrl;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(factory != null)
            factory.start();
    }
}
