package com.hitech.commerce.web;

import java.security.Principal;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

import com.hitech.commerce.service.CartService;
import com.hitech.commerce.service.OrderService;
import com.hitech.commerce.web.form.CheckoutForm;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping({ "/checkout", "/payment.html" })
    public String checkout(Model model) {
        if (cartService.isEmpty()) {
            return "redirect:/cart";
        }
        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new CheckoutForm());
        }
        model.addAttribute("lines", cartService.lines());
        model.addAttribute("total", cartService.total());
        return "checkout";
    }

    @PostMapping("/checkout")
    public String submit(@Valid @ModelAttribute CheckoutForm checkoutForm, BindingResult bindingResult,
            Principal principal, Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("lines", cartService.lines());
            model.addAttribute("total", cartService.total());
            return "checkout";
        }
        try {
            Long orderId = orderService.checkout(principal.getName(), checkoutForm).getId();
            redirectAttributes.addFlashAttribute("orderMessage", "Order #" + orderId + " completed");
            return "redirect:/orders";
        } catch (RuntimeException ex) {
            model.addAttribute("checkoutError", ex.getMessage());
            model.addAttribute("lines", cartService.lines());
            model.addAttribute("total", cartService.total());
            return "checkout";
        }
    }

    @GetMapping("/orders")
    public String orders(Principal principal, Model model) {
        model.addAttribute("orders", orderService.ordersFor(principal.getName()));
        return "orders";
    }

    @GetMapping("/orders/{id}")
    public String order(@PathVariable Long id, Principal principal, Model model) {
        try {
            model.addAttribute("order", orderService.orderFor(principal.getName(), id));
            return "order-detail";
        } catch (NoSuchElementException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found", ex);
        }
    }
}
