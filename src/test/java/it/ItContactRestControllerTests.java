package it;

import com.ciklon.friendtracker.FriendTrackerApplication;
import com.ciklon.friendtracker.api.constant.ApiPaths;
import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.User;
import com.ciklon.friendtracker.core.repository.ContactRepository;
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
import org.testcontainers.junit.jupiter.Testcontainers;
import utils.DataUtils;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "test")
@AutoConfigureMockMvc
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {FriendTrackerApplication.class}
)
public class ItContactRestControllerTests extends AbstractRestControllerBaseTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    public void setUp() {
        contactRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Test contact creation functionality")
    public void givenContactCreationDto_whenCreate_thenReturnOkAndEntityInDb() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        ContactCreationDto dto = DataUtils.getJohnDoeTransientContactCreationDto();

        // when
        ResultActions result = mockMvc.perform(
                post(ApiPaths.CONTACT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(dto))
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.details").value(dto.details()))
                .andExpect(jsonPath("$.link").value(dto.link()))
                .andExpect(jsonPath("$.birthDate").value(dto.birthDate().toString()));

        // Extract the id from the response
        String responseBody = result.andReturn().getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        UUID contactId = UUID.fromString(jsonNode.get("id").asText());

        // Additional database checks
        Contact contact = contactRepository.findById(contactId)
                .orElseThrow(() -> new AssertionError("Contact not found in database"));
        assertEquals(dto.name(), contact.getName());
        assertEquals(dto.details(), contact.getDetails());
        assertEquals(dto.link(), contact.getLink());
        assertEquals(dto.birthDate(), contact.getBirthDate());
    }


    @Test
    @DisplayName("Test partial update contact functionality")
    public void givenUpdateContactDto_whenPartialUpdate_thenReturnOk() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        user = userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        Contact contact = contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user));
        UpdateContactDto updateContactDto = DataUtils.getPartialJohnDoeTransientUpdateContactDtoWithDetails();

        // when
        ResultActions result = mockMvc.perform(
                patch(ApiPaths.CONTACT_BY_ID, contact.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
                        .content(objectMapper.writeValueAsString(updateContactDto))
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(updateContactDto.name()))
                .andExpect(jsonPath("$.details").value(updateContactDto.details()))
                .andExpect(jsonPath("$.link").value(contact.getLink()))
                .andExpect(jsonPath("$.birthDate").value(contact.getBirthDate()));
    }

    @Test
    @DisplayName("Test deleting contact functionality")
    public void givenContactId_whenDelete_thenReturnOkAndEntityDeleted() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        user = userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        Contact contact = contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user));

        // when
        ResultActions result = mockMvc.perform(
                delete(ApiPaths.CONTACT_BY_ID, contact.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk());

        // Additional database checks
        assertEquals(0, contactRepository.count());
    }

    @Test
    @DisplayName("Test fetching contacts list with pagination")
    public void givenPageAndSize_whenGetContactList_thenReturnPagedContactList() throws Exception {
        // given
        User user = DataUtils.getIvanIvanovPersistedUserEntity();
        user = userRepository.save(user);
        LoginRequestDto loginRequestDto = DataUtils.getIvanIvanovTransientLoginRequestDto();
        String accessToken = performLoginAndGetToken(loginRequestDto);

        List<Contact> contacts = List.of(
                contactRepository.save(DataUtils.getFullJohnDoePersistedContactEntity(user)),
                contactRepository.save(DataUtils.getPartialJohnDoePersistedContactEntity(user))
        );

        int page = 1;
        int size = 10;

        // when
        ResultActions result = mockMvc.perform(
                get(ApiPaths.CONTACT)
                        .param("page", String.valueOf(page))
                        .param("size", String.valueOf(size))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + accessToken)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contactDtoList", hasSize(contacts.size())))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.page").value(page))
                .andExpect(jsonPath("$.size").value(size));
    }

    private String performLoginAndGetToken(LoginRequestDto loginRequestDto) throws Exception {
        ResultActions loginResult = mockMvc.perform(
                post(ApiPaths.LOGIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto))
        );

        String loginResponse = loginResult.andReturn().getResponse().getContentAsString();
        JsonNode loginJson = objectMapper.readTree(loginResponse);
        return loginJson.get("accessToken").asText();
    }
}
