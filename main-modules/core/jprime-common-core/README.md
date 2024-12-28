# Базовый модуль JPrime

Содержит глобальные константы и API для межсервисного взаимодействия с учетом reactive

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