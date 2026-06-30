# Модуль функций для написания тестов

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
