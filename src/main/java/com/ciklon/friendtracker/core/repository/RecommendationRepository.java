package com.ciklon.friendtracker.core.repository;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.recommendation.RecommendationDto;
import com.ciklon.friendtracker.core.entity.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RecommendationRepository extends JpaRepository<Recommendation, UUID> {
    RecommendationDto findFirstByFieldType(FieldType fieldType);
}
