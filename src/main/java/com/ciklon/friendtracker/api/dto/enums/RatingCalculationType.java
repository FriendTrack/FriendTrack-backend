package com.ciklon.friendtracker.api.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RatingCalculationType {
    FORMS("FORMS"),
    QUESTIONS("QUESTIONS"),
    ALL("ALL");

    private final String value;
}
