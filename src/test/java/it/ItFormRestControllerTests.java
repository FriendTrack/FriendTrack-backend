package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.form.FormCreationDto;
import com.ciklon.friendtracker.api.dto.user.JwtAuthorityDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.ContactInteraction;
import com.ciklon.friendtracker.core.entity.Form;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.repository.ContactInteractionRepository;
import com.ciklon.friendtracker.core.repository.ContactRepository;
import com.ciklon.friendtracker.core.repository.FormRepository;
import com.ciklon.friendtracker.core.repository.UserRepository;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FriendTrackerApplication.class}
)
public class ItFormRestControllerTests extends AbstractRestControllerBaseTest {
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
    private FormRepository formRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test form creation functionality")
    public void givenFormCreationDto_whenPostNewForm_thenReturnNewFormDto() throws Exception {
        // given
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);
        List<UUID> contactIds = createContactsAndGetUUIDs(user);
        FormCreationDto formCreationDto = DataUtils.getFormCreationDto(contactIds);
        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.FORM)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(formCreationDto))
        );
        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.mood").isNotEmpty())
                .andExpect(jsonPath("$.contactInteractions", hasSize(2)));

        // проверь что contactInteractions db содержит 2 элемента
        List<ContactInteraction> contactInteractions = contactInteractionRepository.findAll();
        UUID formId =
                UUID.fromString(objectMapper.readTree(result.andReturn().getResponse().getContentAsString()).get("id")
                                        .asText());
        assertEquals(2, contactInteractions.size());
        contactInteractions.forEach(contactInteraction -> {
            assertEquals(formCreationDto.contactInteractions().stream()
                                 .filter(contactInteractionCreationDto -> contactInteractionCreationDto.contactId()
                                         .equals(contactInteraction.getContact().getId())).findFirst().orElseThrow()
                                 .contactId(), contactInteraction.getContact().getId());
            // check form id
            assertEquals(contactInteraction.getForm().getId(), formId);
        });

    }

    @Test
    @DisplayName("Test form deletion functionality")
    public void givenFormId_whenDeleteForm_thenReturnSuccess() throws Exception {
        // given
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);
        List<Contact> contactList = createContactsAndGeList(user);
        Form formEntity = formRepository.save(DataUtils.getFormEntity(user, contactList.size()));
        createContactsInteractions(contactList, formEntity);

        // when
        ResultActions result =
                mockMvc.perform(delete(ApiPaths.FORM_BY_ID, formEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer " + accessToken));
        // then
        result.andExpect(status().isOk());

        List<ContactInteraction> contactInteractionsFromDb = contactInteractionRepository.findAll();
        assertEquals(0, contactInteractionsFromDb.size());
        assertEquals(0, formRepository.count());
    }

    @Test
    @DisplayName("Test form getting functionality")
    public void givenFormId_whenGetForm_thenReturnFormDto() throws Exception {
        // given
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);
        List<Contact> contactList = createContactsAndGeList(user);
        Form formEntity = formRepository.save(DataUtils.getFormEntity(user, contactList.size()));
        createContactsInteractions(contactList, formEntity);

        // when
        ResultActions result =
                mockMvc.perform(get(ApiPaths.FORM_BY_ID, formEntity.getId()).contentType(MediaType.APPLICATION_JSON)
                                        .header("Authorization", "Bearer " + accessToken));

        // then
        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.userId").isNotEmpty())
                .andExpect(jsonPath("$.mood").isNotEmpty())
                .andExpect(jsonPath("$.contactInteractions", hasSize(2)));
    }



    private String performLoginAndGetToken(LoginRequestDto loginRequestDto) {
        JwtAuthorityDto dto = userService.login(loginRequestDto);
        return dto.accessToken();
    }

    private List<UUID> createContactsAndGetUUIDs(User user) {
        List<Contact> contacts = List.of(
                contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
        return contacts.stream().map(Contact::getId).toList();
    }

    private List<Contact> createContactsAndGeList(User user) {
        return List.of(
                contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );
    }

    private void createContactsInteractions(List<Contact> contactList, Form form) {
        contactInteractionRepository.save(new ContactInteraction(contactList.get(0), form, 0, 1, 10, 1, 5));
        contactInteractionRepository.save(new ContactInteraction(contactList.get(1), form, 10, 0, 0, 0, 0));
    }
}
