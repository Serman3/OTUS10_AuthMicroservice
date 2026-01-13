package ru.otus.homework.web;

import feign.Response;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.otus.homework.api.client.JwtClient;
import ru.otus.homework.datasource.dao.UserAuthDao;
import ru.otus.homework.datasource.dto.UserDto;
import ru.otus.homework.dto.JwtAuthorizationRequest;
import ru.otus.homework.ex.UserNotCreatedException;
import ru.otus.homework.validation.UserRegistartionValidator;
import ru.otus.homework.dto.JwtAuthenticationResponse;
import ru.otus.homework.dto.JwtTokenRequest;
import ru.otus.homework.dto.RegistrationRequest;

import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@Tag(name = "Аутентификация")
public class AuthController {

    private final JwtClient jwtClient;
    private final UserAuthDao userAuthDao;
    private final UserRegistartionValidator userRegistartionValidator;
    private final ModelMapper modelMapper;

    @Autowired
    public AuthController(JwtClient jwtClient,
                          UserAuthDao userAuthDao,
                          UserRegistartionValidator userRegistartionValidator,
                          ModelMapper modelMapper) {
        this.jwtClient = jwtClient;
        this.userAuthDao = userAuthDao;
        this.userRegistartionValidator = userRegistartionValidator;
        this.modelMapper = modelMapper;
    }

    @Operation(summary = "Авторизоваться")
    @PostMapping("/authorize")
    public ResponseEntity<JwtAuthenticationResponse> authorize(@RequestBody JwtAuthorizationRequest jwtAuthorizationRequest) {
        return ResponseEntity.of(Optional.of(jwtClient.performToken(
                "Basic " + encodeToBase64(jwtAuthorizationRequest.getUsername() + ":" + jwtAuthorizationRequest.getPassword()),
                jwtAuthorizationRequest.getGameId()
        )));
    }

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest registrationRequest, BindingResult bindingResult) {
        userRegistartionValidator.validate(registrationRequest, bindingResult);
        userAuthDao.addUser(modelMapper.map(registrationRequest, UserDto.class));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Рефреш токена доступа")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody JwtTokenRequest jwtTokenRequest) {
        return ResponseEntity.of(Optional.of(jwtClient.refreshToken("Bearer " + jwtTokenRequest.getToken())));
    }

    @Operation(summary = "Разлогиниться")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody JwtTokenRequest jwtTokenRequest) {
        try (Response response = jwtClient.logOut("Bearer " + jwtTokenRequest.getToken())) {
            if (response != null && response.status() == HttpStatus.NO_CONTENT.value())
                return ResponseEntity.noContent().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(UserNotCreatedException exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<Map<String, String>> handleException(Throwable exception) {
        return new ResponseEntity<>(Map.of("ErrorMessage", exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    private String encodeToBase64(String credentials) {
        return Base64.getEncoder().encodeToString(credentials.getBytes());
    }

}
