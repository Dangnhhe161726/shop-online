package com.shopping.online.configs;


import com.shopping.online.sercurities.jwt.JwtAuthEntryPoint;
import com.shopping.online.sercurities.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private final JwtAuthEntryPoint authEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                String.format("%s/auth/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/categories/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/brands/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/products/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/sizes/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/colors/**", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/orders", apiPrefix)).permitAll()

                        .requestMatchers(HttpMethod.GET,
                                String.format("%s/order_details", apiPrefix)).permitAll()
                        .anyRequest()
                        .authenticated()
                )
                .exceptionHandling(e -> e.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
