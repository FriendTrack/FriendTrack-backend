package com.ciklon.friendtracker.api.dto.question;

import com.ciklon.friendtracker.api.dto.enums.FieldType;
import io.swagger.v3.oas.annotations.media.Schema;

public record UpdateQuestionDto(

        @Schema(description = "Описание вопроса", example = "Как часто вы общаетесь с друзьями?")
        String question,

        @Schema(description = "Тип поля, на который будут начислен рейтинг", example = "COMMUNICATION")
        FieldType fieldType

) {
}
