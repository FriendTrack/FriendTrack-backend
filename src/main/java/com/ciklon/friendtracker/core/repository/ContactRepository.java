package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.contact.ContactDto;
import com.ciklon.friendtracker.core.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContactRepository extends JpaRepository<Contact, UUID> {
    Page<ContactDto> findAllByUserId(UUID userId, Pageable pageable);

    boolean existsByNameAndUserId(String name, UUID userId);

    boolean existsByNameInAndUserId(List<String> list, UUID creatorId);

    @Query("""
            select c.id
            from Contact c
            where c.user.id = :userId
    """
    )
    List<UUID> findContactIdsByUserId(UUID userId);


    @Query("""
            select c.id
            from Contact c
            where c.user.id = :userId
            and c.createdAt <= :toDate
    """
    )
    List<UUID> findContactIdsByUserIdAndToDate(UUID userId, LocalDate toDate);


    boolean existsContactByUserIdAndId(UUID userId, UUID id);
}
