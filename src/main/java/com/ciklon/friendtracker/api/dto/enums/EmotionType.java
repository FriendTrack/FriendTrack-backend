package com.ciklon.friendtracker.api.dto.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum EmotionType {
    LIKE("LIKE"),
    DISLIKE("DISLIKE");

    private final String mark;
}
