package ru.otus.homework.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.otus.homework.datasource.dao.UserAuthDao;
import ru.otus.homework.datasource.dto.UserDto;
import ru.otus.homework.ex.UserNotCreatedException;
import ru.otus.homework.dto.RegistrationRequest;
import ru.otus.homework.validator.BaseValidator;

import java.util.Optional;

@Component
public class UserRegistartionValidator extends BaseValidator {

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

        checkErrors(errors, UserNotCreatedException::new);

        Optional<UserDto> userDtoOptional = userAuthDao.findUserByUsername(registrationRequest.getUsername());
        if (userDtoOptional.isPresent()) {
            throw new UserNotCreatedException("Такой пользователь уже существует");
        }
    }
}
