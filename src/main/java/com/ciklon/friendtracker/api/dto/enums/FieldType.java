package com.ciklon.friendtracker.api.dto.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum FieldType {
    TIME("TIME"),
    EMPATHY("EMPATHY"),
    TRUST("TRUST"),
    COMMUNICATION("COMMUNICATION"),
    RESPECT("RESPECT"),
    ALL("ALL");

    private final String value;
}
