package com.hitech.commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.commerce.domain.Promotion;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    List<Promotion> findByActiveTrueOrderByStartsOnAsc();
}
