package com.example.demo.repository.master;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.master.entity.MasterTenant;
import java.util.List;



@Repository
public interface MasterTenantRepository extends JpaRepository<MasterTenant, Long> {
    Optional<MasterTenant> findByTenantId(String tenantId);
}

