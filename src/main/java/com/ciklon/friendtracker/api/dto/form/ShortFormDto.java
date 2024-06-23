package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Краткая информация о форме без списка взаимодействий с контактами")
public record ShortFormDto(
        @Schema(description = "Идентификатор формы")
        UUID id,

        @Schema(description = "Идентификатор пользователя")
        UUID userId,

        @Schema(description = "Дата взаимодействия")
        LocalDate date
) {
}
