package ru.javawebinar.topjava.service.user;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.AbstractUserServiceTest;

import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaUserServiceTest extends AbstractUserServiceTest {

    @Test
    public void getWithMealTest() {
        User userWithMeal = service.getWithMeal(ADMIN_ID);
        assertMatch(userWithMeal, ADMIN);
        MealTestData.assertMatch(userWithMeal.getMeals(), MealTestData.ADMIN_MEAL2, MealTestData.ADMIN_MEAL1);
    }
}
