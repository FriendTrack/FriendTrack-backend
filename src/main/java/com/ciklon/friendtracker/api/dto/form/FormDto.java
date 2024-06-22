package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FormDto(
        @Schema(description = "Идентификатор формы")
        UUID id,

        @Schema(description = "Идентификатор пользователя")
        UUID userId,

        @Schema(description = "Дата взаимодействия")
        LocalDate date,

        @Schema(description = "Количество взаимодействий с контактами")
        int interactionCount,

        @Schema(description = "Список взаимодействий с контактами")
        List<ContactInteractionDto> contactInteractions
) {
}
