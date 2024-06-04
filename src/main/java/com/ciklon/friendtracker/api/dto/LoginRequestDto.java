package com.ciklon.friendtracker.api.dto;

import com.ciklon.friendtracker.core.constant.DefaultMessages;
import com.ciklon.friendtracker.core.constant.RegularExpressions;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record LoginRequestDto(
        @NotNull
        @Size(min = 3, max = 30, message = DefaultMessages.LOGIN_INVALID)
        String login,
        @Pattern(regexp = RegularExpressions.PASSWORD, message = DefaultMessages.PASSWORD_INVALID)
        String password) {
}
