package com.hitech.commerce.web;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CheckoutFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void customerCanAddToCartCheckoutAndViewOrder() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/items")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("productId", "1")
                .param("quantity", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cart"));

        mockMvc.perform(get("/cart")
                .session(session)
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Dell XPS 8950")));

        mockMvc.perform(post("/checkout")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("customerName", "Demo Customer")
                .param("email", "customer@hitech.local")
                .param("address", "Jalan Segamat / Labis, Johor")
                .param("paymentMethod", "Simulated card"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/orders"));

        mockMvc.perform(get("/orders")
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Order #")))
                .andExpect(content().string(containsString("Dell XPS 8950")));
    }
}
