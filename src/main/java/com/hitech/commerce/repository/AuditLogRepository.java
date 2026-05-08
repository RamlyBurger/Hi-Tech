package com.hitech.commerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hitech.commerce.domain.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);

    boolean existsByActionAndTargetTypeAndTargetId(String action, String targetType, Long targetId);
}
