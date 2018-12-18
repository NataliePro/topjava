package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.Locale;

public abstract class AbstractExceptionHandler {
    @Autowired
    private MessageSource messageSource;

    public String getExceptionMessage(Exception e, Locale locale){
        Throwable rootCause = ValidationUtil.getRootCause(e);
        String msg = ValidationUtil.getMessage(rootCause);
        if (msg.contains("users_unique_email_idx")){
            return messageSource.getMessage("user.duplicatedEmail", null, locale);
        }else if (msg.contains("meals_unique_user_datetime_idx")){
            return messageSource.getMessage("meal.duplicatedDateTime", null, locale);
        }
        return msg;
    }
}
