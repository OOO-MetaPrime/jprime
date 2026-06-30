# Базовый модуль JPrime

Содержит глобальные константы и API для межсервисного взаимодействия с учетом reactive

## Настройки

| Настройка                           | Описание                                                                                             | По умолчанию |
| :---                                | :---                                                                                                 | :---         |
| jprime.web.static.cache.maxAge      | Максимальный срок хранения в секундах. <br /> Соответсвует заголовку Cache-control: max-age=${value} | 86400        |
| jprime.web.static.cache.cachePublic | Устанавливает признак public для кеша <br /> Соответсвует заголовку Cache-control: public            | true         |

## Вспомогательные классы

### JPForkJoinPoolService

ForkJoinPool для решений на базе JPrime. Рекомендуется к использованию для запуска асинхронных операций

```java
import java.util.concurrent.CompletableFuture;

class MyService {
  public void run() {
    CompletableFuture.runAsync(() -> {}, JPForkJoinPoolService.pool());
  }
}
```
или использовать реализацию `JPCompletableFuture`

```java
class MyService {  
  public voin run() {
    JPCompletableFuture.runAsync(() -> {});
  }
}
```
