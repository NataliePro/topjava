package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.util.ValidationUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.util.exception.IllegalRequestDataException;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

import static ru.javawebinar.topjava.util.Util.setExceptionModelAndViewWithMessage;
import static ru.javawebinar.topjava.util.exception.ErrorType.*;

@RestControllerAdvice(annotations = RestController.class)
@Order(Ordered.HIGHEST_PRECEDENCE + 5)
public class ExceptionInfoHandler {
    private static Logger log = LoggerFactory.getLogger(ExceptionInfoHandler.class);

    @Autowired
    private MessageSource messageSource;

    public String getExceptionMessage(Exception e, Locale locale) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        String msg = ValidationUtil.getMessage(rootCause);
        if (msg.contains("users_unique_email_idx")) {
            return messageSource.getMessage("user.duplicatedEmail", null, locale);
        } else if (msg.contains("meals_unique_user_datetime_idx")) {
            return messageSource.getMessage("meal.duplicatedDateTime", null, locale);
        }
        return msg;
    }

    //  http://stackoverflow.com/a/22358422/548473
    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotFoundException.class)
    public ErrorInfo handleError(HttpServletRequest req, NotFoundException e) {
        return logAndGetErrorInfo(req, e, false, DATA_NOT_FOUND);
    }

    @ResponseStatus(value = HttpStatus.CONFLICT)  // 409
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ErrorInfo conflict(HttpServletRequest req, DataIntegrityViolationException e, Locale locale) {
        return logAndGetErrorInfoWithMessage(req, e, true, DATA_ERROR, getExceptionMessage(e, locale));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorInfo restValidation(HttpServletRequest req, Exception e, MethodArgumentNotValidException error) {
        return logAndGetErrorInfoWithMessage(req, e, true, VALIDATION_ERROR, ValidationUtil.getErrorMessageFromBidingResult(error.getBindingResult()));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler(BindException.class)
    public ErrorInfo bindingException(HttpServletRequest req, BindException e) {
        return logAndGetErrorInfoWithMessage(req, e, true, VALIDATION_ERROR, ValidationUtil.getErrorMessageFromBidingResult(e.getBindingResult()));
    }

    @ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)  // 422
    @ExceptionHandler({IllegalRequestDataException.class, MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public ErrorInfo illegalRequestDataError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, false, VALIDATION_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView duplicatedErrorHandler(HttpServletRequest req, Exception e, Locale locale) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        return setExceptionModelAndViewWithMessage(e, getExceptionMessage(e, locale));
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorInfo handleError(HttpServletRequest req, Exception e) {
        return logAndGetErrorInfo(req, e, true, APP_ERROR);
    }

    //    https://stackoverflow.com/questions/538870/should-private-helper-methods-be-static-if-they-can-be-static
    private static ErrorInfo logAndGetErrorInfo(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType
    ) {
        return logAndGetErrorInfoWithMessage(req, e, logException, errorType, null);
    }

    private static ErrorInfo logAndGetErrorInfoWithMessage(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String errorMessage) {
        Throwable rootCause = ValidationUtil.getRootCause(e);
        String msg = errorMessage != null ? errorMessage : ValidationUtil.getMessage(rootCause);
        if (logException) {
            log.error(errorType + " at request " + req.getRequestURL(), rootCause);
        } else {
            log.warn("{} at request  {}: {}", errorType, req.getRequestURL(), rootCause.toString());
        }
        return new ErrorInfo(req.getRequestURL(), errorType, msg);
    }
}