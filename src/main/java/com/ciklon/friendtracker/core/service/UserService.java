package com.ciklon.friendtracker.core.service;


import com.ciklon.friendtracker.api.dto.*;
import com.ciklon.friendtracker.common.auth.util.JwtUtils;
import com.ciklon.friendtracker.common.exception.CustomException;
import com.ciklon.friendtracker.common.exception.ExceptionType;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.mapper.UserMapper;
import com.ciklon.friendtracker.core.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            BCryptPasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional
    public UserDto register(RegistrationRequestDto registrationRequestDto) {
        isLoginAndEmailAlreadyUsed(registrationRequestDto.login(), registrationRequestDto.email());
        User user = userMapper.map(registrationRequestDto, passwordEncoder.encode(registrationRequestDto.password()));
        User savedUser = userRepository.save(user);
        return userMapper.map(savedUser);
    }

    @Transactional
    public JwtAuthorityDto login(LoginRequestDto loginRequestDto) {
        isLoginExist(loginRequestDto.login());
        User user = getUserIfPasswordCorrect(loginRequestDto.login(), loginRequestDto.password());

        String accessToken = jwtUtils.generateAccessToken(user.getLogin(), user.getId());
        String refreshToken = jwtUtils.getOrGenerateRefreshToken(user.getLogin(), user.getId());
        jwtUtils.saveToken(user.getId().toString(), refreshToken);
        return new JwtAuthorityDto(user.getId(), accessToken, refreshToken);
    }

    @Transactional
    public void logout(LogoutRequestDto logoutRequestDto) {
        jwtUtils.deleteToken(logoutRequestDto.userId().toString());
    }

    public TokenDto getAccessToken(String refreshToken) {
        checkRefreshToken(refreshToken);
        String login = jwtUtils.getLogin(refreshToken);
        String userId = jwtUtils.getSubject(refreshToken);
        return new TokenDto(jwtUtils.generateAccessToken(login, UUID.fromString(userId)), null);
    }

    @Transactional
    public TokenDto refresh(String refreshToken) {
        checkRefreshToken(refreshToken);
        String login = jwtUtils.getLogin(refreshToken);
        String userId = jwtUtils.getSubject(refreshToken);
        jwtUtils.deleteToken(UUID.fromString(userId).toString());
        String newRefreshToken = jwtUtils.getOrGenerateRefreshToken(login, UUID.fromString(userId));
        jwtUtils.saveToken(userId, newRefreshToken);
        return new TokenDto(null, newRefreshToken);
    }

    private void checkRefreshToken(String refreshToken) {
        if (!jwtUtils.validateToken(refreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
        String redisRefreshToken = jwtUtils.getToken(jwtUtils.getSubject(refreshToken));
        if (!refreshToken.equals(redisRefreshToken)) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Refresh token is invalid");
        }
    }

    private User getUserIfPasswordCorrect(String login, String requestPassword) {
        List<User> users = userRepository.findUsersAmongLoginAndEmailByLogin(login)
                .stream()
                .filter(u -> passwordEncoder.matches(requestPassword, u.getPassword()))
                .toList();
        if (users.isEmpty()) {
            throw new CustomException(ExceptionType.UNAUTHORIZED, "Invalid credentials");
        } else if (users.size() > 1) {
            throw new CustomException(ExceptionType.FATAL, "Database inconsistency");
        }
        return users.get(0);
    }

    private void isLoginExist(String login) {
        if (!userRepository.isProfileExistByLogin(login)) {
            throw new CustomException(ExceptionType.NOT_FOUND, "Login is not found");
        }
    }

    void isLoginAndEmailAlreadyUsed(String login, String email) {
        if (userRepository.isProfileExistByLoginAndEmail(login, email)) {
            throw new CustomException(ExceptionType.ALREADY_EXISTS, "Login is already used");
        }
    }
}
