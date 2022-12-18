package com.hitech.commerce.cart;

import java.io.Serializable;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

@Component
@SessionScope
public class ShoppingCart implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<Long, Integer> items = new LinkedHashMap<>();

    public void add(Long productId, int quantity) {
        int safeQuantity = Math.max(1, quantity);
        items.merge(productId, safeQuantity, Integer::sum);
    }

    public void update(Long productId, int quantity) {
        if (quantity <= 0) {
            items.remove(productId);
            return;
        }
        items.put(productId, quantity);
    }

    public void remove(Long productId) {
        items.remove(productId);
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int count() {
        return items.values().stream().mapToInt(Integer::intValue).sum();
    }

    public Map<Long, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }
}
