package de.yehorsh.authservice.service;

import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import de.yehorsh.authservice.dto.RefreshTokenDto;
import de.yehorsh.authservice.dto.UserDto;
import de.yehorsh.authservice.dto.UserCredentialsDto;
import de.yehorsh.authservice.exception.AuthenticationException;
import de.yehorsh.authservice.exception.RoleNotFoundException;
import de.yehorsh.authservice.exception.UserAlreadyExistsException;
import de.yehorsh.authservice.exception.UserNotFoundException;
import de.yehorsh.authservice.model.entity.Role;
import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.model.enums.RoleName;
import de.yehorsh.authservice.model.enums.UserStatus;
import de.yehorsh.authservice.repository.RoleRepository;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.authservice.security.UserProvider;
import de.yehorsh.authservice.security.jwt.JwtService;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final RoleRepository roleRepository;

    private final UserProvider userProvider;

    @Transactional
    public User registerUser(UserDto userDto) {
        return createUser(userDto, RoleName.valueOf(userDto.roleName()));
    }

    public User createUser(UserDto userDto, RoleName roleName) {
        if (userRepository.existsByEmail(userDto.email())){
            log.warn("User with email {} already exists", userDto.email());
            throw new UserAlreadyExistsException(
                    String.format("User with email %s already exists.", userDto.email())
            );
        }

        User user = new User();

        Role role = roleRepository.findByName(roleName.name())
                .orElseThrow(() -> new RoleNotFoundException(
                        String.format("Role %s not found!", roleName)
                ));

        user.setEmail(userDto.email());
        user.setPassword(passwordEncoder.encode(userDto.password()));
        user.setRoles(role);
        user.setStatus(UserStatus.ACTIVATED);

        User savedUser = userRepository.save(user);

        log.info("Successfully created new user: {}", savedUser);

        return savedUser;
    }

    public RoleName getAuthorizedUserRole() {
        return RoleName.valueOf(userProvider.getCurrentUser().getRoles().getName());
    }

    @Transactional
    public UserDto findById(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
        User currentUser = userProvider.getCurrentUser();
        if (!currentUser.getRoles().getName().equals(RoleName.ADMIN.toString())
                && !currentUser.getRoles().getName().equals(RoleName.MANAGER.toString())
                && !currentUser.getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        if (currentUser.getRoles().getName().equals(RoleName.MANAGER.toString())
                && !user.getRoles().getName().equals(RoleName.CUSTOMER.toString())) {
            throw new AccessDeniedException("Managers can only view customer data");
        }

        return new UserDto(
                user.getEmail(),
                null,
                user.getRoles().getName());
    }

    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user.getEmail());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getEmailFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getEmail(), refreshToken);
        }
        throw new  AuthenticationException("Invalid refresh token");
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userCredentialsDto.getEmail());
        if (optionalUser.isPresent()){
            User user = optionalUser.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())){
                return user;
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    private User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s not found", email)));
    }
}
