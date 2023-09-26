package com.hitech.commerce.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hitech.commerce.service.AccountService;
import com.hitech.commerce.web.form.RegistrationForm;

@Controller
public class AuthController {

    private final AccountService accountService;

    public AuthController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping({ "/login", "/login.html" })
    public String login(Model model) {
        if (!model.containsAttribute("registrationForm")) {
            model.addAttribute("registrationForm", new RegistrationForm());
        }
        return "login";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegistrationForm registrationForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (accountService.usernameExists(registrationForm.getUsername())) {
            bindingResult.rejectValue("username", "duplicate", "Username is already taken");
        }
        if (accountService.emailExists(registrationForm.getEmail())) {
            bindingResult.rejectValue("email", "duplicate", "Email is already registered");
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.registrationForm",
                    bindingResult);
            redirectAttributes.addFlashAttribute("registrationForm", registrationForm);
            redirectAttributes.addFlashAttribute("authMode", "register");
            return "redirect:/login";
        }
        accountService.registerCustomer(registrationForm);
        redirectAttributes.addFlashAttribute("registrationSuccess", true);
        return "redirect:/login";
    }
}
