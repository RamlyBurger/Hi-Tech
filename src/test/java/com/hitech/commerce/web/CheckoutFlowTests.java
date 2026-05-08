package com.hitech.commerce.web;

import static org.hamcrest.Matchers.containsString;
import static org.assertj.core.api.Assertions.assertThat;
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

import java.util.Set;

import com.hitech.commerce.domain.CustomerOrder;
import com.hitech.commerce.domain.OrderLine;
import com.hitech.commerce.domain.OrderStatus;
import com.hitech.commerce.domain.PaymentStatus;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.domain.Role;
import com.hitech.commerce.domain.UserAccount;
import com.hitech.commerce.repository.AuditLogRepository;
import com.hitech.commerce.repository.CustomerOrderRepository;
import com.hitech.commerce.repository.ProductRepository;
import com.hitech.commerce.repository.UserAccountRepository;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CheckoutFlowTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

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

        assertThat(auditLogRepository.findByActionOrderByCreatedAtDesc("ORDER_CHECKOUT"))
                .anySatisfy(log -> {
                    assertThat(log.getActor()).isEqualTo("customer");
                    assertThat(log.getTargetType()).isEqualTo("CustomerOrder");
                });
    }

    @Test
    void orderDetailIsLimitedToOwningCustomer() throws Exception {
        MockHttpSession session = new MockHttpSession();

        mockMvc.perform(post("/cart/items")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("productId", "1")
                .param("quantity", "1"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/checkout")
                .session(session)
                .with(user("customer").roles("CUSTOMER"))
                .with(csrf())
                .param("customerName", "Demo Customer")
                .param("email", "customer@hitech.local")
                .param("address", "Jalan Segamat / Labis, Johor")
                .param("paymentMethod", "Simulated card"))
                .andExpect(status().is3xxRedirection());

        CustomerOrder ownOrder = customerOrderRepository.findByUserAccountUsernameOrderByCreatedAtDesc("customer")
                .get(0);
        CustomerOrder otherOrder = saveOtherCustomerOrder();

        mockMvc.perform(get("/orders/" + ownOrder.getId())
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Order #" + ownOrder.getId())))
                .andExpect(content().string(containsString("Dell XPS 8950")));

        mockMvc.perform(get("/orders/" + otherOrder.getId())
                .with(user("customer").roles("CUSTOMER")))
                .andExpect(status().isNotFound());
    }

    private CustomerOrder saveOtherCustomerOrder() {
        UserAccount other = userAccountRepository.save(new UserAccount(
                "phase2-customer", "phase2-customer@hitech.local", "Phase Two Customer", "{noop}password",
                Set.of(Role.CUSTOMER)));
        Product product = productRepository.findById(1L).orElseThrow();
        CustomerOrder order = new CustomerOrder(other, "Other Customer", other.getEmail(), "Other address",
                "Simulated card");
        order.addLine(new OrderLine(product, 1));
        order.setTotalAmount(product.getSalePrice());
        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentStatus(PaymentStatus.SIMULATED);
        return customerOrderRepository.save(order);
    }
}
