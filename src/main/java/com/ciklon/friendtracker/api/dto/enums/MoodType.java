package com.ciklon.friendtracker.api.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MoodType {
    HAPPY("HAPPY"),
    SAD("SAD"),
    NEUTRAL("NEUTRAL");

    private final String mood;
}
