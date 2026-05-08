package com.hitech.commerce.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.hitech.commerce.web.form.CheckoutForm;
import com.hitech.commerce.web.form.ProductForm;
import com.hitech.commerce.web.form.PromotionForm;
import com.hitech.commerce.web.form.RegistrationForm;

@SpringBootTest
@ActiveProfiles("test")
class FormValidationTests {

    @Autowired
    private Validator validator;

    @Test
    void checkoutRejectsUnsupportedPaymentMethod() {
        CheckoutForm form = new CheckoutForm();
        form.setCustomerName("Demo Customer");
        form.setEmail("customer@hitech.local");
        form.setAddress("Jalan Segamat / Labis, Johor");
        form.setPaymentMethod("wire-transfer<script>");

        assertThat(messagesFor(validator.validate(form)))
                .contains("Choose a supported payment method");
    }

    @Test
    void registrationRejectsUnsafeUsernameAndWeakPassword() {
        RegistrationForm form = new RegistrationForm();
        form.setUsername("../admin");
        form.setEmail("new-customer@hitech.local");
        form.setFullName("New Customer");
        form.setPassword("password");

        assertThat(messagesFor(validator.validate(form)))
                .contains("Use 3-80 letters, numbers, dots, underscores, or hyphens")
                .contains("Use at least one letter and one number");
    }

    @Test
    void productFormRejectsUnsafeSlugAndRemoteImagePath() {
        ProductForm form = validProductForm();
        form.setSlug("../admin");
        form.setImagePath("https://evil.example/product.png");
        form.setDetailImagePath("/admin/products");

        assertThat(messagesFor(validator.validate(form)))
                .contains("Use a lowercase URL slug")
                .contains("Use a local image path under /images");
    }

    @Test
    void promotionFormRejectsUnsupportedHighlightColor() {
        PromotionForm form = new PromotionForm();
        form.setTitle("Security Test Promotion");
        form.setProductName("Demo Product");
        form.setDescription("Promotion validation test");
        form.setHighlightColor("background:url(javascript:alert(1))");
        form.setStartsOn(LocalDate.now());
        form.setEndsOn(LocalDate.now().plusDays(1));

        assertThat(messagesFor(validator.validate(form)))
                .contains("Choose a supported highlight color");
    }

    private ProductForm validProductForm() {
        ProductForm form = new ProductForm();
        form.setSlug("security-test-product");
        form.setName("Security Test Product");
        form.setDescription("Security validation product");
        form.setPrice(BigDecimal.valueOf(99.99));
        form.setDiscountPercentage(0);
        form.setStock(5);
        form.setImagePath("/images/desktop.jpg");
        form.setDetailImagePath("/images/desktop1.jpg");
        form.setCategoryId(1L);
        form.setActive(true);
        return form;
    }

    private Set<String> messagesFor(Set<? extends ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(java.util.stream.Collectors.toSet());
    }
}
