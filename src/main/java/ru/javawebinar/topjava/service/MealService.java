package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealService {
    void add(Meal meal);

    void delete(int id);

    void update(Meal meal);

    Meal get(int id);

    List<Meal> getAll();
}
