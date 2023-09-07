# my-quarkus-project

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: https://quarkus.io/ .

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:
```shell script
mvn quarkus:dev
```

## To run tests 
You can run tests using:
```shell script
mvn clean verify
```


## Building and running the application

The application can be packaged using:
```shell script
mvn clean package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

## Use docker image 
To pull the container and run it use command: 
```shell script
docker compose -f ./docker-compose-start-project.yaml up -d
```

To stop the images:
```shell script
docker compose -f ./docker-compose-start-project.yaml down
```
