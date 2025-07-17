package com.example.demo.dto;

import lombok.Data;

@Data
public class TenantRequest {
    private String tenantId;
    private String schemaName;
    private String username;	
    private String password;
}
