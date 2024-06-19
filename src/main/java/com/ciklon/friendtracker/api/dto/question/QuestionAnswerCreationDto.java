package com.ciklon.friendtracker.api.dto.question;

import io.swagger.v3.oas.annotations.media.Schema;

public record QuestionAnswerCreationDto(

        @Schema(description = "Ответ на вопрос", example = "Часто")
        String answer,

        @Schema(description = "Положительный ли ответ", example = "true")
        boolean isPositive
) {
}
