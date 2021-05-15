package es.codeurjc.mca.userms.controllers;

import es.codeurjc.mca.userms.dtos.requests.UpdateUserEmailRequestDto;
import es.codeurjc.mca.userms.dtos.requests.UserRequestDto;
import es.codeurjc.mca.userms.dtos.responses.CommentResponseDto;
import es.codeurjc.mca.userms.dtos.responses.UserResponseDto;
import es.codeurjc.mca.userms.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Get all users")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all users",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = UserResponseDto.class)))})})
    @GetMapping("/")
    public Collection<UserResponseDto> getUsers() {
        return this.userService.findAll();
    }

    @Operation(summary = "Get a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @GetMapping("/{userId}")
    public UserResponseDto getUser(@Parameter(description = "id of user to be searched")
                                   @PathVariable long userId) {
        return this.userService.findById(userId);
    }

    @Operation(summary = "Create a new user")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "User to be created", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserRequestDto.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created the user",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user attributes supplied",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "Already exists an user with same nick",
                    content = @Content)})
    @PostMapping("/")
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        return this.userService.save(userRequestDto);
    }

    @Operation(summary = "Updates user's email")
    @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Email to be updated", required = true,
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UpdateUserEmailRequestDto.class)))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User with updated email",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid email supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content)})
    @PatchMapping("/{userId}")
    public UserResponseDto updateUserEmail(@Parameter(description = "id of user to update the email")
                                           @PathVariable long userId,
                                           @Valid @RequestBody UpdateUserEmailRequestDto updateUserEmailRequestDto) {
        return this.userService.updateEmail(userId, updateUserEmailRequestDto);
    }

    @Operation(summary = "Deletes user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User deleted",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserResponseDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid format id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "User not found",
                    content = @Content),
            @ApiResponse(responseCode = "409", description = "User can't be deleted because has associated comments",
                    content = @Content)})
    @DeleteMapping("/{userId}")
    public UserResponseDto deleteUser(@Parameter(description = "id of user to be deleted") @PathVariable long userId) {
        return this.userService.delete(userId);
    }

    @Operation(summary = "Get all user's comments")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found all user's comments",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CommentResponseDto.class)))})})
    @GetMapping("/{userId}/comments")
    public Collection<CommentResponseDto> getUserComments(@Parameter(description = "id of user to get comments")
                                                              @PathVariable long userId) {
        return this.userService.getUserComments(userId);
    }

}
