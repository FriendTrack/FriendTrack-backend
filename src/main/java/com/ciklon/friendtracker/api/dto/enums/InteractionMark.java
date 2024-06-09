package com.ciklon.friendtracker.api.dto.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InteractionMark {
    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    private final String mark;
}
