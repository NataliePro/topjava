package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.TimeUtil.*;

public class MealServlet extends HttpServlet {

    private MealService service;
    private static String INSERT_OR_EDIT = "/addMeal.jsp";
    private static String LIST = "/meals.jsp";

    @Override
    public void init() throws ServletException {
        super.init();
        this.service = new MealServiceImpl(new InMemoryMealRepositoryImpl());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String forward = "";
        String action = req.getParameter("action");
        String id = req.getParameter("id");
        if (id != null && !id.isEmpty()) {
            if ("edit".equals(action)) {
                req.setAttribute("meal", service.get(Integer.parseInt(id)));
                forward = INSERT_OR_EDIT;
            } else if ("delete".equals(action)) {
                service.delete(Integer.parseInt(id));
                req.setAttribute("meals", getFilteredWithExceeded(service.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                forward = LIST;
            }
        } else if ("add".equals(action)) {
            forward = INSERT_OR_EDIT;
        } else {
            req.setAttribute("meals", getFilteredWithExceeded(service.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            forward = LIST;

        }
        req.getRequestDispatcher(forward).forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String dateTime = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");
        if (!id.isEmpty()) {
            Meal mealUpdated = service.get(Integer.parseInt(id));
            mealUpdated.setDateTime(LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER));
            mealUpdated.setDescription(description);
            mealUpdated.setCalories(Integer.parseInt(calories));
            service.update(mealUpdated);
        } else {
            service.add(new Meal(LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER), description, Integer.parseInt(calories)));
        }
        req.setAttribute("meals", getFilteredWithExceeded(service.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST).forward(req, resp);
    }
}
