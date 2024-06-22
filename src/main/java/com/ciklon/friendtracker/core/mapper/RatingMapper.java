package com.ciklon.friendtracker.core.mapper;

import com.ciklon.friendtracker.api.dto.enums.RatingCalculationType;
import com.ciklon.friendtracker.api.dto.rating.ContactInteractionsRatingDto;
import com.ciklon.friendtracker.api.dto.rating.QuestionAnswersRatingDto;
import com.ciklon.friendtracker.api.dto.rating.RatingDto;
import com.ciklon.friendtracker.core.constant.RatingProps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RatingMapper {

    private final RatingProps ratingProps;

    public RatingDto mapToRatingDto(QuestionAnswersRatingDto dto) {
        int questionAnswerCount = dto.getQuestionAnswerCount();
        if (questionAnswerCount == 0) {
            return new RatingDto(dto.getContactId(), RatingCalculationType.QUESTIONS, 0, 0, 0, 0, 0, 0, 0);
        }
        return new RatingDto(dto.getContactId(),
                             RatingCalculationType.QUESTIONS,
                             0,
                             questionAnswerCount,
                             (double) dto.getRespectRatingSum() / questionAnswerCount,
                             (double) dto.getTrustRatingSum() / questionAnswerCount,
                             (double) dto.getEmpathyRatingSum() / questionAnswerCount,
                             (double) dto.getQuestionAnswerCount() / questionAnswerCount,
                             (double) dto.getTimeRatingSum() / questionAnswerCount
        );
    }

    public RatingDto mapToRatingDto(ContactInteractionsRatingDto dto) {
        int interactionCount = dto.getInteractionCount();
        if (interactionCount == 0) {
            return new RatingDto(dto.getContactId(), RatingCalculationType.FORMS, 0, 0, 0, 0, 0, 0, 0);
        }
        return new RatingDto(dto.getContactId(),
                             RatingCalculationType.FORMS,
                             interactionCount,
                             0,
                             (double) dto.getRespectRatingSum() / interactionCount,
                             (double) dto.getTrustRatingSum() / interactionCount,
                             (double) dto.getEmpathyRatingSum() / interactionCount,
                             (double) dto.getCommunicationRatingSum() / interactionCount,
                             (double) dto.getTimeRatingSum() / interactionCount
        );
    }

    public RatingDto mapToRatingDtoWithWeights(RatingDto first, RatingDto second) {
        return new RatingDto(first.getContactId(),
                             RatingCalculationType.ALL,
                             first.getInteractionCount() + second.getInteractionCount(),
                             first.getQuestionAnswerCount() + second.getQuestionAnswerCount(),
                             calculateWeightedValue(first.getTimeRating(),
                                                    second.getTimeRating(),
                                                    first.getInteractionCount(),
                                                    second.getQuestionAnswerCount()
                             ),
                             calculateWeightedValue(first.getCommunicationRating(),
                                                    second.getCommunicationRating(),
                                                    first.getInteractionCount(),
                                                    second.getQuestionAnswerCount()
                             ),
                             calculateWeightedValue(first.getRespectRating(),
                                                    second.getRespectRating(),
                                                    first.getInteractionCount(),
                                                    second.getQuestionAnswerCount()
                             ),
                             calculateWeightedValue(first.getTrustRating(),
                                                    second.getTrustRating(),
                                                    first.getInteractionCount(),
                                                    second.getQuestionAnswerCount()
                             ),
                             calculateWeightedValue(first.getEmpathyRating(),
                                                    second.getEmpathyRating(),
                                                    first.getInteractionCount(),
                                                    second.getQuestionAnswerCount()
                             )
        );
    }

    private double calculateWeightedValue(
            double interactionRating, double answerRating, int interactionCount, int answerCount
    ) {
        if (interactionCount == 0 && answerCount == 0) {
            return 0;
        }
        double weightedValue = (interactionRating * ratingProps.getInteractionWeight() +
                answerRating * ratingProps.getAnswerWeight()) / (interactionCount + answerCount);
        return Math.max(ratingProps.getMinRating(), Math.min(ratingProps.getMaxRating(), weightedValue));
    }
}

