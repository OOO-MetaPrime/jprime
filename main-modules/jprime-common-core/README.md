# Базовый модуль 

Содержит глобальные константы и API для межсервисного взаимодействия 

##  Системные события

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

Класс-обработчик должен реализовывать интерфейс  ``implements ApplicationListener<Event extends JPSystemEvent>``

и содержать метод с логикой обработки события 

```
  @Override
  public void onApplicationEvent(<Event extends JPSystemEvent> event) {
    ...
  }
```

### Реализация SystemEventPublisher

Существует две реализации передачи системных событий

* ``KafkaSystemEventService``. Поддержка обмена событиями через Kafka.

Включается опцией `jprime.events.systemevents.kafka.enabled=true` и рекомендуется для микросервисных сборок

* ``ApplicationSystemEventService ``. Поддержка обмена событиями внутри приложения

Включается опцией `jprime.events.systemevents.app.enabled=true` и рекомендуется для монолитных сборок