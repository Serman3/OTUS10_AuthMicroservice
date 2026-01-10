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
import ru.otus.homework.ex.UserNotCreatedException;
import ru.otus.homework.validation.UserRegistartionValidator;
import ru.otus.homework.web.dto.JwtAuthenticationDto;
import ru.otus.homework.web.dto.JwtAuthorizationDto;
import ru.otus.homework.web.dto.RegistrationDto;

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

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/register")
    public ResponseEntity<JwtAuthenticationDto> register(@RequestBody @Valid RegistrationDto registrationDto, BindingResult bindingResult) {
        userRegistartionValidator.validate(registrationDto, bindingResult);
        userAuthDao.addUser(modelMapper.map(registrationDto, UserDto.class));
        return ResponseEntity.of(Optional.of(jwtClient.performToken("Basic " + encodeToBase64(registrationDto.getUsername() + ":" + registrationDto.getPassword()))));
    }

    @Operation(summary = "Рефреш токена доступа")
    @PostMapping("/refresh")
    public ResponseEntity<JwtAuthenticationDto> refresh(@RequestBody JwtAuthorizationDto jwtAuthorizationDto) {
        return ResponseEntity.of(Optional.of(jwtClient.refreshToken("Bearer " + jwtAuthorizationDto.getToken())));
    }

    @Operation(summary = "Разлогиниться")
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody JwtAuthorizationDto jwtAuthorizationDto) {
        try (Response response = jwtClient.logOut("Bearer " + jwtAuthorizationDto.getToken())) {
            if (response != null && response.status() == HttpStatus.NO_CONTENT.value()) return ResponseEntity.noContent().build();
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
