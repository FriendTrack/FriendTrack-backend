package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {
    List<Form> findAllByUserId(UUID userId, Pageable pageable);
}
