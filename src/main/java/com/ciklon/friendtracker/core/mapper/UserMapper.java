package com.ciklon.friendtracker.core.mapper;


import com.ciklon.friendtracker.api.dto.RegistrationRequestDto;
import com.ciklon.friendtracker.api.dto.UpdateUserDto;
import com.ciklon.friendtracker.api.dto.UserDto;
import com.ciklon.friendtracker.core.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "password", source = "encode")
    User map(RegistrationRequestDto registrationRequestDto, String encode);

    UserDto map(User user);

    @Mapping(target = "password", source = "encode", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "login", source = "updateUserDto.login", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "email", source = "updateUserDto.email", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "username", source = "updateUserDto.username", nullValuePropertyMappingStrategy =
            NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    User map(@MappingTarget User user, UpdateUserDto updateUserDto, String encode);
}

