package com.chorifa.shorturl;

import com.chorifa.shorturl.service.UrlService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.concurrent.TimeUnit;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.chorifa.shorturl"})
@MapperScan(basePackages = "com.chorifa.shorturl.dao", sqlSessionTemplateRef = "sqlSessionTemplate")
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
@EnableTransactionManagement(proxyTargetClass = true)
public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        ApplicationContext context = SpringApplication.run(App.class, args);
//        UrlService service = context.getBean("UrlService", UrlService.class);
//
//        String origin = "testGetUrl2short?value=789";
//        String shortUrl = service.compressAndSave(origin);
//        String originUrl = service.lookup(shortUrl);
//        System.out.println(origin.equals(originUrl));
//        service.compressAndSave("testUrl2short");
//        service.compressAndSave("testGetUrl2short?value=456");
//        service.compressAndSave("testGetUrl2short/path456");
//        for(int i = 100; i < 200; i++){
//            service.compressAndSave("testGetUrl2short?value="+i);
//            service.compressAndSave("testGetUrl2short/"+i);
//        }
        try {
            TimeUnit.MINUTES.sleep(2);
        }catch (InterruptedException ignore){
        }
        System.exit(SpringApplication.exit(context, ()->0));
    }
}
