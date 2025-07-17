package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.TenantContext;
import com.example.demo.dto.ClientDTO;
import com.example.demo.dto.TenantRequest;
import com.example.demo.dto.UserRequest;
import com.example.demo.services.TenantService;

@RestController
@RequestMapping("/api/tenants")
public class TenantController {

    @Autowired
    private TenantService tenantService;

    @PostMapping("/create")
    public ResponseEntity<String> createTenant(@RequestBody TenantRequest req) {
        try {
            tenantService.createTenant(req.getTenantId(), req.getSchemaName(), req.getUsername(), req.getPassword());
            return ResponseEntity.ok("Tenant created");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/addUser")
    public ResponseEntity<String> addUserToTenant(@RequestBody UserRequest req) {
        tenantService.saveUserInTenant(req.getTenantId(), req.getUsername(), req.getEmail());
        return ResponseEntity.ok("User added");
    }
    
    @PostMapping("/addClient")
    public ResponseEntity<String> addClientToTenant(@RequestBody ClientDTO req, @RequestHeader("X-Tenant-ID") String tenantId) {
    	//TenantContext.setCurrentTenant(tenantId);
    	tenantService.saveClientInTenant(req.getClientName(), req.getClientDetails(), req.getClientContact(), tenantId);
        
        return ResponseEntity.ok("User added");
    }
}
