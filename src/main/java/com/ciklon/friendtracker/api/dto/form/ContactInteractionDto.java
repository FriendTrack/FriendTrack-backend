package com.ciklon.friendtracker.api.dto.form;

import com.ciklon.friendtracker.api.dto.enums.InteractionMark;
import com.ciklon.friendtracker.api.dto.enums.InteractionType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ContactInteractionDto(
        @Schema(description = "Идентификатор взаимодействия")
        UUID interactionId,

        @Schema(description = "Идентификатор контакта")
        UUID contactId,

        @Schema(description = "Тип взаимодействия")
        InteractionType interactionType,

        @Schema(description = "Уровень радости после взаимодействия")
        Integer happiness,

        @Schema(description = "Уровень печали после взаимодействия")
        Integer sadness,

        @Schema(description = "Уровень страха после взаимодействия")
        Integer fear,

        @Schema(description = "Уровень отвращения после взаимодействия")
        Integer disgust,

        @Schema(description = "Уровень гнева после взаимодействия")
        Integer anger,

        @Schema(description = "Уровень удивления после взаимодействия")
        Integer surprise,

        @Schema(description = "Общая оценка после взаимодействия")
        InteractionMark interactionMark
){
}
