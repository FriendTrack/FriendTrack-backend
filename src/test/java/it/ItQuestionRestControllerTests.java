package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.question.QuestionCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.user.JwtAuthorityDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.Question;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.entity.UserAnswer;
import com.ciklon.friendtracker.core.repository.*;
import com.ciklon.friendtracker.core.service.UserService;
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
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.DataUtils;

import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FriendTrackerApplication.class)
public class ItQuestionRestControllerTests extends AbstractRestControllerBaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ContactInteractionRepository contactInteractionRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @BeforeEach
    public void setUp() {
        contactInteractionRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test question creation functionality")
    public void givenQuestionCreationDto_whenCreate_thenReturnOkAndEntityInDb() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        QuestionCreationDto questionCreationDto = DataUtils.getQuestionCreationDto();

        // when
        ResultActions result = mockMvc.perform(post(ApiPaths.QUESTION).contentType(MediaType.APPLICATION_JSON)
                                                       .header("Authorization", "Bearer " + accessToken)
                                                       .content(objectMapper.writeValueAsString(questionCreationDto)));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value(questionCreationDto.question()))
                .andExpect(jsonPath("$.fieldType").value(questionCreationDto.fieldType().toString()));
    }

    @Test
    @DisplayName("Test question creation functionality with empty question")
    public void givenQuestionCreationDtoWithEmptyQuestion_whenCreate_thenReturnBadRequest() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        QuestionCreationDto questionCreationDto = DataUtils.getQuestionCreationDtoWithEmptyQuestion();

        // when
        ResultActions result = mockMvc.perform(post(ApiPaths.QUESTION).contentType(MediaType.APPLICATION_JSON)
                                                       .header("Authorization", "Bearer " + accessToken)
                                                       .content(objectMapper.writeValueAsString(questionCreationDto)));

        // then
        result.andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Test question getting functionality after creation")
    public void givenQuestionCreationDto_whenGet_thenReturnOkAndEntityInDb() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        Question question = questionRepository.save(DataUtils.getQuestionEntity());

        // when
        ResultActions result =
                mockMvc.perform(get(ApiPaths.QUESTION_BY_ID, question.getId()).contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value(question.getQuestion()));
    }

    @Test
    @DisplayName("Test user answer creation functionality")
    public void givenUserAnswerCreationDto_whenAddAnswer_thenAnswerAdded() throws Exception {
        // given
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);
        List<UUID> contactIds = createContactsAndGetUUIDs(user);
        Question question = questionRepository.save(DataUtils.getQuestionEntity());
        UserAnswerCreationDto userAnswerCreationDto =
                DataUtils.getUserAnswerCreationDto(question.getId(), contactIds.get(0), 4);

        // when
        ResultActions result = mockMvc.perform(post(ApiPaths.USER_ANSWER).contentType(MediaType.APPLICATION_JSON)
                                                       .header("Authorization", "Bearer " + accessToken)
                                                       .content(objectMapper.writeValueAsString(userAnswerCreationDto)));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.questionId").value(userAnswerCreationDto.questionId().toString()))
                .andExpect(jsonPath("$.contactId").value(userAnswerCreationDto.contactId().toString()))
                .andExpect(jsonPath("$.value").value(userAnswerCreationDto.value()));
    }

    @Test
    @DisplayName("Test user answer deletion functionality")
    public void givenUserAnswerId_whenDeleteAnswer_thenAnswerDeleted() throws Exception {
        // given
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);
        List<Contact> contacts = createContactsAndGetList(user);
        Question question = questionRepository.save(DataUtils.getQuestionEntity());
        UserAnswer userAnswer = userAnswerRepository.save(DataUtils.getUserAnswerEntity(contacts.get(0),
                                                                                        question, user, 3
        ));

        // when
        ResultActions result = mockMvc.perform(delete(
                ApiPaths.USER_ANSWER_BY_ID,
                userAnswer.getId()
        ).contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.questionId").value(question.getId().toString()))
                .andExpect(jsonPath("$.contactId").value(contacts.get(0).getId().toString()))
                .andExpect(jsonPath("$.value").value(3));
    }


    private String performLoginAndGetToken(LoginRequestDto loginRequestDto) {
        JwtAuthorityDto dto = userService.login(loginRequestDto);
        return dto.accessToken();
    }

    private List<UUID> createContactsAndGetUUIDs(User user) {
        List<Contact> contacts = List.of(contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                                         contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
        return contacts.stream().map(Contact::getId).toList();
    }

    private List<Contact> createContactsAndGetList(User user) {
        return List.of(contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                       contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
    }
}
