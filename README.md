## Reactive Spring Boot 2 with Kotlin

[![license](https://img.shields.io/github/license/mashape/apistatus.svg)]()


This code supports a [blog post I wrote](https://todd.ginsberg.com/post/springboot2-reactive-kotlin/), and implements a simple counter using Spring Boot 2.0, Kotlin, and Redis.


### Getting The Code

```
git clone https://github.com/tginsberg/springboot2-reactive-kotlin.git
```

### Requirements

0. Gradle 4.0+
1. Java 1.8
2. Redis installed and ready to use

### Running the server

```
gradlew bootRun
```

### Endpoints

| Purpose                  | Method | URL                 | Accept Header       |
|--------------------------|--------|---------------------|---------------------|
| Current state of counter | GET    | `/api/counter`      | `application/json`  |
| Counter event stream     | GET    | `/api/counter`      | `text/event-stream` |
| Increment counter        | PUT    | `/api/counter/up`   | `application/json`  |
| Decrement counter        | PUT    | `/api/counter/down` | `application/json`  |

