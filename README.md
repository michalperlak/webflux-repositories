# webflux - repositories

## Running
> ./gradlew bootRun 

## Running with docker
> ./gradlew build docker


> docker run -p 9090:9090 pl.michalperlak/repositories-app

## Running tests

Unit tests

> ./gradlew test

Integration tests

> ./gradlew integrationTest

End to end tests

> ./gradlew endToEndTest

Reports with tests results are generated in build/reports/tests

### Coverage results

Coverage data from tests runs are accumulated in build/jacoco/tests.exec

Generating coverage report from data file:

> ./gradlew jacocoTestReport

Report is generated in build/reports/jacoco directory

## Running static analysis

Checkstyle

> ./gradlew checkstyleMain

Findbugs

> ./gradlew findbugsMain

Reports are generated in build/reports directory

