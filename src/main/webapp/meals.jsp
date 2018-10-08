<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Meals</title>
    <style type="text/css">
        table {
            color: green;
        }

        .tbl_head {
            color: black;
        }

        tr[exceed=true] {
            color: red;
        }
    </style>
</head>
<body>
<h2>Meals</h2>
<h2><a href="meals?action=add">Add new meal</a></h2>
<table cellspacing="2" border="1" cellpadding="5" width="600">
    <tr class="tbl_head">
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
    </tr>
    <c:forEach items="${meals}" var="meal">
        <tr exceed="${meal.exceed}">
            <td><fmt:parseDate value="${ meal.dateTime }" pattern="yyyy-MM-dd'T'HH:mm" var="parsedDateTime"
                               type="both"/>
                <fmt:formatDate pattern="dd.MM.yyyy HH:mm" value="${ parsedDateTime }"/></td>
            <td>${meal.description}</td>
            <td>${meal.calories}</td>
            <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
            <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
