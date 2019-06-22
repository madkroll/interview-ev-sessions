# interview-ev-sessions
Coding assignment: EV charging sessions RESTful API

## Setup
```
git clone git://github.com/madkroll/interview-ev-sessions.git
cd interview-ev-sessions
mvn clean install
```

## Validate installation
### Using curl
```
curl -v -d '{"stationId":"test-id"}' -H "Content-Type: application/json" -X POST "http://localhost:8080/chargingSessions"
```
### Using API tests
```
pushd ev-sessions-api-test
mvn clean test -DargLine='-DbaseUrl=http://localhost:8080'
popd
```