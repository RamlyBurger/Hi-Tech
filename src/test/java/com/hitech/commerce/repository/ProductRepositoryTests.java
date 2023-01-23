package com.hitech.commerce.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class ProductRepositoryTests {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void seedDataContainsOriginalCatalog() {
        assertThat(productRepository.findByActiveTrueOrderByNameAsc()).hasSize(16);
        assertThat(productRepository.findBySlug("dell-xps-8950")).isPresent();
    }

    @Test
    void filtersByCategoryAndSearchTerm() {
        assertThat(productRepository.findByCategory_CodeAndActiveTrueOrderByNameAsc("laptop"))
                .extracting("name")
                .contains("ROG STRIX G15", "TUF Gaming F15", "Apple Macbook1", "HP Victus 16");

        assertThat(productRepository.searchActive("razer"))
                .extracting("slug")
                .contains("razer-basilisk-ultimate", "razer-pro-type-ultra");
    }
}
