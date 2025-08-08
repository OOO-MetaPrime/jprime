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