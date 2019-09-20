package com.chorifa.shorturl.id;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * snowflake like id generator
 * 1bit empty, 41bit time_stamp, 8bit work_id, 14bit auto-increase number
 * not suitable for current situation
 */
public final class Snowflake{

    //private static final Logger logger = LoggerFactory.getLogger(Snowflake.class);

    private static final long OFFSET = LocalDate.of(2000,1,1).atStartOfDay(ZoneId.of("Z")).toEpochSecond();

    /**
     * 14 bit max auto-increase number
     */
    private static final long MAX_PER_ID = 0b11_1111_1111_1111L;

    /**
     * can use zookeeper's persistent sequential nodes.
     */
    private static final long WORKER_ID = 1L << 14;

    private static long offset = -1;

    private static long lastEpoch = 0;

    /**
     * if choose second as current time unit. 14bit offset tend to be exhausted more easily, while can use more years.
     * if choose micro-second as unit. 14bit offset can represent more ids, while dead years may meet in advance.
     * @return next id
     */
    public static long nextId(){
        return nextId(System.currentTimeMillis()/1000);
    }

    private static synchronized long nextId(long epoch){
        if(epoch < lastEpoch){
//            logger.warn("clock turns back: current epoch time: {}, last epoch time: {}. " +
//                    "Using last epoch instead.",epoch,lastEpoch);
            epoch = lastEpoch;
        }else if(epoch > lastEpoch){
            lastEpoch = epoch;
            offset = -1;
        }
        offset++;
        if(offset >= MAX_PER_ID){
//            logger.warn("All available ids consumed in current epoch: {}. Using ids in next epoch",epoch);
            return nextId(epoch +1);
        }
        return generateId(epoch, offset);
    }

    private static long generateId(long epoch, long offset){
        return (epoch - OFFSET) << 22 | WORKER_ID | offset;
    }

}