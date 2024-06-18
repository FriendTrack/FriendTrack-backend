package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.question.QuestionCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.user.JwtAuthorityDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.core.entity.*;
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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {FriendTrackerApplication.class})
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
    private QuestionAnswerRepository questionAnswerRepository;

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
                .andExpect(jsonPath("$.answers").isNotEmpty());
    }

    @Test
    @DisplayName("Test question creation functionality with empty answers")
    public void givenQuestionCreationDtoWithEmptyAnswers_whenCreate_thenReturnBadRequest() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        QuestionCreationDto questionCreationDto = DataUtils.getQuestionCreationDtoWithEmptyAnswers();

        // when
        ResultActions result = mockMvc.perform(post(ApiPaths.QUESTION).contentType(MediaType.APPLICATION_JSON)
                                                       .header("Authorization", "Bearer " + accessToken)
                                                       .content(objectMapper.writeValueAsString(questionCreationDto)));

        // then
        result.andExpect(status().isBadRequest());
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
        List<QuestionAnswer> answers = questionAnswerRepository.saveAll(DataUtils.getQuestionAnswerEntities(question));

        // when
        ResultActions result =
                mockMvc.perform(get(ApiPaths.QUESTION_BY_ID, question.getId()).contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.question").value(question.getQuestion()))
                .andExpect(jsonPath("$.answers").isNotEmpty());
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
        List<QuestionAnswer> answers = questionAnswerRepository.saveAll(DataUtils.getQuestionAnswerEntities(question));
        UserAnswerCreationDto userAnswerCreationDto =
                DataUtils.getUserAnswerCreationDto(question.getId(), answers.get(0).getId(), contactIds.get(0));

        // when
        ResultActions result = mockMvc.perform(post(ApiPaths.USER_ANSWER).contentType(MediaType.APPLICATION_JSON)
                                                       .header("Authorization", "Bearer " + accessToken)
                                                       .content(objectMapper.writeValueAsString(userAnswerCreationDto)));

        // then
        result.andExpect(status().isOk()).andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.questionId").value(userAnswerCreationDto.questionId().toString()))
                .andExpect(jsonPath("$.contactId").value(userAnswerCreationDto.contactId().toString()))
                .andExpect(jsonPath("$.answerId").value(userAnswerCreationDto.answerId().toString()));
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
        List<QuestionAnswer> answers = questionAnswerRepository.saveAll(DataUtils.getQuestionAnswerEntities(question));
        UserAnswer userAnswer = userAnswerRepository.save(DataUtils.getUserAnswerEntity(contacts.get(0),
                                                                                        question,
                                                                                        answers.get(0),
                                                                                        user
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
                .andExpect(jsonPath("$.answerId").value(answers.get(0).getId().toString()));
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
