# interview-ev-sessions
Coding assignment: EV charging sessions RESTful API

## Used frameworks:
- Maven
- Spring Boot
- Lombok
- Guava (immutable collections only)
- Karate - for API testing (https://github.com/intuit/karate)
- JUnit
- Mockito
- AssertJ

## Setup
```
git clone git://github.com/madkroll/interview-ev-sessions.git
cd interview-ev-sessions
mvn clean package

# make sure java 8 is set as default
java -version

# run as a java process:
java -jar ./ev-sessions-service/target/ev-sessions-service-1.0-SNAPSHOT.jar
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

## Possible storage implementations
Things I considered:
- I found it is not a good idea to write my own custom thread-safe and at the same time well-performed implementation of data structure.
So focused mostly on finding good candidates to use. 
- replacing atomically existent session items by new session items with updated state - enforces immutability
and thread-safety on session object level
- using atomic read/insert operations enforces thread-safety on data structure level
- updates are distributed across different time moments, so even worst case for summary scenario
in relatively short run duration already means processing sub-selection, not the whole data set.
As result, it is more important to quickly navigate to proper sub-selection than iterating over this selection itself.

### Time Complexity Requirements
- submit: O(log n)
- finish: O(log n)
- list-all: O(n)
- summary: O(log n)

### ConcurrentHashMap (branch: master)
- submit session: O(1)
- finish session: O(1)
- list all sessions: O(n)
- summary: O(n) <- breaking requirements

Most basic implementation - idea is to use concurrent hash map implementation and avoid of using non-atomic operations.
It has very good performance for all read/insert operations, but ruins "summary" scenario cause it's not a sorted collection.
So no such meaning like "younger elements".

#### Advantages
- thread-safe
- single data structure to store everything, no need to synchronize modifications on different collections
- mostly non-blocking read-write operations
- constant time for single read/insert operation

#### Downsides
- filtering by any other field apart of one used as a key turns into O(n) complexity
- ignores the fact that only updates for last minute are taken to build summary

### ConcurrentSkipListSet (branch: master, class: SkipListDemo | branch: feature/skip-list-optimization)
According to documentation this collection provides such complexity:
- submit: O(log n)
- finish: O(log n)
- list-all: O(n)
- summary: O(log n)

The way of using Skip-List collection is demonstrated in class: SkipListDemo.
Please take a look at it.
Idea is that we always insert new session events (both, update and delete), so no need to replace existent record.
So it's thread-safe and consistent.
Then summary operation is to take last-minute records and reduce their information down to summary object.
And listing is about removing update events for already finished sessions from result.

I can use thread-safe implementations available in Java Collection Framework - ConcurrentSkipListMap or ConcurrentSkipListSet.
Such collections perfectly solve "summary" scenario (last-minute-records) by making sub-selection of already sorted data within a single method.
Read/insert operations by key used for sorting are O(log n). Such collection is thread-safe.

Version published under this branch is draft actually: feature/skip-list-optimization
More proper way to use it you can find in PoC class: master, SkipListDemo.java

#### Advantages:
- collection itself is thread-safe
- O(log n) complexity for insert/read operations, including building summary
- possibility to build a sub-selection as a snapshot and continue working with it
while running other read/write operations in parallel