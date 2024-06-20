package com.ciklon.friendtracker.api.dto.rating;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserAnswerForCalculationDto(

        @Schema(description = "Идентификатор ответа", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID contactId,

        @Schema(description = "Тип поля, на который будут начислен рейтинг", example = "COMMUNICATION")
        FieldType fieldType,

        @Schema(description = "Флаг, указывающий на то, является ли ответ положительным")
        boolean isPositive
) {
}
