package com.hitech.commerce.web;

import java.util.List;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.hitech.commerce.cart.ShoppingCart;
import com.hitech.commerce.domain.Category;
import com.hitech.commerce.service.CatalogService;

@ControllerAdvice(annotations = Controller.class)
public class GlobalModelAdvice {

    private final CatalogService catalogService;
    private final ObjectProvider<ShoppingCart> cartProvider;

    public GlobalModelAdvice(CatalogService catalogService, ObjectProvider<ShoppingCart> cartProvider) {
        this.catalogService = catalogService;
        this.cartProvider = cartProvider;
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

    @ModelAttribute("cartCount")
    public int cartCount() {
        ShoppingCart cart = cartProvider.getIfAvailable();
        return cart == null ? 0 : cart.count();
    }
}
