package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.MAX_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.MIN_DATE;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredWithExcess;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserCaloriesPerDay;
import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class JspMealController {

    private MealService service;

    public JspMealController(MealService service) {
        this.service = service;
    }

    @PostMapping("/meals")
    public String save(@RequestParam("id") String id,
                       @RequestParam("dateTime") String strDateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories) {
        if (id.isEmpty()) {
            service.create(new Meal(null, LocalDateTime.parse(strDateTime), description, calories), authUserId());
        } else {
            service.update(new Meal(Integer.parseInt(id), LocalDateTime.parse(strDateTime), description, calories), authUserId());
        }
        return "redirect:/meals";
    }

    @GetMapping("/meals/update/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("meal", service.get(id, authUserId()));
        return "mealForm";
    }


    @GetMapping("/mealForm")
    public String create(HttpServletRequest request, Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/meals/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        service.delete(id, authUserId());
        return "redirect:/meals";
    }

    @GetMapping("/meals")
    public String getAll(Model model) {
        model.addAttribute("meals", getWithExcess(service.getAll(authUserId()), authUserCaloriesPerDay()));
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

        model.addAttribute("meals", getFilteredWithExcess(
                service.getBetweenDates(startDate, endDate, authUserId()),
                authUserCaloriesPerDay(), startTime, endTime));
        return "meals";
    }
}
