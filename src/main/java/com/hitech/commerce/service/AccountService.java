package com.hitech.commerce.service;

import java.util.Locale;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.domain.Role;
import com.hitech.commerce.domain.UserAccount;
import com.hitech.commerce.repository.UserAccountRepository;
import com.hitech.commerce.web.form.RegistrationForm;

@Service
public class AccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public AccountService(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserAccount registerCustomer(RegistrationForm form) {
        UserAccount account = new UserAccount(
                normalizeIdentity(form.getUsername()),
                normalizeIdentity(form.getEmail()),
                form.getFullName().trim(),
                passwordEncoder.encode(form.getPassword()),
                Set.of(Role.CUSTOMER));
        return userAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userAccountRepository.existsByUsernameIgnoreCase(normalizeIdentity(username));
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userAccountRepository.existsByEmailIgnoreCase(normalizeIdentity(email));
    }

    private String normalizeIdentity(String value) {
        return value == null ? "" : value.trim().toLowerCase(Locale.ROOT);
    }
}
