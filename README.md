# bankaccount project

This project uses Quarkus, vertx eventbus.

For testing purposes: RestAssured, Spock, JUnit5

The solution has been faced considering DDD and Hexagonal Architecture. Also, the intention is to provide a non-blocking solution 
by using command and event bus techniques.

Saga pattern is being used in order to coordinate money transfer transaction.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```
./mvnw quarkus:dev
```
This will start the application listening on `http://0.0.0.0:8080`
## Packaging and running the application

The application is packageable using `./mvnw package`.
It produces the executable `bankaccount-1.0-SNAPSHOT-runner.jar` file in `/target` directory. (already on place)
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/lib` directory. _über-jar_ is also provided as `bankaccount-1.0-SNAPSHOT.jar.original`

The application is now runnable using `java -jar target/bankaccount-1.0-SNAPSHOT-runner.jar`.

## Using the application
Either running `bankaccount-1.0-SNAPSHOT-runner.jar` or after executing `./mvnw quarkus:dev` you can go to swagger-ui in order to check existing rest API specification
```
http://0.0.0.0:8080/swagger-ui/
```
You can use `POST` endpoints in order to create either accounts or money transfers. 
As this has been tackle by in a non-blocking way (fire and forget), you should access to `GET` endpoints in order to check final status.

You can test all endpoints by using with the above mentioned `http://0.0.0.0:8080/swagger-ui/`

