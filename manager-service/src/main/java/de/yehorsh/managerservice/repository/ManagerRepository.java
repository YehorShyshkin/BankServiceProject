package de.yehorsh.managerservice.repository;

import de.yehorsh.managerservice.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {
    Optional<Manager> findManagerByEmail(String email);
    boolean existsManagerByEmailOrLastNameOrPhoneNumber (String email, String lastName, String phoneNumber);
}


