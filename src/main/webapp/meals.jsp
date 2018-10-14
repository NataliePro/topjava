<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        .normal {
            color: green;
        }

        .exceeded {
            color: red;
        }
    </style>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <h2>Meals</h2>
    <form id="filterForm" method="post" action="meals">
        <table border="0" cellpadding="3" cellspacing="5">
            <tr align="left">
                <th>From date:</th>
                <th>From time:</th>
            </tr>
            <tr>
                <th><input type="date" id="startDate" name="startDate" value=${param.startDate}></th>
                <th><input type="time" id="startTime" name="startTime" value=${param.startTime}></th>
            </tr>
            <tr align="left">
                <th>To date:</th>
                <th>To time:</th>
            </tr>
            <tr>
                <th><input type="date" id="endDate" name="endDate" value=${param.endDate}></th>
                <th><input type="time" id="endTime" name="endTime" value=${param.endTime}></th>
            </tr>
        </table>
        <br>
        <button type="submit" name="filter" id="filter" value="filter">Filter</button>
        <button type="submit" name="cancel" id="cancel" value="cancel">Cancel</button>
    </form>
    <a href="meals?action=create">Add Meal</a>
    <hr/>
    <table border="1" cellpadding="8" cellspacing="0">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal">
            <jsp:useBean id="meal" scope="page" type="ru.javawebinar.topjava.to.MealWithExceed"/>
            <tr class="${meal.exceed ? 'exceeded' : 'normal'}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>

</section>
</body>
</html>