package ru.javawebinar.topjava.service.user;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.Collections;
import java.util.Date;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMealTest() {
        User userWithMeal = service.getWithMeal(USER_ID);
        assertMatch(userWithMeal, USER);
        MealTestData.assertMatch(userWithMeal.getMeals(), MealTestData.MEALS);
    }

    @Test
    public void getWithNotFoundMealTest() {
        User newUser = new User(null, "New", "new@gmail.com", "newPass", 1555, false, new Date(), Collections.singleton(Role.ROLE_USER));
        User created = service.create(newUser);
        newUser.setId(created.getId());
        User userWithMeal = service.getWithMeal(created.getId());
        assertMatch(userWithMeal, newUser);
        Assert.assertTrue(userWithMeal.getMeals().isEmpty());
    }

    @Test
    public void getWithMealNotFoundTest() {
        thrown.expect(NotFoundException.class);
        service.getWithMeal(123);
    }
}
