package com.ciklon.friendtracker.api.dto.recommendation;

import com.ciklon.friendtracker.api.dto.enums.FieldType;

import java.util.UUID;

public record RecommendationDto(
        UUID id,
        FieldType fieldType,
        String title,
        String description
) {
}
