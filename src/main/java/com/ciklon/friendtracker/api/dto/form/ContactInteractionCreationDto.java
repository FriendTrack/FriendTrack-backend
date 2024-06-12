package com.ciklon.friendtracker.api.dto.form;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ContactInteractionCreationDto(

        @Schema(description = "Идентификатор контакта")
        UUID contactId,

        @Schema(description = "Уровень уважения после взаимодействия")
        Integer respect,

        @Schema(description = "Уровень времени после взаимодействия")
        Integer time,

        @Schema(description = "Уровень доверия после взаимодействия")
        Integer trust,

        @Schema(description = "Уровень проявленности эмоций после взаимодействия")
        Integer empathy,

        @Schema(description = "Уровень успешности коммуникации после взаимодействия")
        Integer communication
) {
}
