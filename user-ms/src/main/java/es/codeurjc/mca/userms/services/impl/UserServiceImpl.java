package es.codeurjc.mca.userms.services.impl;

import es.codeurjc.mca.userms.dtos.requests.UpdateUserEmailRequestDto;
import es.codeurjc.mca.userms.dtos.requests.UserRequestDto;
import es.codeurjc.mca.userms.dtos.responses.CommentResponseDto;
import es.codeurjc.mca.userms.dtos.responses.UserResponseDto;
import es.codeurjc.mca.userms.exceptions.UserCanNotBeDeletedException;
import es.codeurjc.mca.userms.exceptions.UserNotFoundException;
import es.codeurjc.mca.userms.exceptions.UserWithSameNickException;
import es.codeurjc.mca.userms.models.User;
import es.codeurjc.mca.userms.repositories.UserRepository;
import es.codeurjc.mca.userms.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final ModelMapper modelMapper;

    private UserRepository userRepository;

    @Value(value = "${monolith.url}")
    private String monolithBaseUri;

    @Autowired
    private RestTemplate restTemplate;

    public UserServiceImpl(UserRepository userRepository) {
        this.modelMapper = new ModelMapper();
        this.userRepository = userRepository;
    }

    public Collection<UserResponseDto> findAll() {
        return this.userRepository.findAll().stream()
                .map(user -> this.modelMapper.map(user, UserResponseDto.class))
                .collect(Collectors.toList());
    }

    public UserResponseDto save(UserRequestDto userRequestDto) {
        if (this.userRepository.existsByNick(userRequestDto.getNick())) {
            throw new UserWithSameNickException();
        }
        User user = this.modelMapper.map(userRequestDto, User.class);
        user = this.userRepository.save(user);
        return this.modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto findById(long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return this.modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto updateEmail(long userId, UpdateUserEmailRequestDto updateUserEmailRequestDto) {
        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getEmail().equalsIgnoreCase(updateUserEmailRequestDto.getEmail())) {
            user.setEmail(updateUserEmailRequestDto.getEmail());
            user = this.userRepository.save(user);
        }
        return this.modelMapper.map(user, UserResponseDto.class);
    }

    public UserResponseDto delete(long userId) {
        User user = this.userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        if (!CollectionUtils.isEmpty(getComments(userId))) {
            throw new UserCanNotBeDeletedException();
        }
        this.userRepository.delete(user);
        return this.modelMapper.map(user, UserResponseDto.class);
    }

    @Override
    public Collection<CommentResponseDto> getUserComments(long userId) {
        return this.getComments(userId);
    }

    private List<CommentResponseDto> getComments(long userId) {
        ResponseEntity<List<CommentResponseDto>> responseEntity =
                restTemplate.exchange(
                        monolithBaseUri + "api/v1/users/" + userId,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );
        List<CommentResponseDto> commentResponseDtos = responseEntity.getBody();
        return commentResponseDtos.stream()
                .map(commentResponseDto -> this.modelMapper.map(commentResponseDto, CommentResponseDto.class))
                .collect(Collectors.toList());
    }
}
