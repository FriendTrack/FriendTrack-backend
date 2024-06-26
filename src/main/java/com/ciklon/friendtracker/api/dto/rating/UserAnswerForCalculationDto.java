package com.ciklon.friendtracker.api.dto.rating;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserAnswerForCalculationDto(

        @Schema(description = "Идентификатор ответа", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID id,

        @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
        UUID contactId,

        @Schema(description = "Тип поля, на который будут начислен рейтинг", example = "COMMUNICATION")
        FieldType fieldType,

        @Schema(description = "Выбранное пользователем значение в качестве ответа",  example = "5")
        int value,

        @Schema(description = "Дата создания", example = "2021-10-10T10:00:00")
        LocalDateTime createdAt
) {
}
