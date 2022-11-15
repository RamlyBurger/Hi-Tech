package com.hitech.commerce.web;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.hitech.commerce.service.CatalogService;
import com.hitech.commerce.service.ContentService;

@Controller
public class PublicController {

    private static final Map<String, String> LEGACY_PRODUCTS = Map.ofEntries(
            Map.entry("singleprod1.html", "dell-xps-8950"),
            Map.entry("singleprod2.html", "hp-omen-45l"),
            Map.entry("singleprod3.html", "hp-pavilion-gaming-desktop"),
            Map.entry("singleprod4.html", "falcon-northwest-tiki"),
            Map.entry("singleprod5.html", "rog-strix-g15"),
            Map.entry("singleprod6.html", "tuf-gaming-f15"),
            Map.entry("singleprod7.html", "apple-macbook1"),
            Map.entry("singleprod8.html", "hp-victus-16"),
            Map.entry("singleprod9.html", "red-magic-7"),
            Map.entry("singleprod10.html", "black-shark-5-pro"),
            Map.entry("singleprod11.html", "asus-rog-phone-6-pro"),
            Map.entry("singleprod12.html", "poco-f4-gt"),
            Map.entry("singleprod13.html", "g502-hero"),
            Map.entry("singleprod14.html", "razer-basilisk-ultimate"),
            Map.entry("singleprod15.html", "logitech-signature-k650"),
            Map.entry("singleprod16.html", "razer-pro-type-ultra"));

    private final CatalogService catalogService;
    private final ContentService contentService;

    public PublicController(CatalogService catalogService, ContentService contentService) {
        this.catalogService = catalogService;
        this.contentService = contentService;
    }

    @GetMapping({ "/", "/home", "/home.html" })
    public String home(Model model) {
        model.addAttribute("featuredProducts", catalogService.featuredProducts());
        model.addAttribute("promotions", contentService.listPromotions().stream().limit(3).toList());
        return "home";
    }

    @GetMapping({ "/about", "/about-us", "/about us.html" })
    public String about() {
        return "about";
    }

    @GetMapping({ "/faq", "/faq.html" })
    public String faq() {
        return "faq";
    }

    @GetMapping({ "/feedback", "/feedback.html" })
    public String feedback() {
        return "feedback";
    }

    @GetMapping({ "/membership", "/membership.html" })
    public String membership() {
        return "membership";
    }

    @GetMapping({ "/events", "/events.html" })
    public String events(Model model) {
        model.addAttribute("events", contentService.listEvents());
        model.addAttribute("promotions", contentService.listPromotions());
        return "events";
    }

    @GetMapping({ "/products", "/products.html" })
    public String products(@RequestParam(name = "q", required = false) String query, Model model) {
        model.addAttribute("products", catalogService.listProducts(null, query));
        model.addAttribute("query", query);
        model.addAttribute("pageTitle", query == null || query.isBlank() ? "Products" : "Search results");
        return "products";
    }

    @GetMapping("/products/{categoryCode}")
    public String category(@PathVariable String categoryCode, Model model) {
        try {
            model.addAttribute("category", catalogService.getCategory(categoryCode));
            model.addAttribute("products", catalogService.listProducts(categoryCode, null));
            return "category";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/products/item/{slug}")
    public String productDetail(@PathVariable String slug, Model model) {
        try {
            model.addAttribute("product", catalogService.getProduct(slug));
            return "product-detail";
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, ex.getMessage(), ex);
        }
    }

    @GetMapping("/proDesktop.html")
    public String legacyDesktop() {
        return "redirect:/products/desktop";
    }

    @GetMapping("/proLaptop.html")
    public String legacyLaptop() {
        return "redirect:/products/laptop";
    }

    @GetMapping("/proSmartphones.html")
    public String legacySmartphones() {
        return "redirect:/products/smartphone";
    }

    @GetMapping("/proAccessories.html")
    public String legacyAccessories() {
        return "redirect:/products/accessories";
    }

    @GetMapping("/{legacyProduct:singleprod[0-9]+\\.html}")
    public String legacyProduct(@PathVariable String legacyProduct) {
        String slug = LEGACY_PRODUCTS.get(legacyProduct);
        if (slug == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return "redirect:/products/item/" + slug;
    }
}
