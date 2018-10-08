package ru.javawebinar.topjava.service;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServiceImpl implements MealService {

    private MealRepository repository;
    private static final Logger log = getLogger(MealServiceImpl.class);

    public MealServiceImpl(MealRepository repository) {
        this.repository = repository;
    }

    @Override
    public void add(Meal meal) {
        repository.add(meal);
        log.debug("meal added");
    }

    @Override
    public void delete(int id) {
        repository.delete(id);
        log.debug("meal deleted");
    }

    @Override
    public void update(Meal meal) {
        repository.update(meal);
        log.debug("meal updated");
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return repository.getAll();
    }
}
