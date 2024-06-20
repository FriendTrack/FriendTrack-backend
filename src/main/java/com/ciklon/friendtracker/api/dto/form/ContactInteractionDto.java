package com.ciklon.friendtracker.api.dto.form;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.UUID;

public record ContactInteractionDto(
        @Schema(description = "Идентификатор взаимодействия")
        UUID interactionId,

        @Schema(description = "Идентификатор контакта")
        UUID contactId,

        @Schema(description = "Уровень уважения после взаимодействия")
        int respect,

        @Schema(description = "Уровень времени после взаимодействия")
        int time,

        @Schema(description = "Уровень доверия после взаимодействия")
        int trust,

        @Schema(description = "Уровень проявленности эмоций после взаимодействия")
        int empathy,

        @Schema(description = "Уровень успешности коммуникации после взаимодействия")
        int communication
){
}
