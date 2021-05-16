package es.codeurjc.books.services.impl;

import es.codeurjc.books.dtos.responses.UserResponseDto;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserMSServiceImpl {

    private final ModelMapper modelMapper;
    @Value(value = "${user-ms.url}")
    private String userMSBaseUri;
    private RestTemplate restTemplate;

    public UserMSServiceImpl(RestTemplate restTemplate) {
        this.modelMapper = new ModelMapper();
        this.restTemplate = restTemplate;
    }

    public List<UserResponseDto> getUsers() {
        ResponseEntity<List<UserResponseDto>> responseEntity =
                restTemplate.exchange(
                        userMSBaseUri + "api/v1/users/",
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        List<UserResponseDto> userResponseDtos = responseEntity.getBody();

        return userResponseDtos.stream()
                .map(userResponseDto -> this.modelMapper.map(userResponseDto, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    public UserResponseDto getUserByNick(String nick) {
        ResponseEntity<UserResponseDto> responseEntity =
                restTemplate.exchange(
                        userMSBaseUri + "api/v1/users?nick=" + nick,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {
                        }
                );
        UserResponseDto userResponseDto = responseEntity.getBody();

        return this.modelMapper.map(userResponseDto, UserResponseDto.class);
    }

}
