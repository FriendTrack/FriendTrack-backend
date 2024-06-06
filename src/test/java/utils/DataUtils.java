package utils;



import com.ciklon.friendtracker.api.dto.contact.ContactCreationDto;
import com.ciklon.friendtracker.api.dto.contact.UpdateContactDto;
import com.ciklon.friendtracker.api.dto.user.LoginRequestDto;
import com.ciklon.friendtracker.api.dto.user.RegistrationRequestDto;
import com.ciklon.friendtracker.core.entity.Contact;
import com.ciklon.friendtracker.core.entity.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
}
