package de.yehorsh.managerservice.config;


import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class TestSecurityConfig {
//    @Bean
//    @Primary
//    public UserDetailsService userDetailsService() {
//        return email -> {
////  We can not use in one microservice classes from another, except common. Here we sould  make REST request to auth-service
//            //            System.out.println("Users in DB: " + userRepository.findAll());
////            User user = userRepository.findByEmail(email)
////                    .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
//
//            return new CustomUserDetails(null);
//        };
//    }
}
