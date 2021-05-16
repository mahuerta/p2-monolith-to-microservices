package es.codeurjc.books.services.impl;

import es.codeurjc.books.dtos.requests.BookRequestDto;
import es.codeurjc.books.dtos.responses.BookDetailsResponseDto;
import es.codeurjc.books.dtos.responses.BookResponseDto;
import es.codeurjc.books.dtos.responses.UserResponseDto;
import es.codeurjc.books.exceptions.BookNotFoundException;
import es.codeurjc.books.models.Book;
import es.codeurjc.books.models.Comment;
import es.codeurjc.books.models.User;
import es.codeurjc.books.repositories.BookRepository;
import es.codeurjc.books.services.BookService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements BookService {

    private final Mapper mapper;
    private final BookRepository bookRepository;

    private final UserMSServiceImpl userMSService;

    @Value(value = "${use.user-ms}")
    private boolean useUserService;

    public BookServiceImpl(Mapper mapper, BookRepository bookRepository, UserMSServiceImpl userMSService) {
        this.mapper = mapper;
        this.bookRepository = bookRepository;
        this.userMSService = userMSService;
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

        if (useUserService) {
            List<UserResponseDto> users = this.userMSService.getUsers();
            for (Comment comment : book.getComments()) {
                Long idUser = comment.getUserMSId();
                User user = new User();
                if (user != null) {
                    Optional<UserResponseDto> userFromMS = users.stream().filter(userResponseDto -> userResponseDto.getId().equals(idUser))
                            .findFirst();

                    if (userFromMS.isPresent()) {
                        user.setNick(userFromMS.get().getNick());
                        user.setEmail(userFromMS.get().getEmail());
                    }

                }
                comment.setUser(user);
            }

        }

        return this.mapper.map(book, BookDetailsResponseDto.class);
    }

}
