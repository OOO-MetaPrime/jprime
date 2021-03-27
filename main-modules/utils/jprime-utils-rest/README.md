# Описание

REST-методы работы утилит

## Настройка утилиты

### Привязка утилиты

| Свойство     | Описание  |
| ------------- | ------------------ | 
| object | Обработка 1 объекта |
| list | Обработка списка объектов |
| uni | Обработка объекта и списка объектов |
| custom | Произвольная |

## Формат вызова

### 1. Универсальный вызов произвольного шага утилиты

``POST /utils/v1/<код утилиты>/mode/<код шага утилиты>``

Входные и выходные параметры произвольный REST, который сериализуется, десериализуется в параметры метода

#### Пример

``POST /utils/v1/hello/mode/response``

* запрос

```json
{
    "request": "hello"
}
```

* ответ

```json
{
    "changeData": false,
    "response": "Thanx for hello",
    "resultType": "custom"
}
```

#### Вызов общих (UNI) утилит

При вызове общих утилит (`uni = true`) необходимо передать в теле запроса `objectClassCode`, по которому будет 
осуществляться роут на конкретный сервис, обрабатывающий этот класс меты. 

##### Пример:

```json
{
    "objectClassCode": "eguApplicant",
    .... //other required fields
}
```

### 2. Режим check

Если в утилите нет метода, помеченного `code = "check"`, то по умолчанию код c `mode/check` отработает с 200 кодом
и пустым результатом

### 3. Получение описания всех утилит

* Доступ роль ADMIN

``GET utils/v1/labels``

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 500 | Ошибка сервера
| 200 | Запрос отработал успешно 

```json
[
    {
        "utilCode": "test2",
        "modeCode": "ready",
        "title": "Тестовая утилита (message ответ)",
        "qName": ""
    },
    {
        "utilCode": "test3",
        "modeCode": "copy",
        "title": "Тестовая утилита (объект ответ)",
        "qName": ""
    },
    ....
]    
```

| Параметр     | Описание
| ------------- | ------------------ 
| utilCode | Урл утилиты
| modeCode | Код шага
| title | Название шага
| qName | QName шага

### 4. Получение списка всех утилит, доступных пользователю

``GET utils/v1/settings``

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 500 | Ошибка сервера
| 200 | Запрос отработал успешно 

```json
[
    {
            "utilCode": "perscopy",
            "modeCode": "copy",
            "title": "Копирование учетных данных ЛДПГУ",
            "qName": "",            
            "confirmMessage": "Вы уверены ?",
            "jpClasses": [
                "pers"
            ],
            "type": "object",
            "jpAttrs": [
                {
                    "jpClass": "pers",
                    "jpAttr": "payRef"
                }
            ],            
            "inParams": [
                {
                    "code": "rn",
                    "type": "long",
                    "description": "Регистрационный номер гражданина",
                    "qName": "perscopy.in.rn.title",
                    "mandatory": true,
                    "multiple": false,
                    "refJpClass": "pers",
                    "refJpAttr": "reg_num",
                    "length": null
                },
                {
                    "code": "specCopy",
                    "type": "boolean",
                    "description": "Документы по образованию",
                    "qName": "perscopy.in.specCopy.title",
                    "mandatory": false,
                    "multiple": false,
                    "refJpClass": "",
                    "refJpAttr": "",
                    "length": null
                },
                ...
            ],
            "resultType": "custom",
            "outCustomParams": [
                {
                    "code": "rn",
                    "type": "long",
                    "description": "Регистрационный номер гражданина",
                    "qName": "perscopy.out.rn.title",
                    "mandatory": true,
                    "multiple": false,
                    "refJpClass": "pers",
                    "refJpAttr": "reg_num",
                    "length": null
                }
            ]
        },
    ....
]    
```

| Параметр     | Описание
| ------------- | ------------------ 
| utilCode | Урл утилиты
| modeCode | Код шага
| title | Название шага
| qName | QName шага
| confirmMessage | Сообщение перед запуском шага
| jpClasses | Список классов, для которых доступна утилита
| type | Тип привязки утилиты (объект, список, произвольная)
| jpAttrs| Настройки доступа на атрибутах
| inParams | Список входных параметров
| resultType | Тип исходящих параметров
| outCustomParams | Список кастомных исходящих параметров

### 5. Получение списка всех утилит, доступных пользователю для указанного класса

``GET utils/v1/settings/{classCode}``

* ответ

| Код     | Описание
| ------------- | ------------------ 
| 401 | Нет доступа
| 500 | Ошибка сервера
| 200 | Запрос отработал успешно 

```json
[
    {
            "utilCode": "perscopy",
            "modeCode": "copy",
            "title": "Копирование учетных данных ЛДПГУ",
            "qName": "",            
            "jpClasses": [
                "pers"
            ],
            "type": "object",
            "inParams": [
                {
                    "code": "rn",
                    "type": "long",
                    "description": "Регистрационный номер гражданина",
                    "qName": "perscopy.in.rn.title",
                    "mandatory": true,
                    "multiple": false,
                    "refJpClass": "pers",
                    "refJpAttr": "reg_num",
                    "length": null
                },
                {
                    "code": "specCopy",
                    "type": "boolean",
                    "description": "Документы по образованию",
                    "qName": "perscopy.in.specCopy.title",
                    "mandatory": false,
                    "multiple": false,
                    "refJpClass": "",
                    "refJpAttr": "",
                    "length": null
                },
                ...
            ],
            "resultType": "custom",
            "outCustomParams": [
                {
                    "code": "rn",
                    "type": "long",
                    "description": "Регистрационный номер гражданина",
                    "qName": "perscopy.out.rn.title",
                    "mandatory": true,
                    "multiple": false,
                    "refJpClass": "pers",
                    "refJpAttr": "reg_num",
                    "length": null
                }
            ]
        },
    ....
]       
```

| Параметр     | Описание
| ------------- | ------------------ 
| utilCode | Урл утилиты
| modeCode | Код шага
| title | Название шага
| qName | QName шага
| confirmMessage | Сообщение перед запуском шага
| jpClasses | Список классов, для которых доступна утилита
| type | Тип привязки утилиты (объект, список, произвольная)
| jpAttrs| Настройки доступа на атрибутах
| inParams | Список входных параметров
| resultType | Тип исходящих параметров
| outCustomParams | Список кастомных исходящих параметров