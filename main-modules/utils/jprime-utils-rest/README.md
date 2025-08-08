# Описание

REST-методы работы утилит

## Настройка утилиты

### Привязка утилиты

| Свойство | Описание                            |
|----------|-------------------------------------|
| object   | Обработка 1 объекта                 |
| list     | Обработка списка объектов           |
| uni      | Обработка объекта и списка объектов |
| custom   | Произвольная                        |

## Формат вызова

### Универсальный вызов произвольного шага утилиты

``POST /utils/v1/<код утилиты>/mode/<код шага утилиты>``

#### Пример c REST запросом

Входные и выходные параметры произвольный REST, который сериализуется, десериализуется в параметры метода

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
  "deleteData": false,
  "response": "Thanx for hello",
    "resultType": "custom"
}
```

#### Пример c multipart/form-data запросом

``POST /utils/v1/hello/mode/response``

* запрос

```multipart/form-data

"file": <binary>,
"request": "hello"
```

* ответ

```json
{
  "changeData": false,
  "deleteData": false,
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

### Режим check

Если в утилите нет метода, помеченного `code = "check"`, то по умолчанию код c `mode/check` отработает с 200 кодом
и пустым результатом

```json
{
  "rootObjectClassCode": null,
  "rootObjectId": null,
  "objectClassCode": "universityNeat",
  "objectIds": [2]
}
```

| Параметр            | Описание                                 |
|---------------------|------------------------------------------|
| rootObjectClassCode | кодовое имя метакласса корневого объекта |
| rootObjectId        | идентификатор корневого объекта          |
| objectClassCode     | Кодовое имя метаописания класса          |
| objectIds           | Идентификатор объекта                    |

### Получение значений по умолчанию

Если в утилите нет метода, помеченного `code = inParamsDefValues`, то по умолчанию код c `mode/inParamsDefValues` отработает с 200 кодом
и пустым результатом

При наличии метода и его вызове, ответ будет содержать `result` со значениями по умолчанию
```json
{
  "description": null,
  "changeData": false,
  "deleteData": false,
  "result": {
    "snils": "13302061984",
    "phoneNumber": "79811112345"
  },
  "resultType": "inParamsDefValues",
  "qName": null
}
```

### Валидация параметров утилиты
Каждая утилита имеет режим `mode/validate` если в утилите это режим не реализован, то по умолчанию
метод выдаст положительный результат валидации

Пример ответа по умолчанию:
```json
{
  "description": "Валидация параметров отсутствует",
  "result": {
    "valid": true
  },
  "resultType": "validate"
}
```

Параметрами для валидации как правило выступают параметры основного метода утилиты.
Если параметры не прошли валидацию, то ответ будет таким
```json
{
  "result": {
    "valid": false,
    "details": [
      {
        "code": "",
        "message": "Введен не корректный номер банковской карты"
      }
    ]
  },
  "resultType": "validate"
}
```

### Получение описания всех утилит

* Доступ роль UI_ADMIN

``GET utils/v1/labels``

* ответ

| Код | Описание                 |
|-----|--------------------------|
| 401 | Нет доступа              |
| 500 | Ошибка сервера           |
| 200 | Запрос отработал успешно |

```json
[
    {
        "utilCode": "test2",
        "modeCode": "ready",
        "title": "Тестовая утилита (message ответ)",
        "qName": "",
        "uni": false,
        "jpClasses": [
          "pers"
        ],
        "jpClassTags": [
          "tag1"
        ],
        "type": "object"   
    },
    {
        "utilCode": "test3",
        "modeCode": "copy",
        "title": "Тестовая утилита (объект ответ)",
        "qName": "",
        "uni": false,
        "jpClasses": [
          "pers"
        ],
        "jpClassTags": [
          "tag1"
        ],
        "type": "object"   
    },
    ....
]    
```

| Параметр    | Описание                                            |
|-------------|-----------------------------------------------------|
| utilCode    | Урл утилиты                                         |
| modeCode    | Код шага                                            |
| title       | Название шага                                       |
| qName       | QName шага                                          |
| uni         | Признак общедоступной утилиты                       |
| jpClasses   | Список классов, для которых доступна утилита        |
| jpClassTags | Теги классов, обрабатываемые этой утилитой          |
| type        | Тип привязки утилиты (объект, список, произвольная) |

### Получение списка всех утилит, доступных пользователю

``GET utils/v1/settings``

* ответ

| Код | Описание                 |
|-----|--------------------------|
| 401 | Нет доступа              |
| 500 | Ошибка сервера           |
| 200 | Запрос отработал успешно |

```json
[
    {
            "utilCode": "perscopy",
            "modeCode": "copy",
            "title": "Копирование учетных данных ЛДПГУ",
            "qName": "",            
            "confirmMessage": "Вы уверены ?",
            "uni": false,
            "jpClasses": [
                "pers"
            ],
            "jpClassTags": [
              "tag1"
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
                },
                ...
            ],
            "inParamsDefValues": false,
            "infoMessage": "Важно сообщение для пользователя.",
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
                }
            ]
        },
    ....
]    
```

| Параметр          | Описание                                            |
|-------------------|-----------------------------------------------------|
| utilCode          | Урл утилиты                                         |
| modeCode          | Код шага                                            |
| title             | Название шага                                       |
| qName             | QName шага                                          |
| confirmMessage    | Сообщение перед запуском шага                       |
| uni               | Признак общедоступной утилиты                       |
| jpClasses         | Список классов, для которых доступна утилита        |
| jpClassTags       | Теги классов, обрабатываемые этой утилитой          |
| type              | Тип привязки утилиты (объект, список, произвольная) |
| jpAttrs           | Настройки доступа на атрибутах                      |
| inParams          | Список входных параметров                           |
| inParamsDefValues | Признак наличия значений по умолчанию               |
| infoMessage       | Сообщение на форму утилиты                          |
| resultType        | Тип исходящих параметров                            |
| outCustomParams   | Список кастомных исходящих параметров               |

### Получение списка всех утилит, доступных пользователю для указанного класса

``GET utils/v1/settings/{classCode}``

* ответ

| Код | Описание                 |
|-----|--------------------------|
| 401 | Нет доступа              |
| 500 | Ошибка сервера           |
| 200 | Запрос отработал успешно |

```json
[
    {
            "utilCode": "perscopy",
            "modeCode": "copy",
            "title": "Копирование учетных данных ЛДПГУ",
            "qName": "",           
            "uni": false,
            "jpClasses": [
                "pers"
            ],
            "jpClassTags": [
              "tag1"
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
                },
                ...
            ],
            "inParamsDefValues": false,
            "infoMessage": "Важно сообщение для пользователя.",
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
                    "length": null,
                    "stringFormat": null,
                    "stringMask": null
                }
            ]
        },
    ....
]       
```

| Параметр          | Описание                                            |
|-------------------|-----------------------------------------------------|
| utilCode          | Урл утилиты                                         |
| modeCode          | Код шага                                            |
| title             | Название шага                                       |
| qName             | QName шага                                          |
| confirmMessage    | Сообщение перед запуском шага                       |
| jpClasses         | Список классов, для которых доступна утилита        |
| uni               | Признак общедоступной утилиты                       |
| jpClassTags       | Теги классов, обрабатываемые этой утилитой          |
| type              | Тип привязки утилиты (объект, список, произвольная) |
| jpAttrs           | Настройки доступа на атрибутах                      |
| inParams          | Список входных параметров                           |
| inParamsDefValues | Признак наличия значений по умолчанию               |
| infoMessage       | Сообщение на форму утилиты                          |
| resultType        | Тип исходящих параметров                            |
| outCustomParams   | Список кастомных исходящих параметров               |

### Массовый чек утилит

``POST utils/v1/batchCheck``

* запрос

```json
{
    "rootObjectClassCode": null,
    "rootObjectId": null,
    "ids": [
      {
          "objectClassCode": "universityNeat", 
          "objectIds": [2]
      }
    ],
    "utils": [
      "test1", "test5", "test6", "test2", "test7"
    ]
}
```

| Параметр            | Описание                                 |
|---------------------|------------------------------------------|
| rootObjectClassCode | кодовое имя метакласса корневого объекта |
| rootObjectId        | идентификатор корневого объекта          |
| ids                 | Идентификаторы проверяемых объектов      |
| objectClassCode     | Кодовое имя метаописания класса          |
| objectIds           | Идентификатор объекта                    |
| utils               | Коды проверяемых утилит                  |

* ответ

```json
{
    "result": [
        {
            "objectClassCode": "universityNeat",
            "objectId": "2",
            "utilCode": "test1",
            "result": {
                "description": "Запуск разрешен",
                "changeData": false,
                "denied": false,
                "resultType": "check",
                "confirm": null,
                "qName": null
            }
        },
        {
            "objectClassCode": "universityNeat",
            "objectId": "2",
            "utilCode": "test5",
            "result": {
                "description": "Запуск закрыт",
                "changeData": false,
                "denied": true,
                "resultType": "check",
                "confirm": null,
                "qName": "utils.test5.denied"
            }
        }
    ],
    "resultType": "batchCheck"
}
```

| Параметр           | Описание                                      |
|--------------------|-----------------------------------------------|
| resultType         | Тип ответа, всегда batchCheck                 |
| objectClassCode    | Кодовое имя метаописания класса               |
| objectIds          | Идентификатор объекта                         |
| utilCode           | Код проверяемой утилиты                       |
| result             | Результат проверки                            |
| result.description | Сообщение проверки                            |
| result.qName       | Код сообщения проверки                        |
| result.changeData  | false                                         |
| result.denied      | Признак запрета запуска утилиты               |
| result.resultType  | Тип ответа, всегда check                      |
| result.confirm     | Сообщение пользователю перед запуском утилиты |