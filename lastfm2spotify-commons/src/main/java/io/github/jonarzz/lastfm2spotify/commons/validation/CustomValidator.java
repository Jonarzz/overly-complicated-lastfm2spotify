package io.github.jonarzz.lastfm2spotify.commons.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public abstract class CustomValidator<T> implements Validator {

    private Class<T> validatedClass;

    protected CustomValidator(Class<T> validatedClass) {
        this.validatedClass = validatedClass;
    }

    @Override
    public final boolean supports(Class<?> clazz) {
        return validatedClass == clazz;
    }

    @Override
    @SuppressWarnings("unchecked") // verified in the supports method
    public final void validate(Object object, Errors errors) {
        executeValidation((T) object, errors);
    }

    protected abstract void executeValidation(T object, Errors errors);

}
