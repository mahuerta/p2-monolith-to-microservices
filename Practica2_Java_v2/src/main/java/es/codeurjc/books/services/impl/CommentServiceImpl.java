package es.codeurjc.books.services.impl;

import es.codeurjc.books.dtos.requests.CommentRequestDto;
import es.codeurjc.books.dtos.responses.CommentResponseDto;
import es.codeurjc.books.dtos.responses.CommentUserResponseDto;
import es.codeurjc.books.dtos.responses.UserCommentResponseDto;
import es.codeurjc.books.dtos.responses.UserResponseDto;
import es.codeurjc.books.exceptions.BookNotFoundException;
import es.codeurjc.books.exceptions.CommentNotFoundException;
import es.codeurjc.books.exceptions.UserNotFoundException;
import es.codeurjc.books.models.Book;
import es.codeurjc.books.models.Comment;
import es.codeurjc.books.models.User;
import es.codeurjc.books.repositories.BookRepository;
import es.codeurjc.books.repositories.CommentRepository;
import es.codeurjc.books.repositories.UserRepository;
import es.codeurjc.books.services.CommentService;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private final UserMSServiceImpl userMSService;
    private final Mapper mapper;
    private final CommentRepository commentRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    @Value(value = "${use.user-ms}")
    private boolean useUserService;

    public CommentServiceImpl(Mapper mapper, CommentRepository commentRepository, BookRepository bookRepository,
                              UserRepository userRepository, UserMSServiceImpl userMSService) {
        this.mapper = mapper;
        this.commentRepository = commentRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.userMSService = userMSService;
    }

    public CommentResponseDto addComment(long bookId, CommentRequestDto commentRequestDto) {
        Book book = this.bookRepository.findById(bookId).orElseThrow(BookNotFoundException::new);
        User user;
        CommentUserResponseDto commentUserResponseDto = new CommentUserResponseDto();
        Comment comment = this.mapper.map(commentRequestDto, Comment.class);
        comment.setBook(book);

        if (useUserService) {
            UserResponseDto userResponseDto = this.userMSService.getUserByNick(commentRequestDto.getUserNick());
            if (userResponseDto == null) {
                throw new UserNotFoundException();
            }
            comment.setUserId(userResponseDto.getId());
            commentUserResponseDto.setNick(userResponseDto.getNick());
            commentUserResponseDto.setEmail(userResponseDto.getEmail());
        } else {
            user = this.userRepository.findByNick(commentRequestDto.getUserNick()).orElseThrow(UserNotFoundException::new);
            comment.setUserId(user.getId());
            commentUserResponseDto.setEmail(user.getEmail());
            commentUserResponseDto.setNick(user.getNick());
        }
        comment = this.commentRepository.save(comment);

        CommentResponseDto commentResponseDto = this.mapper.map(comment, CommentResponseDto.class);
        commentResponseDto.setUser(commentUserResponseDto);

        return commentResponseDto;
    }

    public CommentResponseDto deleteComment(long bookId, long commentId) {
        Comment comment = this.commentRepository.findByBookIdAndId(bookId, commentId)
                .orElseThrow(CommentNotFoundException::new);
        this.commentRepository.delete(comment);
        return this.mapper.map(comment, CommentResponseDto.class);
    }

    public Collection<UserCommentResponseDto> getComments(long userId) {
        return this.commentRepository.findByUserId(userId).stream()
                .map(comment -> this.mapper.map(comment, UserCommentResponseDto.class))
                .collect(Collectors.toList());
    }

}
