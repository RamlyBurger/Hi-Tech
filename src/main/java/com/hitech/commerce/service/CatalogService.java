package com.hitech.commerce.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.domain.Category;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.repository.CategoryRepository;
import com.hitech.commerce.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public CatalogService(CategoryRepository categoryRepository, ProductRepository productRepository) {
        this.categoryRepository = categoryRepository;
        this.productRepository = productRepository;
    }

    public List<Category> listCategories() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    public List<Product> listProducts(String categoryCode, String query) {
        if (query != null && !query.isBlank()) {
            return productRepository.searchActive(query.trim());
        }
        if (categoryCode != null && !categoryCode.isBlank()) {
            return productRepository.findByCategory_CodeAndActiveTrueOrderByNameAsc(categoryCode);
        }
        return productRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Product> featuredProducts() {
        return productRepository.findByActiveTrueOrderByNameAsc().stream().limit(4).toList();
    }

    public Category getCategory(String code) {
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalArgumentException("Unknown category: " + code));
    }

    public Product getProduct(String slug) {
        return productRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("Unknown product: " + slug));
    }
}
