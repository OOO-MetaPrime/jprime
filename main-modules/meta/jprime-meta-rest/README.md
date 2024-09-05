# Описание

REST-методы публикации метаданных на чтение

## Операции с метаописанием

### Получение типов атрибутов

``GET /meta/v1/attrTypes/``

* роль AUTH_ACCESS

* ответ

| Код | Описание                   |
|-----|----------------------------|
| 401 | Нет доступа                |
| 404 | Адрес не найден            |
| 500 | Ошибка сервера             |
| 200 | Утилита отработала успешно |

```json
[
  {
    "code": "string",
    "title": "Строка"
  },
  {
    "code": "long",
    "title": "Целочисленный (длинный)"
  }
]
```

| Параметр | Описание               |
|----------|------------------------|
| code     | Код типа атрибута      |
| title    | Название типа атрибута |

### Получение типов свойств

``GET /meta/v1/propertyTypes/``

* роль AUTH_ACCESS

* ответ

| Код | Описание                   |
|-----|----------------------------|
| 401 | Нет доступа                |
| 404 | Адрес не найден            |
| 500 | Ошибка сервера             |
| 200 | Утилита отработала успешно |

```json
[
  {
    "code": "element",
    "title": "Элемент"
  },
  {
    "code": "string",
    "title": "Строка"
  },
  {
    "code": "long",
    "title": "Целочисленный (длинный)"
  }
]
```

| Параметр | Описание               |
|----------|------------------------|
| code     | Код типа свойства      |
| title    | Название типа свойства |

### Получение всего списка метаописаний

``GET /meta/v1/jpClasses/``

* ответ

| Код | Описание                   |
|-----|----------------------------|
| 401 | Нет доступа                |
| 404 | Адрес не найден            |
| 500 | Ошибка сервера             |
| 200 | Утилита отработала успешно |

```json
{
  "totalCount": 4,
  "classes": [
    {
      "guid": "cc53f898-ceec-49b3-81a3-43eb3fdc43f0",
      "code": "userEvent",
      "qName": "common.userEvent",
      "tags": [
        "tag1"
      ],
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

| Параметр   | Описание                 |
|------------|--------------------------|
| totalCount | Общее количество классов |
| classes    | Список метаописаний      |

### Получение метаописания определенного класса

``GET /meta/v1/jpClasses/<classCode>``

* ответ

| Код | Описание                   |
|-----|----------------------------|
| 401 | Нет доступа                |
| 404 | Адрес не найден            |
| 500 | Ошибка сервера             |
| 200 | Утилита отработала успешно |

```json
{
  "guid": "cc53f898-ceec-49b3-81a3-43eb3fdc43f0",
  "code": "userEvent",
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
    {
      "guid": "749adfcf-67be-4256-8abc-2a24b9568341",
      "code": "fileName",
      "qName": "fileName",
      "name": "Название файла в хранилище",
      "shortName": "Название файла в хранилище",
      "description": "Название файла в хранилище",
      "jpPackage": null,
      "identifier": false,
      "mandatory": false,
      "type": "file",
      "length": null,
      "refJpClass": null,
      "refJpAttr": null,
      "signAttr": null,
      "refJpFile": {
        "titleAttr": "fileTitle",
        "extAttr": "fileExt",
        "sizeAttr": "fileSize",
        "dateAttr": "fileDate",
        "infoAttr": "fileInfo",
        "fileStampAttr": null
      }
    },
    {
      "guid": "a717e338-1e08-4610-917f-9c9969db51b9",
      "code": "jsonattr",
      "qName": "common.userevent.jsonattr",
      "name": "Test json attrs",
      "shortName": "Test json attrs",
      "description": "Test json attrs",
      "jpPackage": null,
      "identifier": false,
      "mandatory": false,
      "type": "json",
      "length": null,
      "refJpClass": null,
      "refJpAttr": null,
      "refJpFile": null,
      "signAttr": null,
      "jpProps": [
        {
          "code": "testprop",
          "qName": "common.userevent.jsonprop",
          "name": "Test Prop",
          "shortName": null,
          "description": null,
          "mandatory": false,
          "type": "element",
          "length": null,
          "refJpClass": null,
          "refJpAttr": null,
          "jpProps": [
            {
              "code": "innertestprop",
              "qName": "common.userevent.injsonprop",
              "name": "Inner Test Prop",
              "shortName": null,
              "description": null,
              "mandatory": false,
              "type": "string",
              "length": null,
              "refJpClass": null,
              "refJpAttr": null,
              "jpProps": []
            }
          ]
        }
      ]
    }
    ...
  ]
}
```

### Выгрузка метаописания в csv

``GET /meta/v1/jpClasses/<classCode>/csv-export/<token>``

| Параметр  | Описание                  |
|-----------|---------------------------|
| classCode | Код импортируемого класса |
| token     | Токен авторизации         |

* ответ

| Код | Описание            |
|-----|---------------------|
| 403 | Нет доступа         |
| 404 | Адрес не найден     |
| 500 | Ошибка сервера      |
| 200 | Успешное выполнение |

> В результате запроса будет скачан файл в формате .csv

## Описание параметров

Параметры класса

| Параметр    | Описание                            |
|-------------|-------------------------------------|
| guid        | Глобальный идентификатор класса     |
| code        | Код класса                          |
| name        | Название класса                     |
| shortName   | Короткое название класса            |
| description | Описание класса                     |
| qName       | qName класса                        |
| tags        | Теги класса                         |
| immutable   | Признак неизменяемости метаописания |
| attrs       | Список атрибутов класса             |

Параметры атрибута

| Параметр         | Описание                                  |
|------------------|-------------------------------------------|
| guid             | Глобальный идентификатор                  |
| code             | Кодовое имя атрибута                      |
| qName            | Полный код атрибута                       |
| name             | Название атрибута                         |
| shortName        | Короткое название атрибута                |
| description      | Описание атрибута                         |
| jpPackage        | Настройка доступа                         |
| identifier       | Признак идентификатора                    |
| mandatory        | Признак обязательности                    |
| type             | Тип атрибута                              |
| updatable        | Признак обновляемости значения атрибута   |
| length           | Длина (для строковых полей)               |
| refJpClass       | Кодовое имя класса, на который ссылается  |
| refJpAttr        | Кодовое имя атрибута ссылочного класса    |
| signAttr         | Кодовое имя атрибута, содержащего подпись |
| refJpFile        | Описание файлового атрибута               |
| simpleFraction   | Описание простой дроби                    |
| money            | Настройки денежного типа                  |
| geometry         | Описание пространственных данных          |
| virtualReference | Описание виртуальной ссылки               |
| jpProps          | Схема свойств псевдо-меты                 |

Параметры описания файлового атрибута (для type file)

| Параметр      | Описание                                                 |
|---------------|----------------------------------------------------------|
| titleAttr     | Кодовое имя атрибута - Заголовок файла                   |
| extAttr       | Кодовое имя атрибута - Расширение файла                  |
| sizeAttr      | Кодовое имя атрибута - Размер файла                      |
| dateAttr      | Кодовое имя атрибута - Дата файла                        |
| infoAttr      | Кодовое имя атрибута - Дополнительная информация о файле |
| fileStampAttr | Кодовое имя атрибута - Файл со штампом для подписи       |

Параметры описания простой дроби (для type simpleFraction)

| Параметр        | Описание                                 |
|-----------------|------------------------------------------|
| integerAttr     | Атрибут для хранения - Целая часть дроби |
| denominatorAttr | Атрибут для хранения - Знаменатель дроби |

Описание свойства псевдо-меты ``jpProps`` (для type json)

| Свойство    | Описание                                                           |
|-------------|--------------------------------------------------------------------|
| code        | Кодовое имя свойства                                               |
| qName       | Полный код свойства                                                |
| name        | Название свойства                                                  |
| mandatory   | Признак обязательности                                             |
| type        | Тип свойства                                                       |
| length      | Длина (для строковых полей, в том числе для строковых виртуальных) |
| refJpClass  | Кодовое имя класса, на который ссылается                           |
| refJpAttr   | Кодовое имя атрибута ссылочного класса                             |
| jpProps     | Схема вложенных свойств                                            |

## Сервисы выгрузки метаописания в .csv файл

Предоставляют механизмы выгрузки атрибутов метаописания в .csv файл.

### Сервис `JPAttrCsvWriter`

Предоставляет возможность более глубокой настройки параметров выгрузки метаописания с помощью `JpCsvBaseWriterSettings`
в `OutputStream`.

### Сервис `JPAttrCsvWriterCommonService`

Предоставляет возможность выгрузить описание атрибутов переданного класса в `InputStream` формата csv
