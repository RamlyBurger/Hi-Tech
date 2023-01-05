package com.hitech.commerce.web;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.hitech.commerce.service.AdminService;
import com.hitech.commerce.web.form.EventForm;
import com.hitech.commerce.web.form.ProductForm;
import com.hitech.commerce.web.form.PromotionForm;

@Controller
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/admin")
    public String dashboard(Model model) {
        model.addAttribute("productCount", adminService.products().size());
        model.addAttribute("eventCount", adminService.events().size());
        model.addAttribute("promotionCount", adminService.promotions().size());
        return "admin/index";
    }

    @GetMapping("/admin/products")
    public String products(Model model) {
        model.addAttribute("products", adminService.products());
        return "admin/products";
    }

    @GetMapping("/admin/products/new")
    public String newProduct(Model model) {
        model.addAttribute("productForm", new ProductForm());
        addCategories(model);
        return "admin/product-form";
    }

    @GetMapping("/admin/products/{id}/edit")
    public String editProduct(@PathVariable Long id, Model model) {
        model.addAttribute("productForm", ProductForm.from(adminService.product(id)));
        addCategories(model);
        return "admin/product-form";
    }

    @PostMapping("/admin/products")
    public String saveProduct(@Valid @ModelAttribute ProductForm productForm, BindingResult bindingResult,
            Model model, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            addCategories(model);
            return "admin/product-form";
        }
        adminService.saveProduct(productForm);
        redirectAttributes.addFlashAttribute("adminMessage", "Product saved");
        return "redirect:/admin/products";
    }

    @PostMapping("/admin/products/{id}/deactivate")
    public String deactivateProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deactivateProduct(id);
        redirectAttributes.addFlashAttribute("adminMessage", "Product deactivated");
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/events")
    public String events(Model model) {
        model.addAttribute("events", adminService.events());
        return "admin/events";
    }

    @GetMapping("/admin/events/new")
    public String newEvent(Model model) {
        model.addAttribute("eventForm", new EventForm());
        return "admin/event-form";
    }

    @GetMapping("/admin/events/{id}/edit")
    public String editEvent(@PathVariable Long id, Model model) {
        model.addAttribute("eventForm", EventForm.from(adminService.event(id)));
        return "admin/event-form";
    }

    @PostMapping("/admin/events")
    public String saveEvent(@Valid @ModelAttribute EventForm eventForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/event-form";
        }
        adminService.saveEvent(eventForm);
        redirectAttributes.addFlashAttribute("adminMessage", "Event saved");
        return "redirect:/admin/events";
    }

    @PostMapping("/admin/events/{id}/deactivate")
    public String deactivateEvent(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deactivateEvent(id);
        redirectAttributes.addFlashAttribute("adminMessage", "Event deactivated");
        return "redirect:/admin/events";
    }

    @GetMapping("/admin/promotions")
    public String promotions(Model model) {
        model.addAttribute("promotions", adminService.promotions());
        return "admin/promotions";
    }

    @GetMapping("/admin/promotions/new")
    public String newPromotion(Model model) {
        model.addAttribute("promotionForm", new PromotionForm());
        return "admin/promotion-form";
    }

    @GetMapping("/admin/promotions/{id}/edit")
    public String editPromotion(@PathVariable Long id, Model model) {
        model.addAttribute("promotionForm", PromotionForm.from(adminService.promotion(id)));
        return "admin/promotion-form";
    }

    @PostMapping("/admin/promotions")
    public String savePromotion(@Valid @ModelAttribute PromotionForm promotionForm, BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "admin/promotion-form";
        }
        adminService.savePromotion(promotionForm);
        redirectAttributes.addFlashAttribute("adminMessage", "Promotion saved");
        return "redirect:/admin/promotions";
    }

    @PostMapping("/admin/promotions/{id}/deactivate")
    public String deactivatePromotion(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        adminService.deactivatePromotion(id);
        redirectAttributes.addFlashAttribute("adminMessage", "Promotion deactivated");
        return "redirect:/admin/promotions";
    }

    private void addCategories(Model model) {
        model.addAttribute("categories", adminService.categories());
    }
}
