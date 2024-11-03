package de.yehorsh.managerservice.service;

import de.yehorsh.managerservice.config.LogInfo;
import de.yehorsh.managerservice.dto.ManagerCreateDto;
import de.yehorsh.managerservice.dto.ManagerUpdateDto;
import de.yehorsh.managerservice.exception.ManagerNotFoundException;
import de.yehorsh.managerservice.model.Manager;
import de.yehorsh.managerservice.model.enums.ManagerStatus;
import de.yehorsh.managerservice.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ManagerService {
    private final ManagerRepository managerRepository;

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
            throw new IllegalArgumentException("Manager with the provided details already exists.");
        }

        Manager createNewManager = Manager.builder()
                .firstName(managerCreateDto.firstName())
                .lastName(managerCreateDto.lastName())
                .email(managerCreateDto.email())
                .phoneNumber(managerCreateDto.phoneNumber())
                .managerStatus(ManagerStatus.ACTIVE)
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
                    return new ManagerNotFoundException("Manager with Id: " + managerId + " not found.");
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
            throw new IllegalArgumentException("Manager Id cannot be null.");
        }

        Manager manager = findManagerById(managerId);
        log.debug("Found manager for deletion: {}", manager);

        managerRepository.delete(manager);
        log.info("Successfully deleted manager with Id: {}", managerId);
    }
}