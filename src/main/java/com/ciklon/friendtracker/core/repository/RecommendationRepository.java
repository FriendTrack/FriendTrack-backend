package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.recommendation.RecommendationDto;
import com.ciklon.friendtracker.core.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {

    // find random recommendation by field type
    @Query("""
            SELECT new com.ciklon.friendtracker.api.dto.recommendation.RecommendationDto(
            r.id,
            r.fieldType,
            r.title,
            r.description
            )
            FROM Recommendation r
            WHERE r.fieldType = :fieldType
            ORDER BY RANDOM()
            LIMIT 1
            """)
    RecommendationDto findByFieldType(FieldType fieldType);
}
