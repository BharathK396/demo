package com.example.demo.services;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.example.demo.config.DataSourceBasedMultiTenantConnectionProvider;
import com.example.demo.config.TenantContext;
import com.example.demo.master.entity.MasterTenant;
import com.example.demo.repository.master.MasterTenantRepository;
import com.example.demo.repository.tenant.ClientRepository;
import com.example.demo.repository.tenant.TenantRepository;
import com.example.demo.tenant.entity.ClientEntity;
import com.example.demo.tenant.entity.Tenant;

import jakarta.transaction.Transactional;

@Service
public class TenantService {
    @Autowired
    @Qualifier("masterDataSource")
    private DataSource dataSource;
    @Autowired private MasterTenantRepository masterRepo;
    @Autowired private TenantRepository tenantRepo;
    @Autowired private ClientRepository clientRepo;
    @Autowired private DataSourceBasedMultiTenantConnectionProvider multiTenantConnectionProvider;

    public void createTenant(String tenantId, String schemaName, String username, String password) throws SQLException {
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
        	// Save tenant in master
    		
			MasterTenant tenant = new MasterTenant();
			tenant.setTenantId(tenantId);
			tenant.setSchemaName(schemaName);
			tenant.setDbUsername(username);
			tenant.setDbPassword(password);
			masterRepo.save(tenant);
    		 
        	
            stmt.executeUpdate("CREATE SCHEMA " + schemaName);
            stmt.executeUpdate("USE " + schemaName);
            

            // Copy table structure (you could use INFORMATION_SCHEMA or manual SQL copy)
          //  stmt.executeUpdate("CREATE TABLE users LIKE master_db.users");
         //   stmt.executeUpdate("CREATE TABLE `" + schemaName + "`.client_entity LIKE master_db.client_entity");
            String createTableSql = """
                    CREATE TABLE IF NOT EXISTS client_entity (
                        client_id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        client_name VARCHAR(100),
                        client_contact VARCHAR(100),
                        client_details TEXT
                    );
                """;
            stmt.executeUpdate(createTableSql);
            System.out.println("Table 'client_entity' created in schema: " + schemaName);
            String createTableSqltenants = """
                    CREATE TABLE IF NOT EXISTS tenants (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        tenant_id VARCHAR(255) NOT NULL,
                        schema_name VARCHAR(255) NOT NULL,
                        db_username VARCHAR(255) NOT NULL,
                        db_password VARCHAR(255) NOT NULL
                    );
                """;
            stmt.executeUpdate(createTableSqltenants);
            
			/*
			 * String sql =
			 * "INSERT INTO client_entity (client_name, client_contact, client_details) VALUES (?, ?, ?)"
			 * ; PreparedStatement ps = conn.prepareStatement(sql);
			 * 
			 * ps.setString(1, schemaName); ps.setString(2, username); ps.setString(3,
			 * password+"_root");
			 * 
			 * ps.executeUpdate();
			 * 
			 * System.out.println("Client inserted successfully.");
			 */
        }
        Tenant tenant = new Tenant();
		tenant.setTenantId(tenantId);
		tenant.setSchemaName(schemaName);
		tenant.setDbUsername(username);
		tenant.setDbPassword(password);
		tenantRepo.save(tenant);
		
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientDetails(password);
        clientEntity.setClientContact(schemaName);
        clientEntity.setClientName(username);
        clientRepo.save(clientEntity);
        // Save tenant in master
		/*
		 * MasterTenant tenant = new MasterTenant(); tenant.setTenantId(tenantId);
		 * tenant.setSchemaName(schemaName); tenant.setDbUsername(username);
		 * tenant.setDbPassword(password); masterRepo.save(tenant);
		 */
    }

    public void saveUserInTenant(String tenantId, String username, String email) {
        MasterTenant tenant = masterRepo.findByTenantId(tenantId).orElseThrow();

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/" + tenant.getSchemaName(),
                tenant.getDbUsername(), tenant.getDbPassword())) {

            String sql = "INSERT INTO users (username, email) VALUES (?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, email);
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public void createTenantDetails(String tenantId, String schemaName, String username, String password) throws SQLException {
        // Validate schemaName (alphanumeric + _ only)
        if (!schemaName.matches("^[a-zA-Z0-9_]+$")) {
            throw new IllegalArgumentException("Invalid schema name: " + schemaName);
        }

        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            // Create the tenant schema safely
            stmt.executeUpdate("CREATE SCHEMA `" + schemaName + "`");

            // Copy the structure of "users" table from master_db
            stmt.executeUpdate("CREATE TABLE `" + schemaName + "`.client_entity LIKE master_db.client_entity");
        }

        // Save tenant metadata to master DB
        MasterTenant tenant = new MasterTenant();
        tenant.setTenantId(tenantId);
        tenant.setSchemaName(schemaName);
        tenant.setDbUsername(username);
        tenant.setDbPassword(password);
        masterRepo.save(tenant);
    }
    
	public void insertClient(String tenantDbUrl, String dbUsername, String dbPassword, String clientName,
			String clientContact, String clientDetails) {
		String sql = "INSERT INTO client_entity (client_name, client_contact, client_details) VALUES (?, ?, ?)";

		try (Connection conn = DriverManager.getConnection(tenantDbUrl, dbUsername, dbPassword);
				PreparedStatement ps = conn.prepareStatement(sql)) {

			ps.setString(1, clientName);
			ps.setString(2, clientContact);
			ps.setString(3, clientDetails);

			ps.executeUpdate();

			System.out.println("Client inserted successfully.");

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void saveClientInTenant(String clientName, String clientDetails, String clientContact, String tenantId) {
	//	TenantContext.setCurrentTenant(tenantId);
		multiTenantConnectionProvider.selectDataSource(tenantId);
		TenantContext.setCurrentTenant(tenantId);
		ClientEntity clientEntity = new ClientEntity();
        clientEntity.setClientDetails(clientDetails);
        clientEntity.setClientContact(clientContact);
        clientEntity.setClientName(clientName);
        clientRepo.save(clientEntity);
	}

}

