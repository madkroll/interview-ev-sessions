# interview-ev-sessions
Coding assignment: EV charging sessions RESTful API

## Used frameworks:
- Maven
- Spring Boot
- Lombok
- Karate - for API testing (https://github.com/intuit/karate)

## Setup
```
git clone git://github.com/madkroll/interview-ev-sessions.git
cd interview-ev-sessions
mvn clean package
```

## Validate installation
### Using curl
```
# submit new session
curl -v -d '{"stationId":"test-id"}' -H "Content-Type: application/json" -X POST "http://localhost:8080/chargingSessions"

# finish existent session
curl -v -X PUT "http://localhost:8080/chargingSessions/${SESSION_ID}"

# list all stored sessions
curl -v -X GET "http://localhost:8080/chargingSessions"

# request last minute summary
curl -v -X GET "http://localhost:8080/chargingSessions/summary"
```
### Using API tests
```
pushd ev-sessions-api-test
mvn clean test -DargLine='-DbaseUrl=http://localhost:8080'
popd
```