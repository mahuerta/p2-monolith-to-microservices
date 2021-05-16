package es.codeurjc.books.services.impl;

import es.codeurjc.books.dtos.requests.BookRequestDto;
import es.codeurjc.books.dtos.responses.*;
import es.codeurjc.books.exceptions.BookNotFoundException;
import es.codeurjc.books.models.Book;
import es.codeurjc.books.models.Comment;
import es.codeurjc.books.repositories.BookRepository;
import es.codeurjc.books.services.BookService;
import es.codeurjc.books.services.UserService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final Mapper mapper;
    private final BookRepository bookRepository;

    private final UserMSServiceImpl userMSService;
    private final UserService userService;

    @Value(value = "${use.user-ms}")
    private boolean useUserService;

    public BookServiceImpl(Mapper mapper, BookRepository bookRepository, UserMSServiceImpl userMSService, UserService userService) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.userMSService = userMSService;
        this.userService = userService;
    }

    public Collection<BookResponseDto> findAll() {
        return this.bookRepository.findAll().stream()
                .map(book -> this.mapper.map(book, BookResponseDto.class))
                .collect(Collectors.toList());
    }

    public BookDetailsResponseDto save(BookRequestDto bookRequestDto) {
        Book book = this.mapper.map(bookRequestDto, Book.class);
        book = this.bookRepository.save(book);
        return this.mapper.map(book, BookDetailsResponseDto.class);
    }

    public BookDetailsResponseDto findById(long bookId) {
        Book book = this.bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        Collection<UserResponseDto> users;

        if (useUserService) {
            users = this.userMSService.getUsers();
        } else {
            users = this.userService.findAll();
        }

        BookDetailsResponseDto bookDetailsResponseDto = this.mapper.map(book, BookDetailsResponseDto.class);
        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();
        for (Comment comment : book.getComments()) {
            CommentResponseDto commentResponseDto = new CommentResponseDto();
            commentResponseDto.setId(comment.getId());
            commentResponseDto.setScore(comment.getScore());
            commentResponseDto.setComment(comment.getComment());

            Long idUser = comment.getUserId();
            CommentUserResponseDto commentUserResponseDto = new CommentUserResponseDto();
            Optional<UserResponseDto> userDB = users.stream().filter(userResponseDto -> userResponseDto.getId().equals(idUser))
                    .findFirst();

            if (userDB.isPresent()) {
                commentUserResponseDto.setNick(userDB.get().getNick());
                commentUserResponseDto.setEmail(userDB.get().getEmail());
            }
            commentResponseDto.setUser(commentUserResponseDto);
            commentResponseDtos.add(commentResponseDto);
        }

        bookDetailsResponseDto.setComments(commentResponseDtos);

        return bookDetailsResponseDto;
    }

}
