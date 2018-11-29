<ins>**CURL COMMANDS FOR WINDOWS**</ins>
<ul>
<b>Get all meals:</b><br>
curl "http://localhost:8080/topjava/rest/meals"</li>

<b>Get meal with id=100003:</b><br>
curl "http://localhost:8080/topjava/rest/meals/100003"</li>

<b>Delete meal with id=100003:</b><br>
-X DELETE "http://localhost:8080/topjava/rest/meals/100003"</li>

<b>Update meal with id=100003:</b><br>
curl -H "Content-Type: application/json" -X PUT -d "{\"id\":100003,\"dateTime\":\"2018-11-13T18:35:00\",\"description\":\"new meal2\",\"calories\":300,\"user\":null}" http://localhost:8080/topjava/rest/meals/100003</li>

<b>Create meal:</b><br>
curl -H "Content-Type: application/json" -X POST -d "{\"dateTime\":\"2018-11-13T18:35:00\",\"description\":\"new meal2\",\"calories\":300,\"user\":null}" http://localhost:8080/topjava/rest/meals</li>

<b>Filter meal:</b><br>
curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-31&startTime=20:00&endDate=2015-06-01&endTime=23:00"</li>
</ul>

<b>Admin: get all users:</b><br>
curl "http://localhost:8080/topjava/rest/admin/users"</li>

<b>Admin: get user with id=100000:</b><br>
curl "http://localhost:8080/topjava/rest/admin/users/100000"</li>

<b>Admin: get user by email=user@yandex.ru:</b><br>
curl "http://localhost:8080/topjava/rest/admin/users/by?email=user@yandex.ru"</li>

<b>Admin: delete user with id=100000:</b><br>
-X DELETE "http://localhost:8080/topjava/rest/admin/users/100000"</li>

<b>Admin: update user with id=100000:</b><br>
curl -H "Content-Type: application/json" -X PUT -d "{\"name\": \"Update user\",\"email\": \"update@yandex.ru\",\"password\": \"passwordUpdate\",\"roles\": [\"ROLE_USER\"]}" http://localhost:8080/topjava/rest/admin/users/100000</li>

<b>Admin: create user:</b><br>
curl -H "Content-Type: application/json" -X POST -d "{\"name\": \"New2\",\"email\": \"new2@yandex.ru\",\"password\": \"passwordNew\",\"roles\": [\"ROLE_USER\"]}" http://localhost:8080/topjava/rest/admin/users</li>

<b>Profile: get user:</b><br>
curl "http://localhost:8080/topjava/rest/profile"</li>

<b>Profile: delete user:</b><br>
-X DELETE "http://localhost:8080/topjava/rest/profile"</li>

<b>Profile: update user:</b><br>
curl -H "Content-Type: application/json" -X PUT -d "{\"name\": \"Update user\",\"email\": \"update@yandex.ru\",\"password\": \"passwordUpdate\",\"roles\": [\"ROLE_USER\"]}" http://localhost:8080/topjava/rest/profile</li>
