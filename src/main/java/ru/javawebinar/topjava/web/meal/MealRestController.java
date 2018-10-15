package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.web.SecurityUtil.*;
import static ru.javawebinar.topjava.util.MealsUtil.*;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        return service.create(meal, authUserId());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, authUserId());
    }

    public void delete(int id) {
        service.delete(id, authUserId());
    }

    public List<MealWithExceed> getAllBetween(LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        startDate = startDate == null ? LocalDate.MIN : startDate;
        endDate = endDate == null ? LocalDate.MAX : endDate;
        startTime = startTime == null ? LocalTime.MIN : startTime;
        endTime = endTime == null ? LocalTime.MAX : endTime;
        Map<LocalDate, Integer> calPerDayMap = service.getAll(authUserId())
                .stream()
                .collect(Collectors.groupingBy(Meal::getDate, Collectors.summingInt(Meal::getCalories)));
        return service.getAllBetween(authUserId(), startDate, endDate, startTime, endTime)
                .stream()
                .map(meal -> createWithExceed(meal, calPerDayMap.get(meal.getDate()) > authUserCaloriesPerDay()))
                .collect(Collectors.toList());
    }

    public List<MealWithExceed> getAll() {
        return getWithExceeded(service.getAll(authUserId()), authUserCaloriesPerDay());
    }
}