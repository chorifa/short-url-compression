package com.chorifa.shorturl.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;

import java.util.Collection;

public class DefaultTableShardingAlgorithm implements PreciseShardingAlgorithm<String> {
    @Override
    public String doSharding(Collection<String> collection, PreciseShardingValue<String> preciseShardingValue) {
        for(String tableName : collection)
            if(tableName.endsWith(preciseShardingValue.getValue().hashCode()%DataSourceConfig.TableNumInEachDB+""))
                return tableName;
        throw new IllegalArgumentException("Cannot find suitable table.");
    }
}
