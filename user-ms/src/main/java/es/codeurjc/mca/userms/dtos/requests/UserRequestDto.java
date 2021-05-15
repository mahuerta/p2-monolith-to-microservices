package es.codeurjc.mca.userms.dtos.requests;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class UserRequestDto {

    @NotBlank(message = "Nick is mandatory")
    private String nick;
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email")
    private String email;

}
