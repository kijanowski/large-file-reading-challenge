# Large File Reader
This is a simple program that reads a large file and makes the content available for other APIs to use it for presentation.

## How to run the program
The application requires Java 21 to compile and run. 
```
./gradlew clean build

java -jar build/libs/rlf-1.0-SNAPSHOT.jar example-file.csv
```

## APIs
There is only one API available: an HTTP endpoint accepting GET requests:
`http://localhost:8000/<city>`

# Things to consider
- larger buffer size for reading the file
- multithreaded reading of the file, if underlying (file) system supports it; would require further perf tests to see if it's worth it
- include Lombok for @RequiredArgsConstructor and @Slf4j, to avoid string concatenation
- smarter validation logic in `LineParser`
