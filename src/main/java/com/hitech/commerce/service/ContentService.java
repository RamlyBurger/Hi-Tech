package com.hitech.commerce.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hitech.commerce.domain.Promotion;
import com.hitech.commerce.domain.StoreEvent;
import com.hitech.commerce.repository.PromotionRepository;
import com.hitech.commerce.repository.StoreEventRepository;

@Service
@Transactional(readOnly = true)
public class ContentService {

    private final StoreEventRepository eventRepository;
    private final PromotionRepository promotionRepository;

    public ContentService(StoreEventRepository eventRepository, PromotionRepository promotionRepository) {
        this.eventRepository = eventRepository;
        this.promotionRepository = promotionRepository;
    }

    public List<StoreEvent> listEvents() {
        return eventRepository.findByActiveTrueOrderByEventDateAscEventTimeAsc();
    }

    public List<Promotion> listPromotions() {
        return promotionRepository.findByActiveTrueOrderByStartsOnAsc();
    }
}
