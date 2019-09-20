package com.chorifa.shorturl.proxy;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component("DBProxy")
public final class DBProxyImpl implements InitializingBean, DisposableBean, DBProxy {

    @Autowired
    private IDService service;

    private final AtomicLong cursor = new AtomicLong();
    private volatile long limit;
    private volatile long nextFrom;
    private volatile long nextLimit;

    //private static final Object lock = new Object();
    private static final Lock lock = new ReentrantLock();
    private static final Condition canConsume = lock.newCondition();
    private static final Condition canProduce = lock.newCondition();
//    private int errorCnt = 0;

    public long nextId(){
        long cur;
        do {
            cur = cursor.get();
            if(cur >= limit){
                try {
                    lock.lock();
                    if(cur >= limit){
                        if(nextFrom >= cur){ // valid
                            cursor.set(nextFrom); // must first
                            limit = nextLimit;
//                            errorCnt = 0;

                            canProduce.signal(); // notify
                        }else{
//                            if(errorCnt++ > 10) {
//                                running = false;
//                                thread.interrupt();
//                                throw new RuntimeException("error get new segment.");
//                            }
                            canProduce.signal();
                            try {
                                canConsume.await(10, TimeUnit.SECONDS);
                            } catch (InterruptedException ignore) {
                            }
                        }
                    }
                }finally {
                    lock.unlock();
                }
            }else if(cursor.compareAndSet(cur,cur+1)) {
//                if(nextFrom < cur && limit - 0.9*(nextLimit-nextFrom) < cur){
//                    System.out.println("single can produce: cur_val: "+cur+" next_from: "+nextFrom);
//                    lock.lock();
//                    try {
//                        canProduce.signal(); // notify to work
//                    }finally {
//                        lock.unlock();
//                    }
//                }
                return cur;
            }
        }while (true);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // init
        String[] s = service.nextId().split("_");
        if(s.length != 2) throw new IllegalArgumentException("next() in IDService error.");
        cursor.set(Long.parseLong(s[0]));
        limit = Long.parseLong(s[0])+Long.parseLong(s[1]);
        thread.start();
    }

    private volatile boolean running = true;
    private final Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (running) {
                try {
                    String[] s = service.nextId().split("_");
                    if (s.length != 2) throw new IllegalArgumentException("next() in IDService error.");
                    lock.lock();
                    try {
                        nextLimit = Long.parseLong(s[0]) + Long.parseLong(s[1]);
                        nextFrom = Long.parseLong(s[0]);
//                        System.out.println("watcher refresh!!! -> next from: "+nextFrom+" next limit: "+nextLimit);
                        canConsume.signalAll();
                        while (cursor.get() < nextFrom) {
                            canProduce.await();
//                            System.out.println("watcher: wait at cursor: "+cursor.get()+" next from: "+nextFrom);
                        }
                    }finally {
                        lock.unlock();
                    }
                } catch (InterruptedException e) {
                    if (!running) {
                        System.out.println("watcher shut down.");
                        break;
                    }
                }
            }
        }
    });

    @Override
    public void destroy() throws Exception {
        running = false;
        thread.interrupt();
    }

}
