package ru.javawebinar.topjava.service.meal;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.AbstractMealServiceTest;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(DATAJPA)
public class DataJpaMealServiceTest extends AbstractMealServiceTest {

    @Test
    public void getWithUser() throws Exception {
        Meal actual = service.getWithUser(ADMIN_MEAL_ID, ADMIN_ID);
        UserTestData.assertMatch(actual.getUser(), ADMIN);
        assertMatch(actual, ADMIN_MEAL1);
    }

    @Test
    public void getWithNotFoundUser() throws Exception {
        Assert.assertNull(service.getWithUser(ADMIN_MEAL_ID, USER_ID));
    }

}
