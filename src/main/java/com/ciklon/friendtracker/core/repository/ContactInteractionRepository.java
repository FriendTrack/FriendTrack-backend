package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContactInteractionRepository extends JpaRepository<ContactInteraction, ContactInteractionId> {
    @Query("""
            select new com.ciklon.friendtracker.api.dto.form.ContactInteractionDto(
                ci.id.contactId,
                ci.id.formId,
                ci.communication,
                ci.empathy,
                ci.trust,
                ci.time,
                ci.respect
            )
            from ContactInteraction ci
            join ci.form f on ci.id.formId = f.id
            where f.user.id = :userId
            and f.date between :fromDate and :toDate
            order by f.date desc
    """
    )
    List<ContactInteraction> findAllByUserIdAndDateBetweenAndFieldType(UUID userId, LocalDate fromDate,
                                                                       LocalDate toDate, Pageable pageable);
}
