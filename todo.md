- check if guava is needed
- multiple stations
- validate app runs on Java 8
- exception handling - including 500
- adjust logging level

## Documentation

## Possible issues
- get and put operations are not atomic, so possible race condition (two threads are competing to put different data).
However business value in setting synchronization around it is not clear.