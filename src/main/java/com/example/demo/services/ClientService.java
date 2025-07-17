package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ClientDTO;
import com.example.demo.repository.tenant.ClientRepository;
import com.example.demo.tenant.entity.ClientEntity;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    @Autowired
    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public ClientDTO saveClient(ClientDTO clientDTO) {
        // Convert DTO to Entity
        ClientEntity entity = new ClientEntity(
            clientDTO.getClientId(),
            clientDTO.getClientName(),
            clientDTO.getClientContact(),
            clientDTO.getClientDetails()
        );

        // Save Entity
        ClientEntity savedEntity = clientRepository.save(entity);

        // Convert back to DTO and return
        return new ClientDTO(
            savedEntity.getClientId(),
            savedEntity.getClientName(),
            savedEntity.getClientContact(),
            savedEntity.getClientDetails()
        );
    }

	public List<ClientDTO> getClientDetails() {
		// TODO Auto-generated method stub
		return null;
	}
}

