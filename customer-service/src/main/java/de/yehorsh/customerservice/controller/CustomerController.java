package de.yehorsh.customerservice.controller;

import de.yehorsh.commonmodule.aspect.LogInfo;
import de.yehorsh.customerservice.dto.CustomerCreateDto;
import de.yehorsh.customerservice.dto.CustomerDto;
import de.yehorsh.customerservice.dto.CustomerUpdateDto;
import de.yehorsh.customerservice.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @LogInfo(name = "create_customer_endpoint")
    @PostMapping
    @PreAuthorize("hasAuthority('MANAGER')")
    public ResponseEntity<String> createCustomer(@RequestBody @Valid CustomerCreateDto customerCreateDto) {
        customerService.createNewCustomer(customerCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Customer was successfully created");
    }

    @LogInfo(name = "find_customer_endpoint")
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('ADMIN')")
    public ResponseEntity<CustomerDto> findCustomer(@PathVariable("id") UUID id) {
        CustomerDto customerDto = CustomerDto.fromCustomer(customerService.findCustomerById(id));
        return ResponseEntity.status(HttpStatus.OK).body(customerDto);
    }

    @LogInfo(name = "find_allCustomer_endpoint")
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<CustomerDto>> findAllCustomers() {
        List<CustomerDto> customerDtoList = customerService.findAllCustomers();
        return ResponseEntity.status(HttpStatus.OK).body(customerDtoList);
    }

    @LogInfo(name = "update_customer_endpoint")
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('CUSTOMER')")
    public ResponseEntity<CustomerUpdateDto> updateCustomer(@PathVariable("id") UUID id, @RequestBody @Valid CustomerUpdateDto customerUpdateDto) {
        customerService.updateCustomer(id, customerUpdateDto);
        return ResponseEntity.status(HttpStatus.OK).body(customerUpdateDto);
    }

    @LogInfo(name = "delete_customer_endpoint")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('MANAGER') or hasAuthority('CUSTOMER')")
    public ResponseEntity<String> deleteCustomer(@PathVariable("id") UUID id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Customer with ID " + id + " was deleted");
    }
}
