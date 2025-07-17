package com.example.demo.dto;

import lombok.Data;

@Data
public class UserRequest {
    private String tenantId;
    private String username;
    private String email;
}
