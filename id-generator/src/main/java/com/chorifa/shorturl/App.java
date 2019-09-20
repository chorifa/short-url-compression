package com.chorifa.shorturl;

import com.chorifa.minirpc.provider.DefaultRPCProviderFactory;
import com.chorifa.shorturl.proxy.DBProxy;
import com.chorifa.shorturl.proxy.DBProxyImpl;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import java.util.concurrent.TimeUnit;

//import java.util.Arrays;
//import java.util.Collections;
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;

/**
 * Hello world!
 *
 */
@SpringBootApplication(scanBasePackages = {"com.chorifa.shorturl"})
@MapperScan("com.chorifa.shorturl.dao")
public class App
{
    public static void main( String[] args )
    {
        ApplicationContext context = SpringApplication.run(App.class, args);
        DBProxy proxy = context.getBean("DBProxy", DBProxyImpl.class);

        DefaultRPCProviderFactory factory = new DefaultRPCProviderFactory().init().addService(DBProxy.class.getName(),null, proxy);
        factory.start();
        try {
            TimeUnit.MINUTES.sleep(1);
        }catch (InterruptedException ignore){
        }finally {
            factory.stop();
        }
        System.exit(SpringApplication.exit(context, () -> 0));
        /*
        Set<Long> set = Collections.newSetFromMap(new ConcurrentHashMap<>(1024));
        Thread[] threads = new Thread[20];
        for(int i = 0; i < threads.length; i++){
            threads[i] = new Thread(()->{
                for(int j = 0; j < 100; j++){
                    long id = proxy.nextId();
                    if(set.contains(id)) System.err.println("has repeated id: "+id);
                    else set.add(id);
                    //LockSupport.parkNanos(1000*1000*10); // 10ms
                }
            });
        }
        for(Thread thread : threads)
            thread.start();
        try {
            for(Thread thread : threads)
                thread.join();
        } catch (InterruptedException ignore) {
        }
        System.out.println("consumer done");
        Long[] longs = set.toArray(new Long[0]);
        Arrays.sort(longs);
        for(int i = 0; i < longs.length-1; i++){
            if(longs[i+1]-longs[i] != 1)
                System.err.println("error get id: between "+longs[i]+" "+longs[i+1]);
        }
        System.out.println("min id: " +longs[0]);
         */
    }
}
