# Описание

REST-методы публикации настроек доступа на чтение

## Операции с метаописанием

### 1. Получение всего списка настроек доступа

``GET /access/v1/jpPackages/``

* роль AUTH_ACCESS

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

для роли `AUTH_ADMIN` 

```json
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

для остальных ролей

```json
[
    {
        "code": "adminAccess",
        "name": "Полный доступ и только для роли ADMIN",
        "description": "Полный доступ и только для роли ADMIN",
        "qName": ""
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

* роль AUTH_ACCESS

| Параметр     | Описание
| ------------- | ------------------ 
| сode | Код настройки.

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 404 | Адрес не найден
| 500 | Ошибка сервера
| 200 | Утилита отработала успешно 

для роли `AUTH_ADMIN` 

```json
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

для остальных ролей

```json
{
    "code": "commonReadonly",
    "name": "Доступ полный для роли ADMIN, остальным на чтение",
    "description": "Доступ полный для роли ADMIN, остальным на чтение",
    "qName": ""
}
```

### 3 Полный список настроек доступа ко всем классам

``GET /access/v1/jpClasses``

* роль AUTH_ACCESS

* ответ 

```json
[
    {
        "classCode": "<classCode1>",
        "read": true,
        "create": true,
        "update": true,
        "delete": true,
        "editAttrs": {
                "<attrCode1>": true,
                "<attrCode2>": true,
                "<attrCode3>": false
         }
    },
    {
        "classCode": "<classCode2>",
        "read": true,
        "create": true,
        "update": true,
        "delete": false,
        "editAttrs": {
                "<attrCode1>": true,
                "<attrCode2>": true,
                "<attrCode3>": false
         }        
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
| editAttrs | Список атрибутов, доступных для редактирования

`Список атрибутов, доступных для редактирования` содержит кодовое имя атрибута и признак возможности `редактирования`.
Если кодового имени атрибута в `editAttrs` нет. то `просмотр` атрибута `запрещен` 

### 4 Настройки доступа к определенному классу

``GET /access/v1/jpClasses/<classCode>``

* роль AUTH_ACCESS

* ответ

```json
{
    "classCode": "<classCode>",
    "read": true,
    "create": true,
    "update": true,
    "delete": true,
    "editAttrs": {
            "<attrCode1>": true,
            "<attrCode2>": true,
            "<attrCode3>": false
     }
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| classCode | Кодовое имя класса
| read | Признак доступа на чтение объектов класса
| create | Признак доступа на создание объектов класса
| update | Признак доступа на обновление объектов класса
| delete | Признак доступа на удаление объектов класса
| editAttrs | Список атрибутов, доступных для редактирования

`Список атрибутов, доступных для редактирования` содержит кодовое имя атрибута и признак возможности `редактирования`.
Если кодового имени атрибута в `editAttrs` нет. то `просмотр` атрибута `запрещен` 

### 5 Настройки доступа к определенному классу при указанном значении 

``GET /access/v1/jpClasses/<classCode>/<код атрибута>/<значение>``

* роль AUTH_ACCESS

Используется для проверки доступа, обычно создания, по обратной ссылке

* ответ

```json
{
    "classCode": "<classCode>",
    "read": true,
    "create": true,
    "update": true,
    "delete": true,
    "editAttrs": {
            "<attrCode1>": true,
            "<attrCode2>": true,
            "<attrCode3>": false
     }
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| classCode | Кодовое имя класса
| read | Признак доступа на чтение объектов класса
| create | Признак доступа на создание объектов класса
| update | Признак доступа на обновление объектов класса
| delete | Признак доступа на удаление объектов класса
| editAttrs | Список атрибутов, доступных для редактирования

`Список атрибутов, доступных для редактирования` содержит кодовое имя атрибута и признак возможности `редактирования`.
Если кодового имени атрибута в `editAttrs` нет. то `просмотр` атрибута `запрещен` 

### 6 Настройки доступа к определенному объекту

``GET /access/v1/jpObjects/<pluralClassCode>/<objectId>``

* роль AUTH_ACCESS

* ответ

```json
{
    "objectClassCode": <classCode>,
    "objectId":  <objectId>,
    "read": true,
    "create": false,
    "update": false,
    "delete": false,
    "editAttrs": {
            "<attrCode1>": true,
            "<attrCode2>": true,
            "<attrCode3>": false
     }
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
| editAttrs | Список атрибутов, доступных для редактирования


`Список атрибутов, доступных для редактирования` содержит кодовое имя атрибута и признак возможности `редактирования`.
Если кодового имени атрибута в `editAttrs` нет. то `просмотр` атрибута `запрещен` 

### 7 Полный список настроек политик доступа ABAC

``GET /access/v1/policySets``

* роль AUTH_ADMIN

* ответ 

```json
[
  {
      "code": "8ffc9a11-6b78-44c5-b940-5d6bc4319e65",
      "name": "Группа политик esrnOrgDepartment",
      "qName": "jpPolicySet.esrnOrgDepartment",
      "target": {
          "jpClasses": [
              "esrnOrgDepartment"
          ]
      },
      "policies": [
      ...      
]
```

### 8 Настройка политики доступа ABAC по коду

``GET /access/v1/policySets/<код политики доступа>``

* роль AUTH_ADMIN

* ответ 

```json
{
    "code": "8ffc9a11-6b78-44c5-b940-5d6bc4319e65",
    "name": "Группа политик esrnOrgDepartment",
    "qName": "jpPolicySet.esrnOrgDepartment",
    "target": {
        "jpClasses": [
            "esrnOrgDepartment"
        ]
    },
    "policies": [
    ...
}
```


### 9 Поиск политик доступа ABAC 

``POST /access/v1/policySets/search``

* роль AUTH_ADMIN

 Возвращаются только политики, соответствующие запросу
 В группах будут отсутствовать политки, не удовлетворяющие условиям поиска
 
* запрос

```json
{
    "name":  null,
    "roles": ["AUTH_ACCESS"],
    "jpClassCodes": [ "esrnOrgDepartment"],
    "useCreate": null,
    "useRead": null,
    "useUpdate": null,
    "useDelete": null,
    "usePermit": null,
    "useProhibition": true,
    "useEnviromentRules": null
}
```

| Параметр     | Описание
| ------------- | ------------------ 
| name | Значение для поиска по имени
| roles | Список ролей для фильтрации
| jpClassCodes | Список классов для фильтрации
| useCreate | Поиск по созданию
| useRead | Поиск по чтению
| useUpdate | Поиск по изменению
| useDelete | Поиск по удалению
| usePermit | Поиск по настройкам на разрешение
| useProhibition | Поиск по настройкам на запрет
| useEnviromentRules | Поиск по наличию правил окружения

* ответ 

```json
[
  {
      "code": "8ffc9a11-6b78-44c5-b940-5d6bc4319e65",
      "name": "Группа политик esrnOrgDepartment",
      "qName": "jpPolicySet.esrnOrgDepartment",
      "target": {
          "jpClasses": [
              "esrnOrgDepartment"
          ]
      },
      "policies": [
      ...  
]
```