package com.hitech.commerce.web;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hitech.commerce.domain.Category;
import com.hitech.commerce.service.CatalogService;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAdvice {

    private final CatalogService catalogService;

    public GlobalModelAdvice(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @ModelAttribute("navCategories")
    public List<Category> navCategories() {
        return catalogService.listCategories();
    }

    @ModelAttribute("currentUser")
    public String currentUser(Authentication authentication) {
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return null;
        }
        return authentication.getName();
    }

    @ModelAttribute("isAdmin")
    public boolean isAdmin(Authentication authentication) {
        return authentication != null && authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
