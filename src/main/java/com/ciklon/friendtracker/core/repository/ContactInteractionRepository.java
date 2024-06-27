package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.form.ContactInteractionDto;
import com.ciklon.friendtracker.api.dto.form.ExtendedContactInteractionDto;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.embedabble.ContactInteractionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface ContactInteractionRepository extends JpaRepository<ContactInteraction, ContactInteractionId> {
    @Query("""
            select new com.ciklon.friendtracker.api.dto.form.ContactInteractionDto(
                ci.id.formId,
                ci.contact.id,
                ci.respect,
                ci.time,
                ci.trust,
                ci.empathy,
                ci.communication
            )
            from ContactInteraction ci
            join ci.form f on ci.id.formId = f.id
            where f.user.id = :userId
            and f.date >= :fromDate
            and f.date <= :toDate
            order by f.date desc
    """
    )
    List<ContactInteractionDto> findAllByUserIdAndDateBetween(UUID userId, @Param("fromDate") LocalDate fromDate,
                                                              @Param("toDate") LocalDate toDate);
    @Query("""
            select new com.ciklon.friendtracker.api.dto.form.ExtendedContactInteractionDto(
                ci.id.formId,
                ci.contact.id,
                ci.respect,
                ci.time,
                ci.trust,
                ci.empathy,
                ci.communication,
                f.date
            )
            from ContactInteraction ci
            join ci.form f on ci.id.formId = f.id
            where f.user.id = :userId
            order by f.date desc
    """
    )
    List<ExtendedContactInteractionDto> findAllByUserId(UUID userId);

    @Query("""
            select new com.ciklon.friendtracker.api.dto.form.ContactInteractionDto(
                ci.id.formId,
                ci.contact.id,
                ci.respect,
                ci.time,
                ci.trust,
                ci.empathy,
                ci.communication
            )
            from ContactInteraction ci
            join ci.form f on ci.id.formId = f.id
            where f.user.id = :userId
            and ci.contact.id = :contactId
            and f.date >= :fromDate
            and f.date <= :toDate
            order by f.date desc
    """
    )
    List<ContactInteractionDto> findAllByUserIdAndContactId(UUID userId, UUID contactId,
                                                            LocalDate fromDate, LocalDate toDate);

    @Query("""
            select new com.ciklon.friendtracker.api.dto.form.ExtendedContactInteractionDto(
                ci.id.formId,
                ci.contact.id,
                ci.respect,
                ci.time,
                ci.trust,
                ci.empathy,
                ci.communication,
                f.date
            )
            from ContactInteraction ci
            join ci.form f on ci.id.formId = f.id
            where f.user.id = :userId
            and ci.contact.id = :contactId
            order by f.date asc
    """
    )
    List<ExtendedContactInteractionDto> findAllByUserIdAndContactId(UUID userId, UUID contactId);
}
