package de.yehorsh.authservice.service;

import de.yehorsh.authservice.dto.JwtAuthenticationDto;
import de.yehorsh.authservice.dto.RefreshTokenDto;
import de.yehorsh.authservice.dto.UserDro;
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
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final RoleRepository roleRepository;

    private final UserProvider userProvider;

    public UUID registerUser(UserDro userDro) {
        User user = createUser(userDro, RoleName.ROLE_MAIN_ADMIN.toString());
        return user.getUserId();
    }

    public User createUser(UserDro userDro, String roleName) {

        User user = new User();

        Role role =
                roleRepository.findByName(roleName).orElseThrow(() ->
                        new RoleNotFoundException(String.format("Role %s not found!", userDro.roleName())));

        user.setUsername(userDro.userName());
        user.setPassword(passwordEncoder.encode(userDro.password()));
        user.setEmail(userDro.email());
        user.setRoles(role);
        user.setStatus(UserStatus.ACTIVATED);

        try {
            userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new UserAlreadyExistsException(
                    String.format("User with email %s already exists.", userDro.email())
            );
        }

        return user;
    }

    public RoleName getAuthorizedUserRole() {
        return RoleName.valueOf(userProvider.getCurrentUser().getRoles().getName());
    }

    public UserDro findById(String id) {
        User user = userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %s not found", id)));
        User currentUser = userProvider.getCurrentUser();
        if (!currentUser.getRoles().getName().equals(RoleName.ROLE_MAIN_ADMIN.toString())
                && !currentUser.getRoles().getName().equals(RoleName.ROLE_ADMIN.toString())
                && !currentUser.getRoles().getName().equals(RoleName.ROLE_MANAGER.toString())
                && !currentUser.getUserId().equals(user.getUserId())) {
            throw new AccessDeniedException("Access denied");
        }

        if (currentUser.getRoles().getName().equals(RoleName.ROLE_MANAGER.toString())
                && !user.getRoles().getName().equals(RoleName.ROLE_CUSTOMER.toString())) {
            throw new AccessDeniedException("Managers can only view customer data");
        }
        return new UserDro(
                user.getUsername(),
                user.getUserId().toString(),
                user.getEmail(),
                user.getRoles().getName());
    }

    public JwtAuthenticationDto singIn(UserCredentialsDto userCredentialsDto) {
        User user = findByCredentials(userCredentialsDto);
        return jwtService.generateAuthToken(user.getUsername());
    }

    public JwtAuthenticationDto refreshToken(RefreshTokenDto refreshTokenDto) {
        String refreshToken = refreshTokenDto.getRefreshToken();
        if (refreshToken != null && jwtService.validateJwtToken(refreshToken)) {
            User user = findByEmail(jwtService.getUsernameFromToken(refreshToken));
            return jwtService.refreshBaseToken(user.getUsername(), refreshToken);
        }
        throw new AuthenticationException("Refresh token is not valid");
    }

    private User findByCredentials(UserCredentialsDto userCredentialsDto) {
        Optional<User> optionalUsers = userRepository.findByUsername(userCredentialsDto.getEmail());
        if (optionalUsers.isPresent()) {
            User user = optionalUsers.get();
            if (passwordEncoder.matches(userCredentialsDto.getPassword(), user.getPassword())) {
                if (user.getStatus() != UserStatus.ACTIVATED) {
                    return user;
                } else {
                    throw new AuthenticationException("User is not activated");
                }
            }
        }
        throw new AuthenticationException("Email or password is not correct");
    }

    private User findByEmail(String email) {
        return userRepository.findByUsername(email).orElseThrow(() ->
                new UserNotFoundException(String.format("User with email %s not found", email)));
    }
}
