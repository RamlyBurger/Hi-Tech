package com.hitech.commerce.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SecurityFlowTests {

    @Autowired
    private MockMvc mockMvc;

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
    void seededCustomerCanLogin() throws Exception {
        mockMvc.perform(post("/login").with(csrf())
                .param("username", "customer")
                .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"))
                .andExpect(authenticated().withUsername("customer"));
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
    }
}
