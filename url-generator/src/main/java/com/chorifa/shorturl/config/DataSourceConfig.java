package com.chorifa.shorturl.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.shardingsphere.api.config.sharding.ShardingRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.TableRuleConfiguration;
import org.apache.shardingsphere.api.config.sharding.strategy.StandardShardingStrategyConfiguration;
import org.apache.shardingsphere.shardingjdbc.api.ShardingDataSourceFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class DataSourceConfig {

	static final int TableNumInEachDB = 3;

	static final int DBNum = 2;

	private final String logicTableName = "origin_url_2_short_url";

	private final String actualDataNodes = "url_mapping_${1..2}.url_origin2short_${0..2}";

	private final String key = "short_url";

	@Value("${mybatis.mapperLocations}")
	private String mapperLocations;

	@Value("${mysql.datasource1.name}")
	private String dbName1;
	@Value("${mysql.datasource1.url}")
	private String dbUrl1;
	@Value("${mysql.datasource2.name}")
	private String dbName2;
	@Value("${mysql.datasource2.url}")
	private String dbUrl2;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver-class-name}")
	private String drive;

	@Bean(name = "shardingDataSource")
	public DataSource createShardingDataSource() throws SQLException{
		ShardingRuleConfiguration config = new ShardingRuleConfiguration();
		config.getTableRuleConfigs().add(createUrlTableRuleConfiguration());
		//config.getBindingTableGroups().add();
		config.setDefaultDatabaseShardingStrategyConfig(new StandardShardingStrategyConfiguration(key, new DefaultDatabaseShardingAlgorithm()));
		config.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration(key, new DefaultTableShardingAlgorithm()));
		return ShardingDataSourceFactory.createDataSource(createDataSourceMap(),config, null);
	}

	private TableRuleConfiguration createUrlTableRuleConfiguration(){
		//if have multi logic-table can set sharding-strategy for each
		//config.setTableShardingStrategyConfig();
		//config.setTableShardingStrategyConfig();
		//config.setKeyGeneratorColumnName();
		return new TableRuleConfiguration(logicTableName, actualDataNodes);
	}

	private Map<String, DataSource> createDataSourceMap(){
		Map<String, DataSource> map = new HashMap<>();
		map.put(dbName1, createDataSource(dbUrl1));
		map.put(dbName2, createDataSource(dbUrl2));
		return map;
	}

	private DataSource createDataSource(final String url){
		HikariConfig config = new HikariConfig();
		config.setDriverClassName(drive);
		config.setJdbcUrl(url);
		config.setUsername(username);
		config.setPassword(password);
		return new HikariDataSource(config);
	}

	@Bean
	public DataSourceTransactionManager transactionManager(@Autowired DataSource shardingDataSource){
		return new DataSourceTransactionManager(shardingDataSource);
	}

	@Bean
	@Primary
	public SqlSessionFactory sqlSessionFactory(@Autowired DataSource shardingDataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(shardingDataSource);
		bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(mapperLocations));
		return bean.getObject();
	}

	@Bean(name = "sqlSessionTemplate")
	@Primary
	public SqlSessionTemplate sqlSessionTemplate(@Autowired SqlSessionFactory sqlSessionFactory){
		return new SqlSessionTemplate(sqlSessionFactory);
	}

}
