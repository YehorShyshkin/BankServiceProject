package de.yehorsh.customerservice.config;

import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.authservice.security.CustomUserDetails;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestConfiguration
public class TestSecurityConfig {
    @Bean
    @Primary
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return email -> {
            System.out.println("Users in DB: " + userRepository.findAll());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

            return new CustomUserDetails(user);
        };
    }
}
