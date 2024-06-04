package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.LoginRequestDto;
import com.ciklon.friendtracker.api.dto.LogoutRequestDto;
import com.ciklon.friendtracker.api.dto.RegistrationRequestDto;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.DataUtils;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.matchesPattern;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Testcontainers()
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FriendTrackerApplication.class}
)
public class ItUserRestControllerTests extends AbstractRestControllerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test user registration functionality")
    public void givenRegisterDto_whenSave_thenReturnJwtAuthorityDto() throws Exception {
        // given
        RegistrationRequestDto dto = DataUtils.getIvanIvanovTransientRegistrationRequestDto();
        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.userId")
                                   .value(matchesPattern("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))
                ) // parse UUID
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.accessToken")
                                   .isNotEmpty()
                )
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.refreshToken")
                                   .isNotEmpty()
                );
    }

    @Test
    @DisplayName("Test user registration with duplicated login functionality")
    public void givenRegisterDto_whenSave_thenUserWithSuchLoginIsAlreadyRegistered() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        RegistrationRequestDto dto = DataUtils.getIvanIvanovTransientRegistrationRequestDto();

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.REGISTER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
        );
        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.message")
                                   .value("Login is already used")
                );

    }

    @Test
    @DisplayName("Test user login functionality")
    public void givenLoginRequestDto_whenLogin_thenReturnJwtAuthorityDto() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = new LoginRequestDto(user.getLogin(), user.getPassword());

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.userId")
                                   .value(matchesPattern("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$"))
                ) // parse UUID
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.accessToken")
                                   .isNotEmpty()
                )
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.refreshToken")
                                   .isNotEmpty()
                );
    }

    @Test
    @DisplayName("Test user login functionality with wrong data")
    public void givenLoginRequestDto_whenLogin_thenUserLoginNotFound() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = new LoginRequestDto("random login", user.getPassword());

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers
                                   .jsonPath("$.message")
                                   .value("Login is not found")
                );
    }

    @Test
    @DisplayName("Test user logout functionality")
    public void givenLogoutRequestDto_whenLogout_thenReturnOk() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();

        ResultActions loginResult = mockMvc.perform(
                post(ApiPaths.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        String loginResponse = loginResult.andReturn().getResponse().getContentAsString();
        JsonNode loginJson = objectMapper.readTree(loginResponse);
        String accessToken = loginJson.get("accessToken").asText();

        LogoutRequestDto logoutRequestDto = new LogoutRequestDto(user.getId());

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.LOGOUT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(logoutRequestDto))
        );

        // then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
