package com.hitech.commerce.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.domain.CustomerOrder;
import com.hitech.commerce.domain.OrderLine;
import com.hitech.commerce.domain.OrderStatus;
import com.hitech.commerce.domain.PaymentStatus;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.domain.UserAccount;
import com.hitech.commerce.repository.CustomerOrderRepository;
import com.hitech.commerce.repository.ProductRepository;
import com.hitech.commerce.repository.UserAccountRepository;
import com.hitech.commerce.web.form.CheckoutForm;

@Service
public class OrderService {

    private final CartService cartService;
    private final CustomerOrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final UserAccountRepository userAccountRepository;
    private final AuditService auditService;

    public OrderService(CartService cartService, CustomerOrderRepository orderRepository,
            ProductRepository productRepository, UserAccountRepository userAccountRepository, AuditService auditService) {
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.userAccountRepository = userAccountRepository;
        this.auditService = auditService;
    }

    @Transactional
    public CustomerOrder checkout(String username, CheckoutForm form) {
        List<CartLine> lines = cartService.lines();
        if (lines.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        UserAccount user = userAccountRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Unknown account: " + username));
        CustomerOrder order = new CustomerOrder(user, form.getCustomerName(), form.getEmail(), form.getAddress(),
                form.getPaymentMethod());
        BigDecimal total = BigDecimal.ZERO;
        for (CartLine line : lines) {
            Product product = productRepository.findById(line.product().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + line.product().getId()));
            if (product.getStock() < line.quantity()) {
                throw new IllegalStateException(product.getName() + " does not have enough stock");
            }
            product.setStock(product.getStock() - line.quantity());
            order.addLine(new OrderLine(product, line.quantity()));
            total = total.add(line.lineTotal());
        }
        order.setTotalAmount(total);
        order.setStatus(OrderStatus.COMPLETED);
        order.setPaymentStatus(PaymentStatus.SIMULATED);
        CustomerOrder saved = orderRepository.save(order);
        auditService.record("ORDER_CHECKOUT", "CustomerOrder", saved.getId(), username);
        cartService.clear();
        return saved;
    }

    @Transactional(readOnly = true)
    public List<CustomerOrder> ordersFor(String username) {
        return orderRepository.findByUserAccountUsernameOrderByCreatedAtDesc(username);
    }

    @Transactional(readOnly = true)
    public CustomerOrder orderFor(String username, Long orderId) {
        return orderRepository.findByIdAndUserAccountUsername(orderId, username)
                .orElseThrow(() -> new NoSuchElementException("Order not found"));
    }
}
