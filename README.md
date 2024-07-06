# Large File Reader
This is a simple program that reads a large file and makes the content available for other APIs to use it for presentation.

## How to run the program
```
./gradlew clean build

java -jar build/libs/rlf-1.0-SNAPSHOT.jar example-file.csv
```

## APIs
There is only one API available: an HTTP endpoint accepting GET requests:
`http://localhost:8000/<city>`
