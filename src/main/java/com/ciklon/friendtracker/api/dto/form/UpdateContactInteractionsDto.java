package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

public record UpdateContactInteractionsDto(
        @Schema(description = "Идентификатор формы")
        UUID formId,

        @Schema(description = "Количество взаимодействий с контактами")
        Integer interactionCount,

        @Schema(description = "Список взаимодействий с контактами")
        List<ContactInteractionCreationDto> contactInteractions
) {
}
