package ru.javawebinar.topjava.web;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.jupiter.api.Test;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEALS;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;
import static ru.javawebinar.topjava.util.MealsUtil.getWithExcess;

class RootControllerTest extends AbstractControllerTest {

    @Test
    void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    void testMeals() throws Exception {
        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", new MealToMatcher(MEALS)));
    }

    private class MealToMatcher extends BaseMatcher<MealTo> {

        private List<MealTo> expected;

        public MealToMatcher(List<Meal> meals) {
            this.expected = getWithExcess(meals, SecurityUtil.authUserCaloriesPerDay());
        }

        @Override
        public boolean matches(Object item) {
            List<MealTo> actual = ((List<MealTo>) item);
            if (expected.size() != actual.size()) {
                return false;
            }
            for (int i = 0; i < actual.size(); i++) {
                if (!actual.get(i).getDescription().equals(expected.get(i).getDescription())
                        || actual.get(i).getCalories() != expected.get(i).getCalories()
                        || !actual.get(i).getDateTime().equals(expected.get(i).getDateTime())
                        || actual.get(i).isExcess() != expected.get(i).isExcess()) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("MEALS is not equal");
        }
    }

}