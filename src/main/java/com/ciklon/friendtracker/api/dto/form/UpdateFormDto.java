package com.ciklon.friendtracker.api.dto.form;

import com.ciklon.friendtracker.api.dto.enums.MoodType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record UpdateFormDto(
        @Schema(description = "Настроение в течение дня")
        MoodType mood,

        @Schema(description = "Дата взаимодействия")
        LocalDate date
) {
}
