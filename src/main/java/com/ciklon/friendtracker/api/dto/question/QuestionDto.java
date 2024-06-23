package com.ciklon.friendtracker.api.dto.question;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record QuestionDto(

        @Schema(description = "Идентификатор вопроса", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Вопрос", example = "Как часто вы общаетесь с друзьями?")
        String question,

        @Schema(description = "Тип поля, на который будут начислен рейтинг", example = "COMMUNICATION")
        FieldType fieldType
) {
}
