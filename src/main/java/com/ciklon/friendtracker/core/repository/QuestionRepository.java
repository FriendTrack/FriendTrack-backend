package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.core.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionRepository extends JpaRepository<Question, UUID> {
    Page<Question> findAll(Pageable pageable);

    @Query(nativeQuery = true, value = """
                WITH grouped_questions AS (
                                   SELECT
                                       q.*,
                                       ROW_NUMBER() OVER (PARTITION BY q.field_type ORDER BY RANDOM()) AS rn
                                   FROM questions q
                                   WHERE q.field_type IN ('TIME', 'COMMUNICATION', 'EMPATHY', 'TRUST', 'RESPECT')
                               )
                               select gq.id,
                                      gq.question,
                                      gq.field_type
                               FROM grouped_questions gq
                               WHERE gq.rn = 1
            """
    )
    List<Question> getByDifferentFieldTypes();
}
