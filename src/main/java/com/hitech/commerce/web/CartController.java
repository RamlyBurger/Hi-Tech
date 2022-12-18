package com.hitech.commerce.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hitech.commerce.service.CartService;

@Controller
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping({ "/cart", "/cart.html" })
    public String cart(Model model) {
        model.addAttribute("lines", cartService.lines());
        model.addAttribute("total", cartService.total());
        return "cart";
    }

    @PostMapping("/cart/items")
    public String add(@RequestParam Long productId, @RequestParam(defaultValue = "1") int quantity,
            RedirectAttributes redirectAttributes) {
        try {
            cartService.addProduct(productId, quantity);
            redirectAttributes.addFlashAttribute("cartMessage", "Product added to cart");
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("cartError", ex.getMessage());
        }
        return "redirect:/cart";
    }

    @PostMapping("/cart/items/{productId}/update")
    public String update(@PathVariable Long productId, @RequestParam int quantity) {
        cartService.updateProduct(productId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/cart/items/{productId}/remove")
    public String remove(@PathVariable Long productId) {
        cartService.removeProduct(productId);
        return "redirect:/cart";
    }
}
