package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerForCalculationDto;
import com.ciklon.friendtracker.core.entity.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, UUID> {
    Page<UserAnswer> findAllByUserId(UUID userId, Pageable pageable);

    Page<UserAnswer> findAllByUserIdAndContactId(UUID contactId, UUID userId, PageRequest pageable);

    @Query("""
        select new com.ciklon.friendtracker.api.dto.rating.UserAnswerForCalculationDto(
            ua.id,
            ua.contact.id,
            ua.question.fieldType,
            ua.questionAnswer.isPositive
        )
        from UserAnswer ua
        where ua.user.id = :userId
        and (ua.question.fieldType = :fieldType or :fieldType = 'ALL')
    """)
    List<UserAnswerForCalculationDto> findAllByUserIdAndDateBetweenAndFieldType(
            UUID userId, FieldType fieldType, Pageable pageable);

    @Query("""
        select new com.ciklon.friendtracker.api.dto.rating.UserAnswerForCalculationDto(
            ua.id,
            ua.contact.id,
            ua.question.fieldType,
            ua.questionAnswer.isPositive
        )
        from UserAnswer ua
        where ua.user.id = :userId
        and ua.contact.id = :contactId
        and (ua.question.fieldType = :fieldType or :fieldType = 'ALL')
    """)
    List<UserAnswerForCalculationDto> findAllByUserIdAndContactIdAndFieldType(
            UUID userId, UUID contactId, FieldType fieldType);

    @Query("""
        select new com.ciklon.friendtracker.api.dto.rating.UserAnswerForCalculationDto(
            ua.id,
            ua.contact.id,
            ua.question.fieldType,
            ua.questionAnswer.isPositive
        )
        from UserAnswer ua
        where ua.user.id = :userId
        and (ua.question.fieldType = :fieldType or :fieldType = 'ALL')
    """)
    List<UserAnswerForCalculationDto> findAllByUserIdAndFieldType(UUID userId, FieldType fieldType);

    @Query("""
        select new com.ciklon.friendtracker.api.dto.rating.UserAnswerForCalculationDto(
            ua.id,
            ua.contact.id,
            ua.question.fieldType,
            ua.questionAnswer.isPositive
        )
        from UserAnswer ua
        where ua.user.id = :userId
    """)
    List<UserAnswerForCalculationDto> findAllByUserId(UUID userId);
}
