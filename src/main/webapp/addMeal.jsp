<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add meal</title>
</head>
<body>
<h1>Meal:</h1>
<form name="addMeal" action="meals" method="post">
    <input type="text" id="id" name="id" value="${meal.id}" hidden="true"><br>
    <label for="dateTime">Date&Time</label><br>
    <input type="datetime-local" id="dateTime" name="dateTime" value="${meal.dateTime}"><br>
    <label for="description">Description</label><br>
    <input type="text" id="description" name="description" value="${meal.description}"><br>
    <label for="calories">Calories</label><br>
    <input type="text" id="calories" name="calories" value="${meal.calories}"><br>
    <br>
    <input type="submit" id="save" name="save" value="save"/>
</form>
</body>
</html>
