# Описание

Модуль мониторинга состояния приложения

## Мониторинг JVM

### Мониторинг памяти JVM

Сравнивает выделенную и используемую память приложению периодически, согласно cron-настройке `jprime.monitoring.jvm.memory.checkTimeout`. По умолчанию, каждую минуту
 
При превышении используемой памяти `jprime.monitoring.jvm.memory.alarmPercents` (90) % от выделенного пишет в лог
предупреждения с кодами `jvm.nonheap.memory.limit.warning` и `jvm.heap.memory.limit.warning` 
```
jvm.nonheap.memory.limit.warning - The limit of used nonheap memory 239,00 MB / 240,00 MB
jvm.heap.memory.limit.warning - The limit of used heap memory 1300,00 MB / 1331,00 MB
```
