package com.example.bookstore.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanWrapperImpl;

@RequiredArgsConstructor
public class PasswordValidator implements ConstraintValidator<FieldMatch, Object> {
    private String password;
    private String repeatPassword;

    public void initialize(FieldMatch fieldMatch) {
        this.password = fieldMatch.field()[0];
        this.repeatPassword = fieldMatch.field()[1];
    }

    @Override
    public boolean isValid(Object value,
                           ConstraintValidatorContext constraintValidatorContext) {
        Object password = new BeanWrapperImpl(value).getPropertyValue(this.password);
        Object repeatPassword = new BeanWrapperImpl(value).getPropertyValue(this.repeatPassword);
        return repeatPassword != null && repeatPassword.equals(password);
    }
}
