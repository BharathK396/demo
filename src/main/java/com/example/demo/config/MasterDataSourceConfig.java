package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

/*
 * import javax.sql.DataSource;
 * 
 * import org.hibernate.jpa.boot.spi.EntityManagerFactoryBuilder; import
 * org.springframework.beans.factory.annotation.Qualifier; import
 * org.springframework.boot.context.properties.ConfigurationProperties; import
 * org.springframework.boot.jdbc.DataSourceBuilder; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.context.annotation.Primary; import
 * org.springframework.data.jpa.repository.config.EnableJpaRepositories; import
 * org.springframework.orm.jpa.JpaTransactionManager; import
 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; import
 * org.springframework.transaction.PlatformTransactionManager;
 * 
 * import jakarta.persistence.EntityManagerFactory;
 * 
 * @Configuration
 * 
 * @EnableJpaRepositories( basePackages = "com.example.demo.repository.master",
 * entityManagerFactoryRef = "masterEntityManagerFactory", transactionManagerRef
 * = "masterTransactionManager" ) public class MasterDataSourceConfig {
 * 
 * @Bean
 * 
 * @Primary
 * 
 * @ConfigurationProperties("spring.datasource.master") public DataSource
 * masterDataSource() { return DataSourceBuilder.create().build(); }
 * 
 * @Bean
 * 
 * @Primary public LocalContainerEntityManagerFactoryBean
 * masterEntityManagerFactory( EntityManagerFactoryBuilder builder) { return
 * builder .dataSource(masterDataSource()) // not withDataSource()
 * .packages("com.example.demo.master.entity") .persistenceUnit("master")
 * .build(); }
 * 
 * @Bean
 * 
 * @Primary public PlatformTransactionManager masterTransactionManager(
 * 
 * @Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) { return
 * new JpaTransactionManager(emf); } }
 */


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder; // ✅ correct import
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

@Configuration
@EnableJpaRepositories(
    basePackages = "com.example.demo.repository.master",
    entityManagerFactoryRef = "masterEntityManagerFactory",
    transactionManagerRef = "masterTransactionManager"
)
public class MasterDataSourceConfig {

    @Primary
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.master")
    public DataSource masterDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Primary
    @Bean
    public LocalContainerEntityManagerFactoryBean masterEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
    	Map<String, Object> properties = new HashMap<>();
    	properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return builder
                .dataSource(masterDataSource())
                .packages("com.example.demo.master.entity")
                .persistenceUnit("master")
                .properties(properties)
                .build();
    }

    @Primary
    @Bean
    public PlatformTransactionManager masterTransactionManager(
            @Qualifier("masterEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
    
}

