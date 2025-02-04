package com.onlinemarket.OnlinemarketProjectFrontend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UserDetailsService userDetailsService() {
        return new CustomerUserDetailsService();
    }
	
    @Bean
    DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

    @Bean
    SecurityFilterChain configureHttp(HttpSecurity http) throws Exception {
        http.authenticationProvider(authenticationProvider());
        
        // return http.authorizeHttpRequests((auth) -> auth.anyRequest().permitAll()).build();

        http.authorizeHttpRequests((auth) -> auth
            .requestMatchers("/customer").authenticated()
            .anyRequest().permitAll())
        .formLogin(form -> form
            .loginPage("/login")
            .usernameParameter("email")
            .permitAll())
        .logout(logout -> logout.permitAll())
        .rememberMe( rem -> rem
            .key("AbcdefghijklMNOPQRS_1234567890") //fixing cookie key
            .tokenValiditySeconds(7*24*60*60));
        
        return http.build();
    }

    @Bean
    WebSecurityCustomizer configureWebSecurity() throws Exception{
        return (web) -> web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
    }
}

