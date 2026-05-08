package com.hitech.commerce.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.ActiveProfiles;

import com.hitech.commerce.domain.UserAccount;
import com.hitech.commerce.web.form.RegistrationForm;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    void registrationStoresNormalizedIdentityAndLookupIsCaseInsensitive() {
        RegistrationForm form = new RegistrationForm();
        form.setUsername("Phase3User");
        form.setEmail("Phase3User@HiTech.Local");
        form.setFullName("Phase Three User");
        form.setPassword("Password1");

        UserAccount account = accountService.registerCustomer(form);

        assertThat(account.getUsername()).isEqualTo("phase3user");
        assertThat(account.getEmail()).isEqualTo("phase3user@hitech.local");
        assertThat(accountService.usernameExists(" PHASE3USER ")).isTrue();
        assertThat(accountService.emailExists("PHASE3USER@HITECH.LOCAL")).isTrue();

        UserDetails userDetails = userDetailsService.loadUserByUsername("PHASE3USER");
        assertThat(userDetails.getUsername()).isEqualTo("phase3user");
    }
}
