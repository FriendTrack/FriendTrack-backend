package com.ciklon.friendtracker.api.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class QuestionAnswersRatingDto {

    @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID contactId;

    @Schema(description = "Количество ответов на вопросы", example = "3")
    private int questionAnswerCount = 0;

    @Schema(description = "Сумма оценок времяпровождения", example = "15")
    private int timeRatingSum = 0;

    @Schema(description = "Сумма оценок уважения", example = "15")
    private int respectRatingSum = 0;

    @Schema(description = "Сумма оценок доверия", example = "15")
    private int trustRatingSum = 0;

    @Schema(description = "Сумма оценок эмпатии", example = "15")
    private int empathyRatingSum = 0;

    @Schema(description = "Сумма оценок коммуникации", example = "15")
    private int communicationRatingSum = 0;

    public QuestionAnswersRatingDto(UUID contactId) {
        this.contactId = contactId;
    }
}
