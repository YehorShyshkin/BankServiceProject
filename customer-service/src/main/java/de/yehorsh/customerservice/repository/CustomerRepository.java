package de.yehorsh.customerservice.repository;

import de.yehorsh.customerservice.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {
    Optional<Customer> findCustomerByEmail(String email);
    boolean existsByEmailOrPhoneNumberOrTaxNumber(
            String email, String phoneNumberOfT, String taxNumber);
    boolean existsByEmail(String email);
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByTaxNumber(String taxNumber);
}