package ru.javawebinar.topjava.util;

import org.springframework.web.servlet.ModelAndView;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.web.SecurityUtil;

public class Util {

    private Util() {
    }

    public static <T extends Comparable<? super T>> boolean isBetween(T value, T start, T end) {
        return value.compareTo(start) >= 0 && value.compareTo(end) <= 0;
    }

    public static <T> T orElse(T value, T defaultValue) {
        return value == null ? defaultValue : value;
    }

    public static ModelAndView setExceptionModelAndViewWithMessage(Exception e, String msg) {
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
}
