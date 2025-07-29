package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {

	/*
	 * @Bean
	 * 
	 * @Primary
	 * 
	 * @ConfigurationProperties("spring.datasource") public DataSource
	 * masterDataSourceSecondary() { return DataSourceBuilder.create().build(); }
	 */	

    // Dynamic datasource resolution
    @Bean(name = "routingDataSource")
    public DataSourceRouting dataSource( @Qualifier("masterDataSource") DataSource masterDataSource) {
        DataSourceRouting routingDataSource = new DataSourceRouting();

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource);
        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);

        routingDataSource.afterPropertiesSet();

        return routingDataSource;
    }
    
	
	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter(),
				new HashMap<>(), null);
	}
	 

}

