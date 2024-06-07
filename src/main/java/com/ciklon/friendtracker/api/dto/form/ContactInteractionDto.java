package com.ciklon.friendtracker.api.dto.form;

import com.ciklon.friendtracker.api.dto.enums.EmotionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ContactInteractionDto(
        @Schema(description = "Идентификатор взаимодействия")
        UUID interactionId,

        @Schema(description = "Идентификатор контакта")
        UUID contactId,

        @Schema(description = "Тип эмоции после взаимодействия")
        EmotionType emotion
){
}
