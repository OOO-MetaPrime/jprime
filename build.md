
# Запуск сборки

## Без запуска интеграционных тестов

Рекомендуется при сборке диструбутивов или у заказчика

```
  gradlew clean build --parallel --stacktrace  --daemon -Dtest.profile=unit
```

## Запуск всех тестов

Зависит от окружения и возможно в процессе разработки

```
  gradlew clean build --parallel --stacktrace  --daemon
```

# Описание тестов в коде

* помечены _интеграционные_ (требующие особого окружения) тесты тэгом `@Tag("integrationTests")`
* помечены _мануальные_ (имеющие смысл только при ручном запуске) тесты тэгом `@Tag("manualTests")`
* _юнит тесты_ - без тэга
* _мануальные тесты_ отключены при билде всегда
* для отключения _интеграционных_ передать при сборке `-Dtest.profile=unit`
* в корневые `build.gradle` проектов добавлять соответствующий блок:

```groovy
subprojects {
  //...
  test {
    failFast(true)
    useJUnitPlatform {
      excludeTags 'manualTests'
      if (System.properties['test.profile'] == 'unit') {
        excludeTags 'integrationTests'
      }
    }
  }
}
```