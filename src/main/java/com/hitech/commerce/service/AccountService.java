package com.hitech.commerce.service;

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
                form.getUsername().trim(),
                form.getEmail().trim(),
                form.getFullName().trim(),
                passwordEncoder.encode(form.getPassword()),
                Set.of(Role.CUSTOMER));
        return userAccountRepository.save(account);
    }

    @Transactional(readOnly = true)
    public boolean usernameExists(String username) {
        return userAccountRepository.existsByUsername(username);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return userAccountRepository.existsByEmail(email);
    }
}
