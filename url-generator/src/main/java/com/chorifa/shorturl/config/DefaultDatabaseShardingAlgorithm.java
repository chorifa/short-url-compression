package com.chorifa.shorturl.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class DefaultDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        for(String dbname : collection)
            if(dbname.endsWith((preciseShardingValue.getValue().hashCode()%DataSourceConfig.DBNum)+1+""))
                return dbname;
        throw new IllegalArgumentException("Cannot find suitable database.");
    }
}
