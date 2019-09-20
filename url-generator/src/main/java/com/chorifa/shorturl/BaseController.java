package com.chorifa.shorturl;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import com.chorifa.shorturl.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("base")
public class BaseController{

    @Autowired
    private UrlService service;

    @GetMapping(value = "/{shortUrl}")
    public void route(@PathVariable String shortUrl, HttpServletResponse response) throws IOException{
        String origin = service.lookup(shortUrl);
        if(origin.equals(UrlService.ERROR))
            response.sendRedirect("/invalid");
        else response.sendRedirect(origin);
    }

    @GetMapping(value = "/testGetUrl2short")
    public String getd(@RequestParam(value = "value") Integer integer){
        return "current url type is get -> request parameter is "+ integer;
    }

    @GetMapping(value = "/testGetUrl2short/{key}")
    public String get(@PathVariable String key){
        return "current url type is get -> path variable is "+key;
    }

    @GetMapping(value = "/testUrl2short")
    public String get(){
        return "current url do not need path variable";
    }

    @GetMapping(value = "/hello")
    public String hello(){
        return "Hello";
    }

    @GetMapping(value = "/invalid")
    public String blankPage(){
        return "Invalid url -> redirect to blank page";
    }

}
