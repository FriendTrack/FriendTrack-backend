package com.ciklon.friendtracker.core.constant;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class RatingProps {

    @Value("${rating.interaction-weight:3.0}")
    private double interactionWeight;

    @Value("${rating.answer-weight:1.0}")
    private double answerWeight;

    @Value("${rating.max-rating}")
    private double maxRating;

    @Value("${rating.min-rating}")
    private double minRating;
}
