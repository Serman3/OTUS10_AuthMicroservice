package ru.otus.homework.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import ru.otus.homework.datasource.dao.UserAuthDao;
import ru.otus.homework.datasource.dto.UserDto;
import ru.otus.homework.ex.UserNotCreatedException;
import ru.otus.homework.dto.RegistrationRequest;

import java.util.List;
import java.util.Optional;

@Component
public class UserRegistartionValidator implements Validator {

    private final UserAuthDao userAuthDao;

    @Autowired
    public UserRegistartionValidator(UserAuthDao userAuthDao) {
        this.userAuthDao = userAuthDao;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RegistrationRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegistrationRequest registrationRequest = (RegistrationRequest) target;

        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrorList = errors.getFieldErrors();
            for (FieldError error : fieldErrorList) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }

        Optional<UserDto> userDtoOptional = userAuthDao.findUserByUsername(registrationRequest.getUsername());
        if (userDtoOptional.isPresent()) {
            throw new UserNotCreatedException("Такой пользователь уже существует");
        }
    }
}
