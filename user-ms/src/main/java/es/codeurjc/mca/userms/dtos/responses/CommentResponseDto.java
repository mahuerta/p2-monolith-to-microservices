package es.codeurjc.mca.userms.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommentResponseDto {

    private Long id;
    private String comment;
    private float score;
    private Long bookId;

}
