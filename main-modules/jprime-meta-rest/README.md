# Описание

REST-методы публикации метаданных на чтение

## Операции с метаописанием

### 1. Получение типов атрибутов

``GET /meta/v1/attrTypes/``

* роль META_ADMIN

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
        "code": "string",
        "title": "Строка"
    },
    {
        "code": "long",
        "title": "Целочисленный (длинный)"
    },
    ...
]
```

| Параметр     | Описание
| ------------- | ------------------ 
| code | Код типа атрибута
| title | Название типа атрибута

### 2. Получение всего списка метаописаний

``GET /meta/v1/jpClasses/``

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

```
{
    "totalCount": 4,
    "classes": [
        {
            "guid": "cc53f898-ceec-49b3-81a3-43eb3fdc43f0",
            "code": "userEvent",
            "pluralCode": "userEvents",
            "qName": "common.userEvent",
            "description": "Таблица с информацией о пользовательских событиях",
            "jpPackage": "adminAccess",
            "immutable": true,
            "attrs": [
                {
                    "guid": "85e203ef-e611-4983-a102-fa046f45874c",
                    "code": "eventCode",
                    "qName": "common.userevent.eventCode",
                    "description": "Код события",
                    "type": "string"
                },
            ...
            ]
        },
        ...
    ]
}                
```

| Параметр     | Описание
| ------------- | ------------------ 
| totalCount | Общее количество классов
| classes | Список метаописаний

### 3 Получение метаописания определенного класса

``GET /meta/v1/jpClasses/<classCode>``

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

```
{
    "guid": "cc53f898-ceec-49b3-81a3-43eb3fdc43f0",
    "code": "userEvent",
    "pluralCode": "userEvents",
    "qName": "common.userEvent",
    "description": "Таблица с информацией о пользовательских событиях",
    "jpPackage": "adminAccess",
    "immutable": true,
    "attrs": [
        {
            "guid": "85e203ef-e611-4983-a102-fa046f45874c",
            "code": "eventCode",
            "qName": "common.userevent.eventCode",
            "description": "Код события",
            "type": "string"
        },
        {
            "guid": "c57d1be4-33e6-4ef9-a87c-f6d23bb4b141",
            "code": "removeDate",
            "qName": "common.userevent.removeDate",
            "description": "Дата, после которой событие будет удалено",
            "type": "datetime"
        },
        ...
    ]
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| guid | Глобальный идентификатор класса
