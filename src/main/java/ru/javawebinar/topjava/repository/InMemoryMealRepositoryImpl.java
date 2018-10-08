package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {

    private static Map<Integer, Meal> meals = new ConcurrentHashMap<>();
    private static AtomicInteger counter = new AtomicInteger(0);

    static {
        getMealsList().forEach(meal -> meals.put(meal.getId(), meal));
    }

    @Override
    public void add(Meal meal) {
        if (meal.getId() == null) {
            meal.setId(counter.incrementAndGet());
        }
        meals.put(meal.getId(), meal);
    }

    @Override
    public void delete(int id) {
        meals.remove(id);
    }

    @Override
    public void update(Meal meal) {
        meals.put(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        return meals.get(id);
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> list = new ArrayList<>();
        list.addAll(meals.values());
        return list;
    }

    public static List<Meal> getMealsList() {
        return Arrays.asList(
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new Meal(counter.incrementAndGet(), LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
    }
}
