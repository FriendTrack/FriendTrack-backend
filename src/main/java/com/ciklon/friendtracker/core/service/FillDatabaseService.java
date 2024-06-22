package com.ciklon.friendtracker.core.service;

import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.api.dto.enums.FieldType;
import com.ciklon.friendtracker.api.dto.enums.MoodType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionCreationDto;
import com.ciklon.friendtracker.api.dto.form.FormCreationDto;
import com.ciklon.friendtracker.api.dto.question.QuestionAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.question.QuestionCreationDto;
import com.ciklon.friendtracker.api.dto.rating.UserAnswerCreationDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.api.dto.user.RegistrationRequestDto;
import com.ciklon.friendtracker.core.entity.*;
import com.ciklon.friendtracker.core.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FillDatabaseService {

    private UserRepository userRepository;

    private ContactRepository contactRepository;

    private UserService userService;

    private ContactInteractionRepository contactInteractionRepository;

    private QuestionRepository questionRepository;

    private QuestionAnswerRepository questionAnswerRepository;

    private FormRepository formRepository;

    private UserAnswerRepository userAnswerRepository;

    public void performFillDatabase() {
        User user = userRepository.save(DataUtils.getIvanIvanovPersistedUserEntity());
        List<Contact> contacts = createContactsAndGetList(user);
        createFormsAndContactInteractions(user, contacts);
        List<Question> questions = createQuestionsAndAnswers();
        List<QuestionAnswer> questionAnswers = createQuestionAnswers(questions);
        createUserAnswers(user, questions, questionAnswers, contacts);
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

    private List<QuestionAnswer> createQuestionAnswers(List<Question> questions) {
        List<QuestionAnswer> questionAnswersForCreation =
                new ArrayList<>(DataUtils.getFirstQuestionAnswerList(questions.get(0)));
        questionAnswersForCreation.addAll(DataUtils.getSecondQuestionAnswerList(questions.get(1)));
        questionAnswersForCreation.addAll(DataUtils.getThirdQuestionAnswerList(questions.get(2)));
        return questionAnswerRepository.saveAll(
                questionAnswersForCreation
        );
    }

    private void createUserAnswers(
            User user,
            List<Question> questions,
            List<QuestionAnswer> questionAnswers,
            List<Contact> contacts
    ) {
        userAnswerRepository.saveAll(
                DataUtils.getUserAnswerList(user, questions, questionAnswers, contacts)
        );
    }



    private static class DataUtils {

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
                    MoodType.HAPPY,
                    LocalDate.now(),
                    2,
                    List.of(
                            new ContactInteractionCreationDto(contactIds.get(0),
                                                              0, 1, 0, 1, 5),
                            new ContactInteractionCreationDto(contactIds.get(1),
                                                              3, 0, 0, 0, 0)
                    )
            );
        }

        public static Form getFormEntity(User user, Integer contactInteractionCount) {
            return new Form(
                    UUID.randomUUID(),
                    MoodType.HAPPY,
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
                    FieldType.COMMUNICATION,
                    List.of(
                            new QuestionAnswerCreationDto("Answer 1", true),
                            new QuestionAnswerCreationDto("Answer 2", false)
                    )
            );
        }


        public static QuestionCreationDto getQuestionCreationDtoWithEmptyAnswers() {
            return new QuestionCreationDto(
                    "Question text",
                    FieldType.COMMUNICATION,
                    Collections.emptyList()
            );
        }

        public static QuestionCreationDto getQuestionCreationDtoWithEmptyQuestion() {
            return new QuestionCreationDto(
                    null,
                    FieldType.COMMUNICATION,
                    List.of(
                            new QuestionAnswerCreationDto("Answer 1", true),
                            new QuestionAnswerCreationDto("Answer 2", false)
                    )
            );
        }

        public static Question getQuestionEntity() {
            return new Question(
                    UUID.randomUUID(),
                    "Question text",
                    FieldType.COMMUNICATION,
                    Collections.emptyList()
            );
        }

        public static List<QuestionAnswer> getQuestionAnswerEntities(Question question) {
            return List.of(
                    new QuestionAnswer(UUID.randomUUID(), question, "Answer 1", true),
                    new QuestionAnswer(UUID.randomUUID(), question, "Answer 2", false)
            );
        }

        public static UserAnswerCreationDto getUserAnswerCreationDto(
                UUID questionId, UUID answerId, UUID contactId
        ) {
            return new UserAnswerCreationDto(
                    questionId,
                    contactId,
                    answerId
            );
        }


        public static UserAnswer getUserAnswerEntity(Contact contact, Question question, QuestionAnswer questionAnswer, User user) {
            return new UserAnswer(
                    question,
                    questionAnswer,
                    contact,
                    user
            );
        }

        public static List<Form> getFormEntityList(User user, List<Contact> contacts) {
            return List.of(
                    new Form(
                            UUID.randomUUID(),
                            MoodType.HAPPY,
                            LocalDate.now(),
                            user,
                            2,
                            LocalDateTime.now(),
                            LocalDateTime.now(),
                            Collections.emptyList()
                    ),
                    new Form(UUID.randomUUID(), MoodType.NEUTRAL, LocalDate.now(), user, 2, LocalDateTime.now(),
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
                    new Question(UUID.randomUUID(), "Question 1", FieldType.COMMUNICATION, Collections.emptyList()),
                    new Question(UUID.randomUUID(), "Question 2", FieldType.EMPATHY, Collections.emptyList()),
                    new Question(UUID.randomUUID(), "Question 3", FieldType.TRUST, Collections.emptyList())
            );
        }

        public static List<QuestionAnswer> getFirstQuestionAnswerList(Question question) {
            return List.of(
                    new QuestionAnswer("Answer 1",question, true),
                    new QuestionAnswer("Answer 2",question, false),
                    new QuestionAnswer("Answer 3",question, false),
                    new QuestionAnswer("Answer 4",question, false)
            );

        }

        public static List<QuestionAnswer> getSecondQuestionAnswerList(Question question) {
            return List.of(
                    new QuestionAnswer("Answer 1",question, false),
                    new QuestionAnswer("Answer 2",question, true),
                    new QuestionAnswer("Answer 3",question, false),
                    new QuestionAnswer("Answer 4",question, false)
            );
        }

        public static List<QuestionAnswer> getThirdQuestionAnswerList(Question question) {
            return List.of(
                    new QuestionAnswer("Answer 1",question, false),
                    new QuestionAnswer("Answer 2",question, false),
                    new QuestionAnswer("Answer 3",question, true),
                    new QuestionAnswer("Answer 4",question, false),
                    new QuestionAnswer("Answer 5",question, false),
                    new QuestionAnswer("Answer 6",question, false)
            );
        }

        public static List<UserAnswer> getUserAnswerList(User user, List<Question> questions,
                                                         List<QuestionAnswer> questionAnswers, List<Contact> contacts) {
            return List.of(
                    new UserAnswer(questions.get(0), questionAnswers.get(0), contacts.get(0), user),
                    new UserAnswer(questions.get(0), questionAnswers.get(1), contacts.get(1), user),
                    new UserAnswer(questions.get(1), questionAnswers.get(2), contacts.get(0), user),
                    new UserAnswer(questions.get(1), questionAnswers.get(3), contacts.get(1), user),
                    new UserAnswer(questions.get(2), questionAnswers.get(4), contacts.get(0), user),
                    new UserAnswer(questions.get(2), questionAnswers.get(5), contacts.get(1), user)
            );
        }
    }

}
