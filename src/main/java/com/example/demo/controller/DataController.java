package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.ClientDTO;
import com.example.demo.services.ClientService;
import com.example.demo.services.CurrentTenantIdentifierResolverImpl;


@RestController
public class DataController {

	@Autowired
	ClientService clientService;
	
	@RequestMapping("/getData")
	public List<ClientDTO> getClientDetails() {
		List<ClientDTO> courtList = clientService.getClientDetails();
		return courtList;
	}
	
	@RequestMapping("/postData")
	public ClientDTO postTestDataDetails(@RequestBody ClientDTO clientDTO) {
		ClientDTO clientDataDTO = clientService.saveClient(clientDTO);
		return clientDataDTO;
	}
	
	 @PostMapping("/clients")
	    public ResponseEntity<ClientDTO> saveClient(
	        @RequestBody ClientDTO dto,
	        @RequestHeader("X-Tenant-ID") String tenantId
	    ) {
	        CurrentTenantIdentifierResolverImpl.setTenantId(tenantId);
	        try {
	            return ResponseEntity.ok(clientService.saveClient(dto));
	        } finally {
	            CurrentTenantIdentifierResolverImpl.clear();
	        }
	    }
}
