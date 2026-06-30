# Описание

API утилиты по предоставлению ОКТМО

### Получение данных по ОКТМО

``POST utils/v1/oktmo-search/mode/get``

* Доступ роль AUTH_ACCESS

* запрос

```json
{
  "oktmo": [
    "38630000"
  ]
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
  "oktmoSearch": [
    "25000000"
  ],
  "authSearch": true
}
```

| Параметр        | Описание                          |
|-----------------|-----------------------------------|
| query           | поисковая строка                  |
| limit           | количество объектов в выборке     |
| subjectSearch   | поиск по субъектам                |
| formationSearch | поиск по муниципальному уровню    |
| districtSearch  | поиск по поселенческому уровню    |
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

### Получение данных по группе ОКТМО

``POST utils/v1/oktmo-search/mode/getGroup``

* Доступ роль AUTH_ACCESS

* запрос

```json
{
  "group": [
    "spbVyborgskiy"
  ],
  "prefixMode": true
}
```

| Параметр   | Описание                                              |
|------------|-------------------------------------------------------|
| group      | Коды групп ОКТМО                                      |
| prefixMode | Возвращаем значимые префиксы ОКТМО, входящие в группу |

```json
{
  "changeData": false,
  "deleteData": false,
  "groups": {
    "spbVyborgskiy": {
      "code": "spbVyborgskiy",
      "name": "Выборгский район",
      "oktmo": [
        "40312",
        "40313",
        "40314",
        "40315",
        "40316",
        "40317",
        "40318",
        "40319"
      ]
    }
  },
  "resultType": "custom"
}
```

| Параметр | Описание                                          |
|----------|---------------------------------------------------|
| groups   | Данные по группе ОКТМО: название/код/список ОКТМО |

### Поиск ОКТМО

``POST utils/v1/oktmo-search/mode/groupSearch``

* Доступ роль AUTH_ACCESS

* запрос

```json
{
  "query": "выборгский",
  "limit": 50,
  "prefixMode": false,
  "oktmoSearch": [
    "25000000"
  ],
  "authSearch": true
}
```

| Параметр    | Описание                                              |
|-------------|-------------------------------------------------------|
| query       | поисковая строка                                      |
| limit       | количество объектов в выборке                         |
| prefixMode  | Возвращаем значимые префиксы ОКТМО, входящие в группу |
| oktmoSearch | поиск с учетом указанных ОКТМО                        |
| authSearch  | поиск с учетом ОКТМО пользователя                     |

```json
{
  "changeData": false,
  "deleteData": false,
  "groups": {
    "spbVyborgskiy": {
      "code": "spbVyborgskiy",
      "name": "Выборгский район",
      "oktmo": [
        "40312000",
        "40313000",
        "40314000",
        "40315000",
        "40316000",
        "40317000",
        "40318000",
        "40319000"
      ]
    }
  },
  "resultType": "custom"
}
```

| Параметр | Описание                                          |
|----------|---------------------------------------------------|
| groups   | Данные по группе ОКТМО: название/код/список ОКТМО |