package es.codeurjc.books.services;

import es.codeurjc.books.dtos.requests.CommentRequestDto;
import es.codeurjc.books.dtos.responses.CommentResponseDto;
import es.codeurjc.books.dtos.responses.UserCommentResponseDto;

import java.util.Collection;

public interface CommentService {

    CommentResponseDto addComment(long bookId, CommentRequestDto commentRequestDto);

    CommentResponseDto deleteComment(long bookId, long commentId);

    Collection<UserCommentResponseDto> getComments(long userId);

}
