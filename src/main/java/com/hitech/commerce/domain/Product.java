package com.hitech.commerce.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 120)
    private String slug;

    @Column(nullable = false, length = 160)
    private String name;

    @Column(nullable = false, length = 1000)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "discount_percentage", nullable = false)
    private Integer discountPercentage = 0;

    @Column(nullable = false)
    private Integer stock;

    @Column(name = "image_path", nullable = false, length = 255)
    private String imagePath;

    @Column(name = "detail_image_path", nullable = false, length = 255)
    private String detailImagePath;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    protected Product() {
    }

    public Product(String slug, String name, String description, BigDecimal price, Integer discountPercentage,
            Integer stock, String imagePath, String detailImagePath, Category category) {
        this.slug = slug;
        this.name = name;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.stock = stock;
        this.imagePath = imagePath;
        this.detailImagePath = detailImagePath;
        this.category = category;
    }

    public Long getId() {
        return id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getSalePrice() {
        if (discountPercentage == null || discountPercentage <= 0) {
            return price;
        }
        BigDecimal multiplier = BigDecimal.valueOf(100L - discountPercentage)
                .divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP);
        return price.multiply(multiplier).setScale(2, RoundingMode.HALF_UP);
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(Integer discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getDetailImagePath() {
        return detailImagePath;
    }

    public void setDetailImagePath(String detailImagePath) {
        this.detailImagePath = detailImagePath;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
