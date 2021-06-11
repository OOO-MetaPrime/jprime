# Базовый модуль 

Содержит глобальные константы и API для межсервисного взаимодействия 

## Базовые настройки


| Параметр  | Описание     | Пример  |
| -----| ------------- | ------------------ | 
| spring.application.name | имя spring сервиса | sandbox-service |
| jprime.application.code | код jprime сервиса | sandbox |
| jprime.application.title | имя jprime сервиса | Тестовая песочница |

##  Системные события

Все события в системе являются ассинхронными. 

Событие является наследником класса ``mp.jprime.events.systemevents.JPSystemEvent``
и может быть инициировано в любом сервисе системы 

События инициируются и обрабатываются программным кодом и используются для передачи информации между сервисами 

### Инициализация события

```
  private SystemEventPublisher eventPublisher;

  @Autowired(required = false)
  private void setSystemEventPublisher(SystemEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  ...
  
  private void publishEvent(String code) {
    if (eventPublisher == null) {
      return;
    }
    eventPublisher.publishEvent(new <Event extends JPSystemEvent>());
  }
```

### Подписывание на событие

Класс-обработчик должен содержать метод с логикой обработки события 

```  
  @EventListener
  public void handleApplicationEvent(JPSystemApplicationEvent event) {
    JPSystemEvent jpSystemEvent = event.getEvent();
    ....
  }  
```

### Реализация SystemEventPublisher

Существует две реализации передачи системных событий

* ``KafkaSystemEventService``. Поддержка обмена событиями через Kafka.

Включается опцией `jprime.events.systemevents.kafka.enabled=true` и рекомендуется для микросервисных сборок

* ``ApplicationSystemEventService ``. Поддержка обмена событиями внутри приложения

Включается опцией `jprime.events.systemevents.app.enabled=true` и рекомендуется для монолитных сборок