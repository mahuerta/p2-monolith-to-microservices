package es.codeurjc.mca.userms.services;

import java.util.Collection;

import es.codeurjc.mca.userms.dtos.requests.UpdateUserEmailRequestDto;
import es.codeurjc.mca.userms.dtos.requests.UserRequestDto;
import es.codeurjc.mca.userms.dtos.responses.UserResponseDto;

public interface UserService {

    Collection<UserResponseDto> findAll();

    UserResponseDto save(UserRequestDto userRequestDto);

    UserResponseDto findById(long userId);

    UserResponseDto updateEmail(long userId, UpdateUserEmailRequestDto updateUserEmailRequestDto);

    UserResponseDto delete(long userId);

}
