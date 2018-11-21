package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import static ru.javawebinar.topjava.util.DateTimeUtil.MAX_DATE;
import static ru.javawebinar.topjava.util.DateTimeUtil.MIN_DATE;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @PostMapping
    public String save(@RequestParam("id") String strId,
                       @RequestParam("dateTime") String strDateTime,
                       @RequestParam("description") String description,
                       @RequestParam("calories") int calories) {
        Integer id = strId.isEmpty() ? null : Integer.parseInt(strId);
        Meal meal = new Meal(id, LocalDateTime.parse(strDateTime), description, calories);
        if (strId.isEmpty()) {
            create(meal);
        } else {
            update(meal, id);
        }
        return "redirect:/meals";
    }

    @GetMapping("/update/{id}")
    public String update(@PathVariable("id") int id, Model model) {
        model.addAttribute("meal", get(id));
        return "mealForm";
    }

    @GetMapping("/mealForm")
    public String create(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "mealForm";
    }

    @GetMapping("/delete/{id}")
    public String deleteMeal(@PathVariable("id") int id) {
        delete(id);
        return "redirect:/meals";
    }

    @GetMapping
    public String getAll(Model model) {
        model.addAttribute("meals", getAll());
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

        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }
}
