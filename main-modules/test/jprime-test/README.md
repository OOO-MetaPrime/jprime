# Модуль функций для написания тестов

## Проверка времени сборки

Для проверки модуля во время компиляции в build.gradle надо добавить строчку

```groovy
apply from: "${rootProject.projectDir}/jprime/gradle-scripts/validation-tests.gradle"
```

Для переопределения тестирования во время компиляции в нужно:

1) Добавить зависимость

```groovy
dependencies {
  testImplementation testFixtures(project(":jprime-test"))
}
```

2) Определить наследника сервиса в теста

```text
concrete-service/
└── src/
    ├── main/
    │   └── java/
    └── test/
        └── java/
            └── mp/
                └── test/
                    └── validation/
                        └── JPMigrationResourceValidationConcreteTest.java
```

```java
public class JPMigrationResourceValidationConcreteTest extends JPMigrationResourceValidationTest {
}
```

## Kafka testcontsiners

Для полключения к контейнеру с целью исследования сообщений в топиках можно поднять AKHQ в docker.

AKHQ будет доступен на порту 7080


docker-compose.yml

```yaml
version: '3.8'

services:
  akhq:
    image: tchiotludo/akhq:latest
    container_name: akhq
    restart: unless-stopped
    ports:
      - "7080:8080"
    extra_hosts:
      # Важно! localhost внутри контейнера будет указывать на хост
      - "localhost:host-gateway"
    environment:
      AKHQ_CONFIGURATION: |
        akhq:
          connections:
            remote-kafka:
              properties:
                bootstrap.servers: "localhost:<порт на котором запустился testcontainers>" 
```
