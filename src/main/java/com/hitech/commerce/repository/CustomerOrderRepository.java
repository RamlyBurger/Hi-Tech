package com.hitech.commerce.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.commerce.domain.CustomerOrder;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

    List<CustomerOrder> findByUserAccountUsernameOrderByCreatedAtDesc(String username);

    Optional<CustomerOrder> findByIdAndUserAccountUsername(Long id, String username);
}
