# Описание

REST запросы CRUD операция по мете

## Получение списка объектов

``GET /api/v1/<множественный код метакласса>?offset=<>&limit=<>``

* ответ
```
{
    "offset": 0,
    "limit": 10,
    "objectsCount": 1,
    "classCode": "spbCompany",
    "objects": [
        {
            "id": 1800065421,
            "classCode": "spbCompany",
            "title": null,
            "data": {
                "ownerType": 0,
                "dateReg": "2012-04-09T20:00:00.000+0000",
                "educationType": null,
                "municipality": null,
            }
        }
     ]
}
```

## Блок поиска

``POST /api/v1/<множественный код метакласса>/search``

### Критерии поиска

* fuzzyLike - нечеткий поиск. Для работоспособности в ``map`` должно быть указано поле ``fuzzy``, содержащее токены для нечеткого поиска
* fuzzyOrderLike - нечеткий поиск c учетом последовательности. Для работоспособности в ``map`` должно быть указано поле ``fuzzy``, содержащее токены для нечеткого поиска 

### Форматы запроса

* название содержит
```
{
       "offset": 0,
       "limit": 50,
       "filter": {
          "cond": {
               "attr": "longName",
               "like": "МАЯК"
             }
       }
} 
```

* Выбор организации, у которой есть адрес, название которого содержит "Девяткино" + подсчет точного кол-во таких объектов 
```
 {
       "offset": 0,
       "limit": 50,
       "totalCount": true,
       "filter": {
          "and": [ 
             { "cond": {
                 "attr": "addresses",
                 "exists": {
                               "cond":{"attr": "fullAddress","like": "Девяткино"}
                         }
               }
               }
             ]
       }
    
} 
```

* название начинается ИЛИ инн = с сортировкой по указанным атрибутам
```
 {
    "limit": "10",
    "offset": "0",
    "filter": {"and": [
        {"cond": {"attr":"longName", "startsWith": "ООО"}},
        {"cond": {"attr":"inn", "eq": "7802199633"}}
        ]},
    "orders": [{"asc":"inn"}, {"desc":"longName"}]
} 
```

* объекты, удовлетворяющие указанному условию на указанную дату
```
{
       "offset": 0,
       "limit": 50,
       "filter": {
          "cond": {
               "feature": "pers_date_n_lt",
               "checkDay": "2019-02-01"
             }
       }
} 
```

### Использование параметров авторизации в системе

В различных частях системы, например, блоке filter в search-запросах, возможно использование данных текущей авторизации

| Свойство     | Описание  |
| ------------- | ------------------ | 
| AUTH_USERID | Идентификатор пользователя |
| AUTH_ORGID | Организация пользователя |
| AUTH_DEPID | Подразделение пользователя |

Пример:

```
{
    "totalCount": true,
    "offset": 0,
    "limit": 50,
    "filter": {
        "and": [
            {
                "cond": {
                    "attr": "org",
                    "eq": "{AUTH_ORGID}"
                }
            }
        ]
    }
}
```

### Формат ответов

```
{
    "offset": 0,
    "limit": 10,
    "objectsCount": 1,
    "classCode": "spbCompany",
    "objects": [
        {
            "id": 1800065421,
            "classCode": "spbCompany",
            "title": null,
            "data": {
                "ownerType": 0,
                "dateReg": "2012-04-09T20:00:00.000+0000",
                "educationType": null,
                "municipality": null,
            }
        }
     ]
}
```
