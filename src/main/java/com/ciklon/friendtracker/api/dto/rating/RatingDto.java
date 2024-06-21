package com.ciklon.friendtracker.api.dto.rating;


import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {

    @Schema(description = "Идентификатор контакта", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID contactId;

    @Schema(description = "Количество взаимодействий", example = "5")
    private int interactionCount;

    @Schema(description = "Количество ответов на вопросы", example = "3")
    private int questionAnswerCount;

    @Schema(description = "Рейтинг времяпровождения", example = "0.5")
    private double timeRating;

    @Schema(description = "Рейтинг коммуникации", example = "0.5")
    private double communicationRating;

    @Schema(description = "Рейтинг уважения", example = "0.5")
    private double respectRating;

    @Schema(description = "Рейтинг доверия", example = "0.5")
    private double trustRating;

    @Schema(description = "Рейтинг эмпатии", example = "0.5")
    private double empathyRating;

    @Schema(description = "Тип расчета рейтинга", example = "ALL")
    private RatingCalculationType calculationType;

    public RatingDto(UUID contactId, RatingCalculationType ratingCalculationType) {
        this.contactId = contactId;
        this.calculationType = ratingCalculationType;

        this.interactionCount = 0;
        this.questionAnswerCount = 0;
        this.timeRating = 0;
        this.communicationRating = 0;
        this.respectRating = 0;
        this.trustRating = 0;
    }

    public RatingDto(
            UUID contactId,
            RatingCalculationType ratingCalculationType,
            int interactionCount,
            int questionAnswerCount,
            double timeRating,
            double communicationRating,
            double respectRating,
            double trustRating,
            double empathyRating
    ) {
        this.contactId = contactId;
        this.calculationType = ratingCalculationType;
        this.interactionCount = interactionCount;
        this.questionAnswerCount = questionAnswerCount;
        this.timeRating = timeRating;
        this.communicationRating = communicationRating;
        this.respectRating = respectRating;
        this.trustRating = trustRating;
        this.empathyRating = empathyRating;
    }
}
