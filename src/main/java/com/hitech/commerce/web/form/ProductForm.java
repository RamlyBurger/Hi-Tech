package com.hitech.commerce.web.form;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.hitech.commerce.domain.Product;

public class ProductForm {

    private Long id;

    @NotBlank
    @Size(max = 120)
    private String slug;

    @NotBlank
    @Size(max = 160)
    private String name;

    @NotBlank
    @Size(max = 1000)
    private String description;

    @NotNull
    @DecimalMin("0.00")
    private BigDecimal price;

    @NotNull
    @Min(0)
    @Max(95)
    private Integer discountPercentage = 0;

    @NotNull
    @Min(0)
    private Integer stock = 0;

    @NotBlank
    @Size(max = 255)
    private String imagePath;

    @NotBlank
    @Size(max = 255)
    private String detailImagePath;

    @NotNull
    private Long categoryId;

    private boolean active = true;

    public static ProductForm from(Product product) {
        ProductForm form = new ProductForm();
        form.setId(product.getId());
        form.setSlug(product.getSlug());
        form.setName(product.getName());
        form.setDescription(product.getDescription());
        form.setPrice(product.getPrice());
        form.setDiscountPercentage(product.getDiscountPercentage());
        form.setStock(product.getStock());
        form.setImagePath(product.getImagePath());
        form.setDetailImagePath(product.getDetailImagePath());
        form.setCategoryId(product.getCategory().getId());
        form.setActive(product.isActive());
        return form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
