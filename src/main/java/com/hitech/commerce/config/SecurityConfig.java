package com.hitech.commerce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/home", "/home.html", "/about", "/about-us", "/about us.html", "/faq",
                        "/faq.html", "/feedback", "/feedback.html", "/membership", "/membership.html", "/events",
                        "/events.html", "/products", "/products.html", "/products/**", "/proDesktop.html",
                        "/proLaptop.html", "/proSmartphones.html", "/proAccessories.html", "/singleprod*.html",
                        "/css/**", "/fonts/**", "/icon/**", "/images/**", "/js/**", "/vid/**", "/login",
                        "/login.html", "/register")
                .permitAll()
                .antMatchers("/admin/**").hasRole("ADMIN")
                .antMatchers("/cart/**", "/checkout/**", "/orders/**").hasRole("CUSTOMER")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .permitAll();
        return http.build();
    }
}
