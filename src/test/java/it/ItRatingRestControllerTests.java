package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.user.JwtAuthorityDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.Form;
import com.ciklon.friendtracker.core.entity.Question;
import com.ciklon.friendtracker.core.entity.User;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {FriendTrackerApplication.class})
public class ItRatingRestControllerTests extends AbstractRestControllerBaseTest {
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
    private FormRepository formRepository;

    @Autowired
    private UserAnswerRepository userAnswerRepository;

    @BeforeEach
    public void setUp() {
        contactInteractionRepository.deleteAll();
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test rating getting functionality")
    public void givenRating_whenGetRating_thenReturnsRating() throws Exception {
        // given
        fillDatabase();
        String accessToken = performLoginAndGetToken(DataUtils.getIvanIvanovTransientLoginRequestDto());
        // when
        ResultActions result = mockMvc.perform(
                get(ApiPaths.RATING).contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .param("fromDate", "2021-01-01")
                        .param("toDate", "2025-12-31")
                        .param("fieldType", "COMMUNICATION")
                        .param("calculationType", "ALL")
                        .param("page", "1")
                        .param("size", "10")
        );
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.page").value(1))
                .andExpect(jsonPath("$.size").value(10))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.content[0].contactId").isNotEmpty())
                .andExpect(jsonPath("$.content[0].calculationType").value("ALL"));
    }

    private String performLoginAndGetToken(LoginRequestDto loginRequestDto) {
        JwtAuthorityDto dto = userService.login(loginRequestDto);
        return dto.accessToken();
    }


    private void fillDatabase() {
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        List<Contact> contacts = createContactsAndGetList(user);
        createFormsAndContactInteractions(user, contacts);
        List<Question> questions = createQuestionsAndAnswers();
        createUserAnswers(user, questions, contacts);
    }


    private List<UUID> createContactsAndGetUUIDs(User user) {
        List<Contact> contacts = List.of(
                contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
        return contacts.stream().map(Contact::getId).toList();
    }

    private List<Contact> createContactsAndGetList(User user) {
        return List.of(
                contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
    }


    private void createFormsAndContactInteractions(User user, List<Contact> contacts) {
        List<Form> formList = formRepository.saveAll(
                DataUtils.getFormEntityList(user, contacts)
        );
        contactInteractionRepository.saveAll(
                DataUtils.getContactInteractionEntityList(formList.get(0), contacts.stream().limit(2).toList())
        );
        contactInteractionRepository.saveAll(
                DataUtils.getContactInteractionEntityList(formList.get(1), contacts.stream().skip(2).toList())
        );
    }

    private List<Question> createQuestionsAndAnswers() {
        return questionRepository.saveAll(
                DataUtils.getQuestionList()
        );

    }

    private void createUserAnswers(
            User user,
            List<Question> questions,
            List<Contact> contacts
    ) {
        userAnswerRepository.saveAll(
                DataUtils.getUserAnswerList(user, questions, contacts)
        );
    }

}
