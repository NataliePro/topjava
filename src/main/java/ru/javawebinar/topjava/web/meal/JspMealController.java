package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.SecurityUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.MAX_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.MIN_DATE;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExcess;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    public JspMealController(MealService service) {
        super(service);
    }

    @PostMapping
    public String save(@RequestParam("id") String strId,
                       @RequestParam("dateTime") String strDateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories) {
        if (strId.isEmpty()) {
            int userId = authUserId();
            Meal meal = new Meal(null, LocalDateTime.parse(strDateTime), description, calories);
            checkNew(meal);
            log.info("create {} for user {}", meal, userId);
            service.create(meal, userId);
        } else {
            int userId = SecurityUtil.authUserId();
            int id = Integer.parseInt(strId);
            Meal meal = new Meal(id, LocalDateTime.parse(strDateTime), description, calories);
            assureIdConsistent(meal, id);
            log.info("update {} for user {}", meal, userId);
            service.update(meal, userId);
        }
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("meal", service.get(id, authUserId()));
        return "mealForm";
    }


    @GetMapping("/mealForm")
    public String create(HttpServletRequest request, Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        int userId = authUserId();
        log.info("delete meal {} for user {}", id, userId);
        service.delete(id, userId);
        return "redirect:/meals";
    }

    @GetMapping
    public String getAll(Model model) {
        int userId = authUserId();
        log.info("getAll for user {}", userId);
        model.addAttribute("meals", getWithExcess(service.getAll(userId), authUserCaloriesPerDay()));
        return "meals";
    }

    @PostMapping("/filter")
    public String filter(Model model,
                         @RequestParam("startDate") String startDateStr,
                         @RequestParam("endDate") String endDateStr,
                         @RequestParam("startTime") String startTimeStr,
                         @RequestParam("endTime") String endTimeStr) {

        LocalDate startDate = startDateStr.isEmpty() ? MIN_DATE : LocalDate.parse(startDateStr);
        LocalDate endDate = endDateStr.isEmpty() ? MAX_DATE : LocalDate.parse(endDateStr);
        LocalTime startTime = startTimeStr.isEmpty() ? LocalTime.MIN : LocalTime.parse(startTimeStr);
        LocalTime endTime = endTimeStr.isEmpty() ? LocalTime.MAX : LocalTime.parse(endTimeStr);

        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, endDate, startTime, endTime, userId);

        model.addAttribute("meals", getFilteredWithExcess(
                service.getBetweenDates(startDate, endDate, userId),
                authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
