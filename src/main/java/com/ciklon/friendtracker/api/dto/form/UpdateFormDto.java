package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UpdateFormDto(

        @Schema(description = "Дата взаимодействия")
        LocalDate date
) {
}
