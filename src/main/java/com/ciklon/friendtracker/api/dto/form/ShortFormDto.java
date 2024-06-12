package com.ciklon.friendtracker.api.dto.form;

import com.ciklon.friendtracker.api.dto.enums.MoodType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Краткая информация о форме без списка взаимодействий с контактами")
public record ShortFormDto(
        @Schema(description = "Идентификатор формы")
        UUID id,

        @Schema(description = "Идентификатор пользователя")
        UUID userId,

        @Schema(description = "Настроение в течение дня")
        MoodType mood,

        @Schema(description = "Дата взаимодействия")
        LocalDate date
) {
}
