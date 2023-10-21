package com.example.taskmanagerapi.helper;

import com.example.taskmanagerapi.domain.enumerations.MessageCode;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
public class MessageHelper {
    private final MessageSource messageSource;
    public MessageHelper(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
    public String getMessage(MessageCode code) {
        return messageSource.getMessage(code.getCode(), null, LocaleContextHolder.getLocale());
    }
}
