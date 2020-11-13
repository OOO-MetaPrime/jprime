# Описание

REST-методы публикации настроек доступа на чтение

## Операции с метаописанием

### 1. Получение всего списка настроек доступа

``GET /access/v1/jpPackages/``

* роль AUTH_ADMIN или META_ADMIN

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

```
[
    {
        "code": "adminAccess",
        "name": "Полный доступ и только для роли ADMIN",
        "description": "Полный доступ и только для роли ADMIN",
        "qName": "",
        "accesses": [
            {
                "type": "permit",
                "role": "ADMIN",
                "read": true,
                "create": true,
                "update": true,
                "delete": true
            }
        ]
    },
    ...
]             
```

| Параметр     | Описание
| ------------- | ------------------ 
| totalCount | Общее количество классов
| classes | Список метаописаний

### 2 Получение настроек доступа по указанному коду

``GET /access/v1/jpPackages/<сode>``

* роль AUTH_ADMIN или META_ADMIN

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

```
{
    "code": "commonReadonly",
    "name": "Доступ полный для роли ADMIN, остальным на чтение",
    "description": "Доступ полный для роли ADMIN, остальным на чтение",
    "qName": "",
    "accesses": [
        {
            "type": "permit",
            "role": "ADMIN",
            "read": true,
            "create": true,
            "update": true,
            "delete": true
        },
        {
            "type": "permit",
            "role": "AUTH_ACCESS",
            "read": true,
            "create": false,
            "update": false,
            "delete": false
        },
        {
            "type": "prohibition",
            "role": "DISABLED",
            "read": true,
            "create": true,
            "update": true,
            "delete": true
        }
    ]
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| guid | Глобальный идентификатор класса


### 3 Полный список настроек доступа ко всем классам

``GET /access/v1/jpClasses``

* роль AUTH_ACCESS

* ответ 

```
[
    {
        "classCode": "<classCode1>",
        "read": true,
        "create": true,
        "update": true,
        "delete": true
    },
    {
        "classCode": "<classCode2>",
        "read": true,
        "create": true,
        "update": true
    }
]
```

| Параметр     | Описание
| ------------- | ------------------ 
| classCode | Кодовое имя класса
| read | Признак доступа на чтение объектов класса
| create | Признак доступа на создание объектов класса
| update | Признак доступа на обновление объектов класса
| delete | Признак доступа на удаление объектов класса

### 4 Настройки доступа к определенному классу

``GET /access/v1/jpClasses/<classCode>``

* роль AUTH_ACCESS

* ответ

```
{
    "classCode": "<classCode>",
    "read": true,
    "create": true,
    "update": true,
    "delete": true
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| classCode | Кодовое имя класса
| read | Признак доступа на чтение объектов класса
| create | Признак доступа на создание объектов класса
| update | Признак доступа на обновление объектов класса
| delete | Признак доступа на удаление объектов класса

### 5 Настройки доступа к определенному объекту

``GET /access/v1/jpObjects/<pluralClassCode>/<objectId>``

* роль AUTH_ACCESS

* ответ

```
{
    "objectClassCode": <classCode>,
    "objectId":  <objectId>,
    "read": true,
    "create": false,
    "update": false,
    "delete": false
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| objectClassCode | Кодовое имя класса объекта
| objectId | Идентификатор объекта
| read | Признак доступа на чтение объекта
| create | Признак доступа на создание объекта
| update | Признак доступа на обновление объекта
| delete | Признак доступа на удаление объекта