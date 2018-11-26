CURL COMMANDS FOR WINDOWS

Get all meals:
curl "http://localhost:8080/topjava/rest/meals"

Get meal with id=100003:
curl "http://localhost:8080/topjava/rest/meals/100003"

Delete meal with id=100003:
-X DELETE "http://localhost:8080/topjava/rest/meals/100003"

Update meal with id=100003:
curl -H "Content-Type: application/json" -X PUT -d "{\"id\":100003,\"dateTime\":\"2018-11-13T18:35:00\",\"description\":\"new meal2\",\"calories\":300,\"user\":null}" http://localhost:8080/topjava/rest/meals/100003

Create meal:
curl -H "Content-Type: application/json" -X POST -d "{\"dateTime\":\"2018-11-13T18:35:00\",\"description\":\"new meal2\",\"calories\":300,\"user\":null}" http://localhost:8080/topjava/rest/meals

Filter meal:
curl "http://localhost:8080/topjava/rest/meals/filter?startDate=2015-05-31&startTime=20:00&endDate=2015-06-01&endTime=23:00"