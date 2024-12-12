package de.yehorsh.customerservice.config;

import de.yehorsh.authservice.model.entity.User;
import de.yehorsh.authservice.repository.UserRepository;
import de.yehorsh.authservice.service.CustomUserDetails;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    public UserRepository userRepository() {
        return Mockito.mock(UserRepository.class);
    }

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
