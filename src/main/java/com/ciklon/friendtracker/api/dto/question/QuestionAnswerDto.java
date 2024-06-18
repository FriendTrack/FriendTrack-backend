package com.ciklon.friendtracker.api.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record QuestionAnswerDto(

        @Schema(description = "Идентификатор вопроса", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Ответ на вопрос", example = "Часто")
        String answer
) {
}
