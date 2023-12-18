package com.shopping.online.validations;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;

public class ValidationDTO {

    public static String getMessageError(BindingResult result) {
        List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
        return errorMessages.toString();
    }
}
