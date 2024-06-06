package com.ciklon.friendtracker.core.service.integration;

import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserIntegrationService {

    private final UserRepository userRepository;

    public User getUserById(UUID creatorId) {
        return userRepository.findById(creatorId)
                .orElseThrow(() -> new CustomException(ExceptionType.BAD_REQUEST, "User not found"));
    }
}
