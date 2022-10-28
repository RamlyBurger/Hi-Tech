package com.hitech.commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.commerce.domain.StoreEvent;

public interface StoreEventRepository extends JpaRepository<StoreEvent, Long> {

    List<StoreEvent> findByActiveTrueOrderByEventDateAscEventTimeAsc();
}
