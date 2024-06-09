package utils;



import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.api.dto.enums.InteractionMark;
import com.ciklon.friendtracker.api.dto.enums.InteractionType;
import com.ciklon.friendtracker.api.dto.enums.MoodType;
import com.ciklon.friendtracker.api.dto.form.ContactInteractionCreationDto;
import com.ciklon.friendtracker.api.dto.form.FormCreationDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.api.dto.user.RegistrationRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public static UpdateContactDto getFullJohnDoeTransientUpdateContactDto(){
        return new UpdateContactDto(
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
                        new ContactInteractionCreationDto(contactIds.get(0), InteractionType.CALL,
                                0, 1, 10, 1, 5, 5,
                                InteractionMark.LIKE),
                        new ContactInteractionCreationDto(contactIds.get(1),
                                InteractionType.MEETING, 10, 0, 0, 0, 0, 0,
                                InteractionMark.DISLIKE)
                )
        );
    }
}
