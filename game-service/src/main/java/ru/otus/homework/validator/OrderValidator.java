package ru.otus.homework.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import ru.otus.homework.ex.OrderParseException;
import ru.otus.homework.web.dto.OrderRequest;

@Component
public class OrderValidator extends BaseValidator {

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(OrderRequest.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        checkErrors(errors, OrderParseException::new);
    }
}
