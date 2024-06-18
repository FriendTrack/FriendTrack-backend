package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Page<Question> findAll(Pageable pageable);
}
