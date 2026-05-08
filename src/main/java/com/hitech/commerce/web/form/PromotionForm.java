package com.hitech.commerce.web.form;

import java.time.LocalDate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.hitech.commerce.domain.Promotion;

public class PromotionForm {

    private Long id;

    @NotBlank
    @Size(max = 160)
    private String title;

    @NotBlank
    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 160)
    private String productName;

    @NotBlank
    @Size(max = 40)
    @Pattern(regexp = "red|purple|gold|green|blue|orange|darkolivegreen|black|#[0-9A-Fa-f]{6}", message = "Choose a supported highlight color")
    private String highlightColor = "blue";

    @NotNull
    private LocalDate startsOn;

    @NotNull
    private LocalDate endsOn;

    private boolean active = true;

    public static PromotionForm from(Promotion promotion) {
        PromotionForm form = new PromotionForm();
        form.setId(promotion.getId());
        form.setTitle(promotion.getTitle());
        form.setDescription(promotion.getDescription());
        form.setProductName(promotion.getProductName());
        form.setHighlightColor(promotion.getHighlightColor());
        form.setStartsOn(promotion.getStartsOn());
        form.setEndsOn(promotion.getEndsOn());
        form.setActive(promotion.isActive());
        return form;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getHighlightColor() {
        return highlightColor;
    }

    public void setHighlightColor(String highlightColor) {
        this.highlightColor = highlightColor;
    }

    public LocalDate getStartsOn() {
        return startsOn;
    }

    public void setStartsOn(LocalDate startsOn) {
        this.startsOn = startsOn;
    }

    public LocalDate getEndsOn() {
        return endsOn;
    }

    public void setEndsOn(LocalDate endsOn) {
        this.endsOn = endsOn;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
