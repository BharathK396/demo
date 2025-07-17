package com.example.demo.services;

import com.example.demo.dto.ClientDTO;
import com.example.demo.tenant.entity.ClientEntity;

public class ClientMapper {

    public static ClientDTO toDto(ClientEntity entity) {
        return new ClientDTO(
            entity.getClientId(),
            entity.getClientName(),
            entity.getClientContact(),
            entity.getClientDetails()
        );
    }

    public static ClientEntity toEntity(ClientDTO dto) {
        return new ClientEntity(
            dto.getClientId(),
            dto.getClientName(),
            dto.getClientContact(),
            dto.getClientDetails()
        );
    }
}

