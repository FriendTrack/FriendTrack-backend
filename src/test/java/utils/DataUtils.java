package utils;


import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionCreationDto;
import com.ciklon.friendtracker.api.dto.form.FormCreationDto;
import com.ciklon.friendtracker.api.dto.question.QuestionAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.question.QuestionCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.api.dto.user.RegistrationRequestDto;
import com.ciklon.friendtracker.core.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;


public class DataUtils {

    public static RegistrationRequestDto getIvanIvanovTransientRegistrationRequestDto(){
        return new RegistrationRequestDto(
                "email@mail.ru",
                "password123",
                "ivan_ivanov",
                "ivan"
        );
    }

    public static LoginRequestDto getIvanIvanovTransientLoginRequestDto(){
        return new LoginRequestDto(
                "ivan_ivanov",
                "password123"
        );
    }

    public static User getIvanIvanovPersistedUserEntity(){
        return new User(
                UUID.randomUUID(),
                "email@mail.ru",
                "ivan_ivanov",
                "$2a$10$CQvDUJHtaddDvTxSKZt/cOxCyFs.NMVOprr37SlXbv7FOHEdLwgmy",
                "ivan",
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static ContactCreationDto getJohnDoeTransientContactCreationDto(){
        return new ContactCreationDto(
                "John Doe",
                "Some details",
                "https://www.linkedin.com/in/johndoe",
                LocalDate.now()
        );
    }

    public static UpdateContactDto getPartialJohnDoeTransientUpdateContactDtoWithDetails(){
        return new UpdateContactDto(
                "John Doe",
                "Some details",
                null,
                null
        );
    }

    public static Contact getFullJohnDoePersistedContactEntity(User user){
        return new Contact(
                UUID.randomUUID(),
                "John Doe",
                "Some details",
                "https://www.linkedin.com/in/johndoe",
                LocalDate.now(),
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static Contact getPartialJohnDoePersistedContactEntity(User user){
        return new Contact(
                "John Doe",
                null,
                null,
                null,
                user,
                LocalDateTime.now(),
                LocalDateTime.now()
        );
    }

    public static FormCreationDto getFormCreationDto(List<UUID> contactIds) {
        return new FormCreationDto(
                LocalDate.now(),
                2,
                List.of(
                        new ContactInteractionCreationDto(contactIds.get(0),
                                0, 1, 5, 1, 5),
                        new ContactInteractionCreationDto(contactIds.get(1),
                                10, 0, 0, 0, 0)
                )
        );
    }

    public static Form getFormEntity(User user, Integer contactInteractionCount) {
        return new Form(
                UUID.randomUUID(),
                LocalDate.now(),
                user,
                contactInteractionCount,
                LocalDateTime.now(),
                LocalDateTime.now(),
                Collections.emptyList()
        );
    }

    public static QuestionCreationDto getQuestionCreationDto() {
        return new QuestionCreationDto(
                "Question text",
                FieldType.COMMUNICATION
        );
    }


    public static QuestionCreationDto getQuestionCreationDtoWithEmptyAnswers() {
        return new QuestionCreationDto(
                "Question text",
                FieldType.COMMUNICATION
        );
    }

    public static QuestionCreationDto getQuestionCreationDtoWithEmptyQuestion() {
        return new QuestionCreationDto(
                null,
                FieldType.COMMUNICATION
        );
    }

    public static Question getQuestionEntity() {
        return new Question(
                UUID.randomUUID(),
                "Question text",
                FieldType.COMMUNICATION
        );
    }

    public static UserAnswerCreationDto getUserAnswerCreationDto(
            UUID questionId, UUID contactId, int value
    ) {
        return new UserAnswerCreationDto(
                questionId,
                contactId,
                value
        );
    }


    public static UserAnswer getUserAnswerEntity(Contact contact, Question question, User user, int value) {
        return new UserAnswer(
                question,
                value,
                contact,
                user
        );
    }

    public static List<Form> getFormEntityList(User user, List<Contact> contacts) {
        return List.of(
                new Form(
                        UUID.randomUUID(),
                        LocalDate.now(),
                        user,
                        2,
                        LocalDateTime.now(),
                        LocalDateTime.now(),
                        Collections.emptyList()
                ),
                new Form(UUID.randomUUID(), LocalDate.now(), user, 2, LocalDateTime.now(),
                         LocalDateTime.now(), Collections.emptyList()
                )
        );
    }

    public static List<ContactInteraction> getContactInteractionEntityList(Form form, List<Contact> contacts) {
        return contacts.stream().map(contact -> new ContactInteraction(
                contact,
                form,
                (int) (Math.random() * 10),
                (int) (Math.random() * 10),
                (int) (Math.random() * 10),
                (int) (Math.random() * 10),
                (int) (Math.random() * 10)
        )).toList();
    }

    public static List<Question> getQuestionList() {
        return List.of(
                new Question(UUID.randomUUID(), "Question 1", FieldType.COMMUNICATION),
                new Question(UUID.randomUUID(), "Question 2", FieldType.EMPATHY),
                new Question(UUID.randomUUID(), "Question 3", FieldType.TRUST)
        );
    }


    public static List<UserAnswer> getUserAnswerList(User user, List<Question> questions, List<Contact> contacts) {
        return List.of(
                new UserAnswer(questions.get(0), 3, contacts.get(0), user),
                new UserAnswer(questions.get(0), 1, contacts.get(1), user),
                new UserAnswer(questions.get(1), 5, contacts.get(0), user),
                new UserAnswer(questions.get(1), 3, contacts.get(1), user),
                new UserAnswer(questions.get(2), 1, contacts.get(0), user),
                new UserAnswer(questions.get(2), 3, contacts.get(1), user)
        );
    }
}
