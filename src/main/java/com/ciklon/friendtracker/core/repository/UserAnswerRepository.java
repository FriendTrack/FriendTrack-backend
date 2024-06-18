package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.UserAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserAnswerRepository extends JpaRepository<UserAnswer, UUID> {
    Page<UserAnswer> findAllByUserId(UUID userId, Pageable pageable);

    Page<UserAnswer> findAllByUserIdAndContactId(UUID contactId, UUID userId, PageRequest pageable);
}
