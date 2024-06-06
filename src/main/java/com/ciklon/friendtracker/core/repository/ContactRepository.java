package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.contact.ContactDto;
import com.ciklon.friendtracker.core.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Page<ContactDto> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByNameAndUserId(String name, UUID userId);
}
