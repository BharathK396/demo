package com.example.demo.tenant.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tenants")
public class Tenant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "tenant_id")
    private String tenantId;
    @Column(name = "schema_name")
    private String schemaName;
    @Column(name = "db_username")
    private String dbUsername;
    @Column(name = "db_password")
    private String dbPassword;
}

