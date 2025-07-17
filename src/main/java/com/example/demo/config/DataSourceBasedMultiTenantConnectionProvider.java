package com.example.demo.config;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.stereotype.Component;

import com.example.demo.master.entity.MasterTenant;
import com.example.demo.repository.master.MasterTenantRepository;

@Component
public class DataSourceBasedMultiTenantConnectionProvider extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
	
    @Autowired
    private MasterTenantRepository masterTenantRepository;
    
	/*
	 * @Autowired private TenantRepository tenantRepository;
	 */

    private Map<String, DataSource> dataSourceMap = new ConcurrentHashMap<>();

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSourceMap.values().stream().findFirst().orElseThrow();
    }

    public DataSource selectDataSource(String tenantId) {
        return dataSourceMap.computeIfAbsent(tenantId, id -> {
            MasterTenant tenant = masterTenantRepository.findByTenantId(id).orElseThrow();
            return DataSourceBuilder.create()
                    .url("jdbc:mysql://localhost:3306/" + tenant.getSchemaName())
                    .username(tenant.getDbUsername())
                    .password(tenant.getDbPassword())
                    .driverClassName("com.mysql.cj.jdbc.Driver")
                    .build();
        });
    }
    
	/*
	 * public DataSource selectDataSourceTenant(String tenantId) { return
	 * dataSourceMap.computeIfAbsent(tenantId, id -> { Tenant tenant =
	 * tenantRepository.findByTenantId(id).orElseThrow(); return
	 * DataSourceBuilder.create() .url("jdbc:mysql://localhost:3306/" +
	 * tenant.getSchemaName()) .username(tenant.getDbUsername())
	 * .password(tenant.getDbPassword())
	 * .driverClassName("com.mysql.cj.jdbc.Driver") .build(); }); }
	 */

	@Override
	protected DataSource selectDataSource(Object tenantIdentifier) {
		// TODO Auto-generated method stub
		return null;
	}
}

