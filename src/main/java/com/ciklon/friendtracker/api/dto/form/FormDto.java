package com.ciklon.friendtracker.api.dto.form;

import com.ciklon.friendtracker.api.dto.enums.MoodType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record FormDto(
        @Schema(description = "Идентификатор формы")
        UUID id,

        @Schema(description = "Идентификатор пользователя")
        UUID userId,

        @Schema(description = "Настроение в течение дня")
        MoodType mood,

        @Schema(description = "Дата взаимодействия")
        LocalDate date,

        @Schema(description = "Количество взаимодействий с контактами")
        Integer interactionCount,

        @Schema(description = "Список взаимодействий с контактами")
        List<ContactInteractionCreationDto> contactInteractions
) {
}
