package ru.javawebinar.topjava.web;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static ru.javawebinar.topjava.util.MealsUtil.*;
import static ru.javawebinar.topjava.util.TimeUtil.*;

public class MealServlet extends HttpServlet {

    private MealRepository repository;
    private static String INSERT_OR_EDIT = "/addMeal.jsp";
    private static String LIST = "/meals.jsp";

    @Override
    public void init() throws ServletException {
        this.repository = new InMemoryMealRepositoryImpl();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("edit".equals(action) || "delete".equals(action)) {
            String id = req.getParameter("id");
            if ("edit".equals(action) && id != null && !id.isEmpty()) {
                req.setAttribute("meal", repository.get(Integer.parseInt(id)));
                req.getRequestDispatcher(INSERT_OR_EDIT).forward(req, resp);
            } else if ("delete".equals(action) && id != null && !id.isEmpty()) {
                repository.delete(Integer.parseInt(id));
                req.setAttribute("meals", getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
                resp.sendRedirect("meals");
            } else {
                resp.setContentType("text/html");
                PrintWriter pw = resp.getWriter();
                pw.println("<html><body>");
                pw.println("Invalid ID value!");
                pw.println("</body></html>");
                pw.close();
            }
        } else if ("add".equals(action)) {
            req.getRequestDispatcher(INSERT_OR_EDIT).forward(req, resp);
        } else {
            req.setAttribute("meals", getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
            req.getRequestDispatcher(LIST).forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String id = req.getParameter("id");
        String dateTime = req.getParameter("dateTime");
        String description = req.getParameter("description");
        String calories = req.getParameter("calories");
        repository.save(new Meal(!id.isEmpty() ? Integer.parseInt(id) : null, LocalDateTime.parse(dateTime, DATE_TIME_FORMATTER), description, Integer.parseInt(calories)));
        req.setAttribute("meals", getFilteredWithExceeded(repository.getAll(), LocalTime.MIN, LocalTime.MAX, CALORIES_PER_DAY));
        req.getRequestDispatcher(LIST).forward(req, resp);
    }
}
