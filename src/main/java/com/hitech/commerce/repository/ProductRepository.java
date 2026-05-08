package com.hitech.commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hitech.commerce.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySlugAndActiveTrue(String slug);

    Optional<Product> findByIdAndActiveTrue(Long id);

    List<Product> findByActiveTrueOrderByNameAsc();

    List<Product> findByCategory_CodeAndActiveTrueOrderByNameAsc(String categoryCode);

    @Query("select p from Product p where p.active = true and "
            + "(lower(p.name) like lower(concat('%', :term, '%')) "
            + "or lower(p.description) like lower(concat('%', :term, '%')) "
            + "or lower(p.category.name) like lower(concat('%', :term, '%'))) "
            + "order by p.name asc")
    List<Product> searchActive(@Param("term") String term);
}
