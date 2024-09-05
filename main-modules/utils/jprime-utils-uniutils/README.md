# Описание

Типовые общие утилиты.

## 1. Утилита массового изменения атрибутов

### 1.1 Описание утилиты

Изменяет значения переданных атрибутов у объектов с переданным `objectClassCode` и переданными `id`.

### 1.2 Использование

Для запуска необходимо сделать запрос:

`POST .../utils/v1/attrvaluechangeutil/mode/change` с телом запроса вида:

```json
{
    "objectClassCode": "eguApplicant",
    "objectIds": 
    [
        "1","2","3"
    ],
    "attrs": 
    [
        {
            "attrCode": "firstName",
            "attrValue": "new firstName"
        },
        {
            "attrCode": "middleName",
            "attrValue": "new middleName"
        }
    ]
}
```
где параметры - это:

| Код             | Описание                                                           | 
|-----------------|--------------------------------------------------------------------| 
| objectClassCode | код класса меты                                                    |
| objectIds       | список идентификаторов сущностей, в которых надо обновить атрибуты |
| attrs           | список пар код-значение атрибутов, которые нужно изменить          |

`attrs`:

| Код       | Описание                                   | 
|-----------|--------------------------------------------| 
| attrCode  | код атрибута в мете                        |
| attrValue | новое значение атрибута, может быть `null` |

### 1.3 Ответ утилиты

В случае успеха возвращает код `200 OK` с телом ответа:

```json
{
    "description": "Массовое внесение изменений успешно выполнено",
    "changeData": true,
    "resultType": "message",
    "qName": "attrvaluechangeutil.done"
}
```

В случае ошибки логирует детали ошибки и возвращает код `400 BAD REQUEST` с телом ответа:

```json
{
    "timestamp": "2020-11-06T10:54:59.630+0300",
    "path": "/utils/v1/attrvaluechangeutil/mode/change",
    "status": 400,
    "error": "Bad Request",
    "message": "",
    "requestId": "b0a13207-6",
    "details": [
        {
            "message": "Массовое внесение изменений не выполнено",
            "code": "attrvaluechangeutil.failed"
        }
    ]
}
```
