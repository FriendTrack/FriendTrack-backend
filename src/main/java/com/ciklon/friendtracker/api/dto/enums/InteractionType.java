package com.ciklon.friendtracker.api.dto.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum InteractionType {
    TEXT("TEXT"),
    CALL("CALL"),
    MEETING("MEETING");

    private final String type;
}
