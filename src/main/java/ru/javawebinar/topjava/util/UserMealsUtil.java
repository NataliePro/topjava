package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.TimeUtil.isBetween;

public class UserMealsUtil {

    private static Map<LocalDate, Integer> calPerDayMap = new HashMap<>();

    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceededByCycle(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        setCalPerDayMap(mealList);
        List<UserMealWithExceed> filteredList = new ArrayList<>();
        mealList.forEach(userMeal -> {
            if (isBetween(userMeal.getTime(),startTime,endTime)){
                filteredList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), calPerDayMap.getOrDefault(userMeal.getDate(),-1) > caloriesPerDay));
            }
        });
        return filteredList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        setCalPerDayMap(mealList);
        return mealList.stream()
                .filter(userMeal -> isBetween(userMeal.getTime(), startTime, endTime))
                .map(userMeal -> new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), calPerDayMap.getOrDefault(userMeal.getDate(),-1) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static void setCalPerDayMap(List<UserMeal> mealList) {
        calPerDayMap.clear();
        mealList.forEach(userMeal -> calPerDayMap.merge(userMeal.getDate(), userMeal.getCalories(), (oldVal, newVal) -> oldVal + newVal));
    }
}
