package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private Map<Integer, Meal> userMealMap = new ConcurrentHashMap<>();

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
        userMealMap = repository.get(userId);
        if (userMealMap != null) {
            if (meal.isNew()) {
                meal.setId(counter.incrementAndGet());
                userMealMap.put(meal.getId(), meal);
                repository.replace(userId, userMealMap);
                return meal;
            }
            Meal userMeal = getUserMealFromMap(userMealMap, meal.getId());
            if (userMeal == null) {
                return null;
            }
            userMealMap.replace(meal.getId(), meal);
            repository.replace(userId, userMealMap);
            return meal;
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        userMealMap = repository.get(userId);
        if (userMealMap != null) {
            Meal removedMeal = userMealMap.remove(id);
            if (removedMeal == null) {
                return false;
            }
            repository.replace(userId, userMealMap);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id, int userId) {
        userMealMap = repository.get(userId);
        if (userMealMap != null) {
            return getUserMealFromMap(userMealMap, id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        userMealMap = repository.get(userId);
        if (userMealMap != null) {
            return userMealMap.values()
                    .stream()
                    .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Meal> getBetween(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        return getAll(userId).stream()
                .filter(meal -> isBetween(meal.getDate(), startDate, endDate) && isBetween(meal.getTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getDateTime).reversed())
                .collect(Collectors.toList());
    }

    private Meal getUserMealFromMap(Map<Integer, Meal> userMealMap, int id) {
        return userMealMap
                .values()
                .stream()
                .filter(meal -> meal.getId() == id)
                .findFirst()
                .orElse(null);
    }
}

