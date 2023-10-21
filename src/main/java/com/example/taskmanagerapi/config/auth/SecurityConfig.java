package com.example.taskmanagerapi.config.auth;

import com.example.taskmanagerapi.helper.MessageHelper;
import com.example.taskmanagerapi.services.IJwtService;
import com.example.taskmanagerapi.services.MyUserDetailsService;
import com.example.taskmanagerapi.services.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final UserService userService;
    private final MessageHelper messageHelper;
    private final HandlerExceptionResolver resolver;
    private final PasswordEncoder passwordEncoder;
    private final IJwtService IJwtService;

    public SecurityConfig(UserService userService, MessageHelper messageHelper,
                          IJwtService IJwtService, @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.messageHelper = messageHelper;
        this.IJwtService = IJwtService;
        this.resolver = resolver;
        this.passwordEncoder = passwordEncoder;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(request -> request
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/user").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .formLogin(AbstractHttpConfigurer::disable);

        http.authenticationManager(authManager(http));
        http.addFilterBefore(new JwtAuthenticationFilter(this.userDetailsService(), this.messageHelper, this.IJwtService), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(new FilterChainExceptionHandler(this.resolver), JwtAuthenticationFilter.class);

        return http.build();
    }
    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(authenticationProvider());
        return authenticationManagerBuilder.build();
    }
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService());
        return provider;
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new MyUserDetailsService(this.userService);
    }
}
