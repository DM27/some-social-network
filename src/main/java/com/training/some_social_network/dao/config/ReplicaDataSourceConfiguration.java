package com.training.some_social_network.dao.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties
public class ReplicaDataSourceConfiguration {

    @Bean
    @Primary
    @DependsOn({"routingDatasource", "masterDataSource", "replicaDataSource"})
    @ConditionalOnProperty(name = "spring.replica.datasource.enable", havingValue = "true")
    public DataSource dataSource() {
        return new LazyConnectionDataSourceProxy(routingDatasource());
    }

    @Bean
    @ConditionalOnProperty(name = "spring.replica.datasource.enable", havingValue = "true")
    public DataSource routingDatasource() {
        Map<Object, Object> targetDataSources = new HashMap<>();
        DataSource masterDatasource = masterDataSource();
        DataSource replicaDatasource = replicaDataSource();
        targetDataSources.put(DatabaseType.MASTER, masterDatasource);
        targetDataSources.put(DatabaseType.REPLICA, replicaDatasource);

        ReplicationRoutingDataSource clientRoutingDatasource = new ReplicationRoutingDataSource();
        clientRoutingDatasource.setTargetDataSources(targetDataSources);
        clientRoutingDatasource.setDefaultTargetDataSource(masterDatasource);

        return clientRoutingDatasource;
    }

    @Bean
    public DataSource masterDataSource() {
        return new HikariDataSource(masterPoolConfig());
    }

    @Bean
    @ConditionalOnProperty(name = "spring.replica.datasource.enable", havingValue = "true")
    public DataSource replicaDataSource() {
        return new HikariDataSource(replicaPoolConfig());
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.master.datasource")
    public HikariConfig masterPoolConfig() {
        return new HikariConfig();
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.replica.datasource")
    @ConditionalOnProperty(name = "spring.replica.datasource.enable", havingValue = "true")
    public HikariConfig replicaPoolConfig() {
        return new HikariConfig();
    }
}
