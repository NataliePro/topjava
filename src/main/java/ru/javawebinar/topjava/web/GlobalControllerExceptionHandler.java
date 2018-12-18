package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@ControllerAdvice
public class GlobalControllerExceptionHandler extends AbstractExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ModelAndView duplicatedErrorHandler(HttpServletRequest req, Exception e, Locale locale) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        return setExceptionModelAndViewWithMessage(e, getExceptionMessage(e, locale));
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("Exception at request " + req.getRequestURL(), e);
        return setExceptionModelAndView(e);
    }

    private ModelAndView setExceptionModelAndViewWithMessage(Exception e, String msg) {
        ModelAndView mav = new ModelAndView("exception/exception");

        Throwable rootCause = ValidationUtil.getRootCause(e);

        if (msg == null) {
            msg = ValidationUtil.getMessage(rootCause);
        }

        mav.addObject("exception", rootCause);
        mav.addObject("message", msg);

        // Interceptor is not invoked, put userTo
        AuthorizedUser authorizedUser = SecurityUtil.safeGet();
        if (authorizedUser != null) {
            mav.addObject("userTo", authorizedUser.getUserTo());
        }
        return mav;
    }

    private ModelAndView setExceptionModelAndView(Exception e) {
        return setExceptionModelAndViewWithMessage(e, null);
    }
}
