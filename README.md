# Reactive Spring Sample Application

## Motivation
This application provides a sample code to provide an introduction to building Reactive Spring applications with REST API interface.
The main focus point is to get familiar with `Mono<T>` & `Flux<T>` asynchronous endpoint architecture as well as usage of function composition with *"dot"* notation.
The second point is to provide a soft guideline to writing test cases for asynchronous data streams.

## Supported deployment platforms

| Platform | Architecture | Deployable | Runnable | Remarks |
|----------|--------------|------------|----------|---------|
| Local environment | x64| Yes | Yes |  |
| Kubernetes | x64 | Yes | Yes |  |
| OpenShift | x64 | ? | ? | To be provided |

## Dependencies

- Java 8+
- MongoDB (optional only for DB related API endpoints & tests)
- Docker (optional only building container images)
    - local - for local builds
    - external - docker host set by environment variable `DOCKER_HOST`
- Helm v3 (optional only for deployment to Kubernetes or OpenShift)
  
> ### Note
> `DOCKER_HOST` environment variable uses a following formats:  
> - `tcp://docker-host:2375`  
> - `unix:///var/run/docker.sock`  
> - `npipe:///./pipe/docker_engine`  
> - `ssh://docker-host:22`

## Building
Full build with tests cases
```bash
mvnw package
```
Full build without running test cases
```bash
mvnw package -DskipTests
```

## Usage
```bash
mvnw spring-boot:run
```
API endpoints will be available at [localhost:8080](https://localhost:8080/) by default.

## Configuration
Application uses a configuration stored at `src/main/resources/application.yaml`.

## Deployment
This step is purely optional and needed only if you intend to deploy this application to containerized environment e.g. Docker, Kubernetes, OpenShift...
1. Build container image
```bash
mvnw spring-boot:build-image
```
2. After successful build your image will be called `hello0:0.0.2-SNAPSHOT`. You may want to tag your image respectively to your image artifact repository convention.
```bash
docker tag hello0:0.0.2-SNAPSHOT registry.host/user-name/hello0:0.0.2-SNAPSHOT
```
3. Push image into artifact repository
```bash
docker push registry.host/user-name/hello0:0.0.2-SNAPSHOT
```
4. Further steps depend on the container environment used
### Docker
By far this the easiest way to run your application in containerized environment. However, this is only viable for development and testing and certainly not for production!
```bash
docker run --rm -p 8080:8080 hello0:0.0.2-SNAPSHOT
```
### Kubernetes
```bash
cd ./charts
helm install spring-reactive-hello-world hello0
```
> ### Note
> Be sure to check out deployment variables in `./charts/hello0/values.yaml`!

## Running tests
```bash
mvnw test
```

## Development
If you are using IntelliJ IDEA, be sure to check out your JAVA environment settings and synchronize all packages from Maven repository to make sure that no packages are missing.
Importing project into IntelliJ IDEA is straight forward. Just create a new project from existing files, synchronize Maven packages, and you are good to go.

MongoDB database is optional and needed only if your want to play with *reactive* DB streams. Currently, there is **no official support for Oracle Database** in Reactive Spring and the list of supported database engines is limited.
Furthermore, no prior database content is needed. All data is created as a part of mocked test data.

## Resources
Links used and handy web resources:
- [Spring Framework Reference](https://docs.spring.io/spring-framework/docs/current/reference/html/)
- [Reactor Core API](https://projectreactor.io/docs/core/release/api/reactor/core/publisher/package-summary.html)
- [Reactor 3 Reference Guide](https://projectreactor.io/docs/core/release/reference/)
- [Udemy course - Build Reactive RESTFUL APIs using Spring Boot/WebFlux](https://www.udemy.com/course/build-reactive-restful-apis-using-spring-boot-webflux/)
