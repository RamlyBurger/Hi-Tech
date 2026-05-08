package com.hitech.commerce.service;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.domain.Category;
import com.hitech.commerce.domain.Product;
import com.hitech.commerce.domain.Promotion;
import com.hitech.commerce.domain.StoreEvent;
import com.hitech.commerce.repository.CategoryRepository;
import com.hitech.commerce.repository.ProductRepository;
import com.hitech.commerce.repository.PromotionRepository;
import com.hitech.commerce.repository.StoreEventRepository;
import com.hitech.commerce.web.form.EventForm;
import com.hitech.commerce.web.form.ProductForm;
import com.hitech.commerce.web.form.PromotionForm;

@Service
public class AdminService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final StoreEventRepository eventRepository;
    private final PromotionRepository promotionRepository;
    private final AuditService auditService;

    public AdminService(ProductRepository productRepository, CategoryRepository categoryRepository,
            StoreEventRepository eventRepository, PromotionRepository promotionRepository, AuditService auditService) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.eventRepository = eventRepository;
        this.promotionRepository = promotionRepository;
        this.auditService = auditService;
    }

    @Transactional(readOnly = true)
    public List<Product> products() {
        return productRepository.findAll(Sort.by("name"));
    }

    @Transactional(readOnly = true)
    public List<Category> categories() {
        return categoryRepository.findAll(Sort.by("name"));
    }

    @Transactional(readOnly = true)
    public Product product(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown product"));
    }

    @Transactional
    public Product saveProduct(ProductForm form) {
        Category category = categoryRepository.findById(form.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Unknown category"));
        Product product = form.getId() == null
                ? new Product(form.getSlug(), form.getName(), form.getDescription(), form.getPrice(),
                        form.getDiscountPercentage(), form.getStock(), form.getImagePath(), form.getDetailImagePath(),
                        category)
                : product(form.getId());
        product.setSlug(form.getSlug());
        product.setName(form.getName());
        product.setDescription(form.getDescription());
        product.setPrice(form.getPrice());
        product.setDiscountPercentage(form.getDiscountPercentage());
        product.setStock(form.getStock());
        product.setImagePath(form.getImagePath());
        product.setDetailImagePath(form.getDetailImagePath());
        product.setCategory(category);
        product.setActive(form.isActive());
        Product saved = productRepository.save(product);
        auditService.record("PRODUCT_SAVED", "Product", saved.getId(), saved.getSlug());
        return saved;
    }

    @Transactional
    public void deactivateProduct(Long id) {
        Product product = product(id);
        product.setActive(false);
        auditService.record("PRODUCT_DEACTIVATED", "Product", product.getId(), product.getSlug());
    }

    @Transactional(readOnly = true)
    public List<StoreEvent> events() {
        return eventRepository.findAll(Sort.by("eventDate", "eventTime"));
    }

    @Transactional(readOnly = true)
    public StoreEvent event(Long id) {
        return eventRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown event"));
    }

    @Transactional
    public StoreEvent saveEvent(EventForm form) {
        StoreEvent event = form.getId() == null
                ? new StoreEvent(form.getName(), form.getVenue(), form.getEventDate(), form.getEventTime())
                : event(form.getId());
        event.setName(form.getName());
        event.setVenue(form.getVenue());
        event.setEventDate(form.getEventDate());
        event.setEventTime(form.getEventTime());
        event.setActive(form.isActive());
        StoreEvent saved = eventRepository.save(event);
        auditService.record("EVENT_SAVED", "StoreEvent", saved.getId(), saved.getName());
        return saved;
    }

    @Transactional
    public void deactivateEvent(Long id) {
        StoreEvent event = event(id);
        event.setActive(false);
        auditService.record("EVENT_DEACTIVATED", "StoreEvent", event.getId(), event.getName());
    }

    @Transactional(readOnly = true)
    public List<Promotion> promotions() {
        return promotionRepository.findAll(Sort.by("startsOn"));
    }

    @Transactional(readOnly = true)
    public Promotion promotion(Long id) {
        return promotionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Unknown promotion"));
    }

    @Transactional
    public Promotion savePromotion(PromotionForm form) {
        Promotion promotion = form.getId() == null
                ? new Promotion(form.getTitle(), form.getDescription(), form.getProductName(),
                        form.getHighlightColor(), form.getStartsOn(), form.getEndsOn())
                : promotion(form.getId());
        promotion.setTitle(form.getTitle());
        promotion.setDescription(form.getDescription());
        promotion.setProductName(form.getProductName());
        promotion.setHighlightColor(form.getHighlightColor());
        promotion.setStartsOn(form.getStartsOn());
        promotion.setEndsOn(form.getEndsOn());
        promotion.setActive(form.isActive());
        Promotion saved = promotionRepository.save(promotion);
        auditService.record("PROMOTION_SAVED", "Promotion", saved.getId(), saved.getTitle());
        return saved;
    }

    @Transactional
    public void deactivatePromotion(Long id) {
        Promotion promotion = promotion(id);
        promotion.setActive(false);
        auditService.record("PROMOTION_DEACTIVATED", "Promotion", promotion.getId(), promotion.getTitle());
    }
}
