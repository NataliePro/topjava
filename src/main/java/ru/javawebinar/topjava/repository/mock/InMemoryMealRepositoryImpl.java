package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;
import static ru.javawebinar.topjava.util.MealsUtil.getFilteredSortedMealsFromMap;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        repository.put(10000, new HashMap<>());
        repository.put(10001, new HashMap<>());
        for (int i = 0; i < 3; i++) {
            save(MealsUtil.MEALS.get(i), 10000);
        }
        for (int i = 3; i < 6; i++) {
            save(MealsUtil.MEALS.get(i), 10001);
        }
    }

    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> userMealMap = repository.computeIfAbsent(userId, v -> new HashMap<>());
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMealMap.put(meal.getId(), meal);
            return meal;
        }
        Meal userMeal = userMealMap.get(meal.getId());
        if (userMeal == null) {
            return null;
        }
        userMealMap.replace(meal.getId(), meal);
        return meal;
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap != null) {
            Meal removedMeal = userMealMap.remove(id);
            return !(removedMeal == null);
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMealMap = repository.get(userId);
        if (userMealMap != null) {
            return userMealMap.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getFilteredSortedMealsFromMap(repository, userId, meal -> true);
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getFilteredSortedMealsFromMap(repository, userId, meal -> isBetween(meal.getDate(), startDate, endDate) && isBetween(meal.getTime(), startTime, endTime));
    }
}

