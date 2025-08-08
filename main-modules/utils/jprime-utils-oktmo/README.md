# Описание

API утилиты по предоставлению ОКТМО

### Получение данных по ОКТМО

``POST utils/v1/oktmo-search/mode/get``

* Доступ роль AUTH_ACCESS

* запрос

```json
{
  "oktmo": ["38630000"]
}
```

| Параметр | Описание     |
|----------|--------------|
| oktmo    | Список ОКТМО |


```json
{
  "description": null,
  "changeData": false,
  "deleteData": false,
  "oktmo": {
    "38630000": "Поныровский муниципальный район"
  },
  "result": null,
  "resultType": "custom",
  "qName": null
}
```

| Параметр | Описание                          |
|----------|-----------------------------------|
| oktmo    | Данные ОКТМО в виде код: название |

### Поиск ОКТМО

``POST utils/v1/oktmo-search/mode/search``

* Доступ роль AUTH_ACCESS

* запрос

```json
{
  "query": "25 смоленск",
  "limit": 50,
  "subjectSearch": true,
  "formationSearch": true,
  "districtSearch": true,
  "oktmoSearch": ["25000000"],
  "authSearch": true
}
```

| Параметр        | Описание                          |
|-----------------|-----------------------------------|
| query           | поисковая строка                  |
| limit           | количество объектов в выборке     |
| subjectSearch   | поиск по субъектам                |
| formationSearch | поиск по образованиям             |
| districtSearch  | поиск по округам                  |
| oktmoSearch     | поиск с учетом указанных ОКТМО    |
| authSearch      | поиск с учетом ОКТМО пользователя |


```json
{
  "description": null,
  "changeData": false,
  "deleteData": false,
  "oktmo": {
    "25612422": "Смоленское"
  },
  "result": null,
  "resultType": "custom",
  "qName": null
}
```

| Параметр | Описание                          |
|----------|-----------------------------------|
| oktmo    | Данные ОКТМО в виде код: название |