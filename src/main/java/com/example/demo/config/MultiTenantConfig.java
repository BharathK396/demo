package com.example.demo.config;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.hibernate.engine.jdbc.connections.internal.MultiTenantConnectionProviderInitiator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;

/*
 * package com.example.demo.config;
 * 
 * import java.util.HashMap; import java.util.Map; import
 * org.hibernate.cfg.Environment;
 * 
 * import org.springframework.beans.factory.annotation.Autowired; import
 * org.springframework.context.annotation.Bean; import
 * org.springframework.context.annotation.Configuration; import
 * org.springframework.orm.jpa.JpaTransactionManager; import
 * org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean; import
 * org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter; import
 * org.springframework.transaction.PlatformTransactionManager; import
 * org.springframework.transaction.annotation.EnableTransactionManagement;
 * 
 * import com.example.demo.services.CurrentTenantIdentifierResolverImpl; import
 * com.example.demo.services.DataSourceBasedMultiTenantConnectionProvider;
 * 
 * import jakarta.persistence.EntityManagerFactory;
 * 
 * @Configuration
 * 
 * @EnableTransactionManagement public class MultiTenantConfig {
 * 
 * @Autowired private DataSourceBasedMultiTenantConnectionProvider
 * multiTenantConnectionProvider;
 * 
 * @Autowired private CurrentTenantIdentifierResolverImpl
 * tenantIdentifierResolver;
 * 
 * @Bean public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
 * Map<String, Object> hibernateProps = new HashMap<>();
 * hibernateProps.put("hibernate.multiTenancy", "DATABASE");
 * hibernateProps.put("hibernate.multi_tenant_connection_provider",
 * multiTenantConnectionProvider);
 * hibernateProps.put("hibernate.tenant_identifier_resolver",
 * tenantIdentifierResolver); hibernateProps.put("hibernate.dialect",
 * "org.hibernate.dialect.MySQL8Dialect");
 * hibernateProps.put("hibernate.show_sql", true);
 * hibernateProps.put("hibernate.hbm2ddl.auto", "update");
 * 
 * LocalContainerEntityManagerFactoryBean em = new
 * LocalContainerEntityManagerFactoryBean();
 * em.setPackagesToScan("com.example.multitenant.entity");
 * em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
 * em.setJpaPropertyMap(hibernateProps); return em; }
 * 
 * @Bean public PlatformTransactionManager transactionManager(
 * EntityManagerFactory emf) { return new JpaTransactionManager(emf); } }
 * 
 */
import org.hibernate.cfg.Environment;
@Configuration
@EnableJpaRepositories(
		basePackages = "com.example.demo.repository.tenant",
    entityManagerFactoryRef = "tenantEntityManagerFactory",
    transactionManagerRef = "tenantTransactionManager"
)
public class MultiTenantConfig {

    @Autowired
    private DataSourceBasedMultiTenantConnectionProvider multiTenantConnectionProvider;

    @Autowired
    private CurrentTenantIdentifierResolver tenantIdentifierResolver;
    
    @Autowired
    @Qualifier("masterDataSource")  // this must match the bean name
    private DataSource masterDataSource;

    @Bean
    public LocalContainerEntityManagerFactoryBean tenantEntityManagerFactory(EntityManagerFactoryBuilder builder) {
        Map<String, Object> hibernateProps = new HashMap<>();
       // hibernateProps.put(Environment.MULTI_TENANT, MultiTenancyStrategy.SCHEMA);
        hibernateProps.put("hibernate.multiTenancy", "SCHEMA");
        hibernateProps.put(Environment.MULTI_TENANT_CONNECTION_PROVIDER, multiTenantConnectionProvider);
        hibernateProps.put(Environment.MULTI_TENANT_IDENTIFIER_RESOLVER, tenantIdentifierResolver);
        hibernateProps.put(Environment.DIALECT, "org.hibernate.dialect.MySQLDialect");

        return builder
        	    .dataSource(masterDataSource)
        	    .packages("com.example.demo.tenant.entity")
        	    .persistenceUnit("master")
        	    .build();
    }

    @Bean
    public PlatformTransactionManager tenantTransactionManager(
            @Qualifier("tenantEntityManagerFactory") EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }
}

