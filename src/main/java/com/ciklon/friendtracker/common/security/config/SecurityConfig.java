package com.ciklon.friendtracker.common.security.config;

import com.ciklon.friendtracker.common.auth.annotation.EnableJwtUtils;
import com.ciklon.friendtracker.common.auth.jwt.JwtTokenFilter;
import com.ciklon.friendtracker.common.auth.util.JwtUtils;
import com.ciklon.friendtracker.common.security.constant.ApiPaths;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import static com.ciklon.friendtracker.common.security.constant.SecurityConstants.AUTH_WHITELIST;


@Configuration
@Slf4j
@EnableJwtUtils
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    @SneakyThrows
    public SecurityFilterChain securityFilterChainJwt(
            HttpSecurity http,
            JwtTokenFilter jwtTokenFilter,
            AuthenticationEntryPoint authenticationEntryPoint
    ) {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(c -> c
                        .authenticationEntryPoint(authenticationEntryPoint)
                )
                .authorizeHttpRequests(request ->
                                        request.requestMatchers(AUTH_WHITELIST).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.REGISTER).permitAll()
                                                .requestMatchers(HttpMethod.POST, ApiPaths.LOGIN).permitAll()
                                                .requestMatchers(HttpMethod.GET, "/api/v1/fill-database").permitAll()
                                                .anyRequest().authenticated()
                )
                .anonymous(AbstractHttpConfigurer::disable)
                .addFilterBefore(jwtTokenFilter, AuthorizationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint(@Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver) {
        return (request, response, ex) -> resolver.resolveException(request, response, null, ex);
    }

    @Bean
    public JwtTokenFilter jwtTokenFilter(JwtUtils jwtUtils) {
        return new JwtTokenFilter(jwtUtils);
    }
}
