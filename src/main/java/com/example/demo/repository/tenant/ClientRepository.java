package com.example.demo.repository.tenant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.tenant.entity.ClientEntity;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {
    
    ClientEntity findByClientName(String clientName);

    ClientEntity findByClientContact(String clientContact);
}

