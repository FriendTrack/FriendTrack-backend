package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.Question;
import com.ciklon.friendtracker.core.entity.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, UUID> {
    List<QuestionAnswer> findByQuestion(Question question);

    List<QuestionAnswer> deleteAllByQuestionId(UUID questionId);
}
