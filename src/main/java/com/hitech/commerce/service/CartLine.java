package com.hitech.commerce.service;

import java.math.BigDecimal;

import com.hitech.commerce.domain.Product;

public record CartLine(Product product, int quantity, BigDecimal lineTotal) {
}
