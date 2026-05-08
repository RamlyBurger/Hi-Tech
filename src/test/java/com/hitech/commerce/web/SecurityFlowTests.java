package com.hitech.commerce.web;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import com.hitech.commerce.domain.Category;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.repository.AuditLogRepository;
import com.hitech.commerce.repository.CategoryRepository;
import com.hitech.commerce.repository.ProductRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void publicStorefrontPagesAreVisible() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Featured products")));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dell XPS 8950")));
    }

    @Test
    void securityHeadersAreAppliedToPublicPages() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Security-Policy", containsString("default-src 'self'")))
                .andExpect(header().string("Content-Security-Policy", containsString("frame-ancestors 'none'")))
                .andExpect(header().string("Referrer-Policy", "same-origin"))
                .andExpect(header().string("Permissions-Policy", containsString("camera=()")))
                .andExpect(header().string("X-Frame-Options", "DENY"));
    }

    @Test
    void seededCustomerCanLogin() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                .param("username", "Customer")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("customer"));
    }

    @Test
    void registrationRejectsCaseInsensitiveDuplicateIdentity() throws Exception {
        mockMvc.perform(post("/register").with(csrf())
                .param("username", "Customer")
                .param("email", "CUSTOMER@HITECH.LOCAL")
                .param("fullName", "Duplicate Customer")
                .param("password", "Password1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(result -> assertThat(result.getFlashMap().get("authMode")).isEqualTo("register"))
                .andExpect(result -> assertThat(result.getFlashMap()
                        .containsKey("org.springframework.validation.BindingResult.registrationForm")).isTrue());
    }

    @Test
    void adminRoutesRequireAdminRole() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/admin").with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isForbidden());

        mockMvc.perform(get("/admin").with(user("admin").roles("ADMIN", "CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Admin Dashboard")));
    }

    @Test
    void mutatingRoutesRejectMissingCsrfToken() throws Exception {
        mockMvc.perform(post("/admin/products").with(user("admin").roles("ADMIN", "CUSTOMER")))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/cart/items").with(user("customer").roles("CUSTOMER"))
                .param("productId", "1")
                .param("quantity", "1"))
                .andExpect(status().isForbidden());

        mockMvc.perform(post("/checkout").with(user("customer").roles("CUSTOMER"))
                .param("customerName", "Demo Customer")
                .param("email", "customer@hitech.local")
                .param("address", "Jalan Segamat / Labis, Johor")
                .param("paymentMethod", "Simulated card"))
                .andExpect(status().isForbidden());
    }

    @Test
    void inactiveProductsCannotBeViewedOrAddedToCart() throws Exception {
        Product inactiveProduct = saveProduct("inactive-security-product", "Inactive Security Product", false);
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(get("/products/item/" + inactiveProduct.getSlug()))
                .andExpect(status().isNotFound());

        mockMvc.perform(post("/cart/items")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("productId", inactiveProduct.getId().toString())
                .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        mockMvc.perform(get("/cart")
                .session(session)
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Inactive Security Product"))));
    }

    @Test
    void cartDropsProductThatBecomesInactiveAfterItWasAdded() throws Exception {
        Product product = saveProduct("cart-deactivated-product", "Cart Deactivated Product", true);
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/items")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("productId", product.getId().toString())
                .param("quantity", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        product.setActive(false);
        productRepository.save(product);

        mockMvc.perform(get("/cart")
                .session(session)
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Cart Deactivated Product"))));
    }

    @Test
    void adminCanCreateProduct() throws Exception {
        mockMvc.perform(post("/admin/products")
                .with(user("admin").roles("ADMIN", "CUSTOMER"))
                .with(csrf())
                .param("slug", "mockmvc-admin-product")
                .param("name", "MockMvc Admin Product")
                .param("description", "Created through the admin product form.")
                .param("price", "99.99")
                .param("discountPercentage", "0")
                .param("stock", "3")
                .param("categoryId", "4")
                .param("imagePath", "/images/logitech1.jpg")
                .param("detailImagePath", "/images/logitech1b.jpg")
                .param("active", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/products"));

        mockMvc.perform(get("/products/item/mockmvc-admin-product"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("MockMvc Admin Product")));

        Product product = productRepository.findBySlug("mockmvc-admin-product").orElseThrow();
        assertThat(auditLogRepository.existsByActionAndTargetTypeAndTargetId(
                "PRODUCT_SAVED", "Product", product.getId())).isTrue();
    }

    private Product saveProduct(String slug, String name, boolean active) {
        Category category = categoryRepository.findByCode("desktop").orElseThrow();
        Product product = new Product(slug, name, "Product used by security tests.",
                BigDecimal.valueOf(99), 0, 5, "/images/desktop.jpg", "/images/desktop1.jpg", category);
        product.setActive(active);
        return productRepository.save(product);
    }
}
