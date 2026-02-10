package ru.otus.homework.validator;


import org.springframework.validation.FieldError;
import org.springframework.validation.Validator;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.function.Function;

public abstract class BaseValidator implements Validator {

    protected <E extends RuntimeException> void checkErrors(Errors errors, Function<String, E> throwableFunction) {
        if (errors.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrorList = errors.getFieldErrors();
            for (FieldError error : fieldErrorList) {
                errorMessage.append(error.getField()).append(" - ").append(error.getDefaultMessage()).append(";");
            }
            throw throwableFunction.apply(errorMessage.toString());
        }
    }

}
