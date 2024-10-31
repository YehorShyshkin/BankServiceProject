package com.bankapp.app.repository;

import com.bankapp.app.dto.ManagerDTO;
import com.bankapp.app.entity.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    List<ManagerDTO> findManagerById(UUID managerId);
}
