package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {

    @Autowired
    private MealService mealService;

    @Autowired
    MessageSource messageSource;

    @GetMapping(value = "/i18n")
    @ResponseBody
    public String getMyAjaxMessage(@RequestParam("key") String key, HttpServletRequest request) {
        String[] args = {"common.deleted", "common.saved", "common.enabled", "common.disabled", "common.errorStatus"};
        return messageSource.getMessage(key, args, RequestContextUtils.getLocale(request));
    }

    @GetMapping("/")
    public String root() {
        return "redirect:meals";
    }

    @GetMapping("/users")
    public String users() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

    @GetMapping("/meals")
    public String meals(Model model) {
        return "meals";
    }
}
