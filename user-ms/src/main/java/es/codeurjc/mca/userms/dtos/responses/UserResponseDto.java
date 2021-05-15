package es.codeurjc.mca.userms.dtos.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponseDto {

    private Long id;
    private String nick;
    private String email;

}
