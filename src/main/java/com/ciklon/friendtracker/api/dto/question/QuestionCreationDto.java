package com.ciklon.friendtracker.api.dto.question;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public record QuestionCreationDto(

        @Schema(description = "Вопрос", example = "Как часто вы общаетесь с друзьями?")
        String question,

        @Schema(description = "Тип поля, на который будут начислен рейтинг", example = "COMMUNICATION")
        FieldType fieldType
) {
}
