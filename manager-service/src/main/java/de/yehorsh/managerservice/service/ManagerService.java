package de.yehorsh.managerservice.service;

import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.commonmodule.aspect.LogInfo;
import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.exception.ManagerNotFoundException;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import de.yehorsh.managerservice.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;
    private final RestTemplate restTemplate;


    @LogInfo(name = "create_manager_service")
    public Manager createNewManager(ManagerCreateDto managerCreateDto) {
        if (managerRepository.existsManagerByEmailOrLastNameOrPhoneNumber(
                managerCreateDto.email(),
                managerCreateDto.lastName(),
                managerCreateDto.phoneNumber())) {
            log.warn("Manager with email: {}, or phoneNumber: {}, or the same last name:  {}, already exists",
                    managerCreateDto.email(),
                    managerCreateDto.phoneNumber(),
                    managerCreateDto.lastName());
            throw new IllegalArgumentException("Manager with the provided details already exists");
        }

        UserDto userDto = new UserDto(
                managerCreateDto.email(),
                managerCreateDto.password(),
                "MANAGER");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "/users/create", userDto, String.class);

        if (responseEntity.getStatusCode() != HttpStatus.CREATED) {
            log.error("Failed to create user in AuthService: {}", responseEntity.getStatusCode());
            throw new RuntimeException("Failed to create user in AuthService");
        }

        String userId = responseEntity.getBody();
        if (userId == null || userId.isEmpty()) {
            log.error("AuthService returned an empty userId");
            throw new RuntimeException("Failed to retrieve userId from AuthService");
        }

        Manager createNewManager = Manager.builder()
                .firstName(managerCreateDto.firstName())
                .lastName(managerCreateDto.lastName())
                .email(managerCreateDto.email())
                .phoneNumber(managerCreateDto.phoneNumber())
                .managerStatus(ManagerStatus.ACTIVE)
                .userId(userId)
                .build();

        Manager savedManager = managerRepository.save(createNewManager);
        log.info("Successfully created new manager: {}", savedManager);

        return savedManager;
    }

    @LogInfo(name = "find_manager_by_id_service")
    public Manager findManagerById(UUID managerId) {
        log.info("Attempting to find manager with Id: {}", managerId);
        return managerRepository.findById(managerId)
                .map(manager -> {
                    log.info("Successfully found manager with Id: {}", managerId);
                    return manager;
                })
                .orElseThrow(() -> {
                    log.error("Manager with Id: {} not found", managerId);
                    return new ManagerNotFoundException("Manager with Id: " + managerId + " not found");
                });
    }

    @LogInfo(name = "update_manager_service")
    public void updateManager(UUID managerId, ManagerUpdateDto managerUpdateDto) {
        Manager manager = findManagerById(managerId);
        log.debug("Found manager for update with Id: {}", managerId);
        manager.setFirstName(managerUpdateDto.firstName());
        manager.setLastName(managerUpdateDto.lastName());
        manager.setEmail(managerUpdateDto.email());
        manager.setPhoneNumber(managerUpdateDto.phoneNumber());
        manager.setManagerStatus(managerUpdateDto.managerStatus());

        managerRepository.save(manager);
        log.info("Successfully updated manager with Id: {}", managerId);
    }

    @LogInfo(name = "delete_manager_service")
    public void deleteManager(UUID managerId) {
        if (managerId == null) {
            throw new IllegalArgumentException("Manager Id cannot be null");
        }

        Manager manager = findManagerById(managerId);
        log.debug("Found manager for deletion: {}", manager);

        managerRepository.delete(manager);
        log.info("Successfully deleted manager with Id: {}", managerId);
    }
}