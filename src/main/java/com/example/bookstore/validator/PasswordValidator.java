package com.example.bookstore.validator;

import com.example.bookstore.model.User;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<FieldMatch, Object> {
    private final User user;
    private String password;
    private String repeatPassword;

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext constraintValidatorContext) {
        Object password = new BeanWrapperImpl(value).getPropertyValue(this.password);
        Object repeatPassword = new BeanWrapperImpl(value).getPropertyValue(this.repeatPassword);
        return repeatPassword != null && repeatPassword.equals(password);
    }
}
