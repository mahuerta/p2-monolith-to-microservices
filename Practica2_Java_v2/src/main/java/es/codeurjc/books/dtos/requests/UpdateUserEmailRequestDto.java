package es.codeurjc.books.dtos.requests;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class UpdateUserEmailRequestDto {

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Invalid email")
    private String email;

}
