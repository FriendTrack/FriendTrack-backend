package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.Form;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FormRepository extends JpaRepository<Form, UUID> {
}
