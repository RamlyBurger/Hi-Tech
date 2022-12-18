package com.hitech.commerce.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.cart.ShoppingCart;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.repository.ProductRepository;

@Service
public class CartService {

    private final ShoppingCart cart;
    private final ProductRepository productRepository;

    public CartService(ShoppingCart cart, ProductRepository productRepository) {
        this.cart = cart;
        this.productRepository = productRepository;
    }

    @Transactional(readOnly = true)
    public void addProduct(Long productId, int quantity) {
        Product product = getProduct(productId);
        if (product.getStock() <= 0) {
            throw new IllegalStateException("Product is out of stock");
        }
        cart.add(productId, Math.min(quantity, product.getStock()));
    }

    public void updateProduct(Long productId, int quantity) {
        cart.update(productId, quantity);
    }

    public void removeProduct(Long productId) {
        cart.remove(productId);
    }

    public void clear() {
        cart.clear();
    }

    public boolean isEmpty() {
        return cart.isEmpty();
    }

    public int count() {
        return cart.count();
    }

    @Transactional(readOnly = true)
    public List<CartLine> lines() {
        return cart.getItems().entrySet().stream()
                .map(entry -> {
                    Product product = getProduct(entry.getKey());
                    int quantity = Math.min(entry.getValue(), product.getStock());
                    return new CartLine(product, quantity, product.getSalePrice().multiply(BigDecimal.valueOf(quantity)));
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public BigDecimal total() {
        return lines().stream()
                .map(CartLine::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + productId));
    }
}
