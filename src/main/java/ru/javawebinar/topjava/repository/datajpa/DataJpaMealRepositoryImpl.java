package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class DataJpaMealRepositoryImpl implements MealRepository {

    private CrudMealRepository crudMealRepository;
    private CrudUserRepository crudUserRepository;

    public DataJpaMealRepositoryImpl(CrudMealRepository crudRepository, CrudUserRepository crudUserRepository) {
        this.crudMealRepository = crudRepository;
        this.crudUserRepository = crudUserRepository;
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal != null) {
            meal.setUser(crudUserRepository.getOne(userId));
            if (meal.isNew()) {
                return crudMealRepository.save(meal);
            } else {
                return crudMealRepository.update(meal.getDateTime(), meal.getDescription(),
                        meal.getCalories(), meal.getId(), userId) != 0 ? meal : null;
            }
        }
        return null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return crudMealRepository.delete(id, userId) != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        return crudMealRepository.get(id, userId);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return crudMealRepository.getAll(userId);
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        return crudMealRepository.getAllBetween(startDate, endDate, userId);
    }

    @Override
    public Meal getWithUser(int id, int userId) {
        return crudMealRepository.getWithUser(id, userId);
    }
}
