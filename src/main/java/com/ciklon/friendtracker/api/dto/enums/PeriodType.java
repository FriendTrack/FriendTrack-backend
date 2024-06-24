package com.ciklon.friendtracker.api.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PeriodType {
    WEEK("WEEK"),
    MONTH("MONTH"),
    HALF_YEAR("HALF_YEAR");

    private final String value;
}
