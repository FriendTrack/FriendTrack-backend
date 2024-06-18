package com.ciklon.friendtracker.api.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserAnswerCreationDto(

        @Schema(description = "Идентификатор вопроса", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID questionId,

        @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID contactId,

        @Schema(description = "Ответ на вопрос", example = "Часто")
        String answer
) {
}
