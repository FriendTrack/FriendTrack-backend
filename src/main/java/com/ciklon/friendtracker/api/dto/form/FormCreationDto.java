package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.List;


public record FormCreationDto(

        @Schema(description = "Дата взаимодействия")
        LocalDate date,

        @Schema(description = "Количество взаимодействий с контактами")
        Integer interactionCount,

        @Schema(description = "Список взаимодействий с контактами")
        List<ContactInteractionCreationDto> contactInteractions
) {
}
