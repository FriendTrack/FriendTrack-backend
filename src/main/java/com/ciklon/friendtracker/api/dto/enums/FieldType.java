package com.ciklon.friendtracker.api.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FieldType {

    ALL("ALL"),
    COMMUNICATION("COMMUNICATION"),
    EMPATHY("EMPATHY"),
    RESPECT("RESPECT"),
    TIME("TIME"),
    TRUST("TRUST");

    private final String value;
}
