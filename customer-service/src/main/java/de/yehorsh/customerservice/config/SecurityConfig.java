package de.yehorsh.customerservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    JwtFilterCustomer jwtFilterCustomer;
    public SecurityConfig(JwtFilterCustomer jwtFilterCustomer) {
        this.jwtFilterCustomer = jwtFilterCustomer;
    }
    private static final String ROLE_MANAGER = "MANAGER";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_CUSTOMER = "CUSTOMER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/customers").hasRole(ROLE_MANAGER)
                        .requestMatchers("/customers/{id}").hasAnyRole(ROLE_MANAGER, ROLE_ADMIN)
                        .requestMatchers("/customers/findAllCustomers").hasRole(ROLE_ADMIN)
                        .requestMatchers("/customers/update/{id}").hasAnyRole(ROLE_MANAGER, ROLE_CUSTOMER)
                        .requestMatchers("/customers/delete/{id}").hasAnyRole(ROLE_MANAGER, ROLE_CUSTOMER)
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtFilterCustomer, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
