# Основной модуль JPrime

Содержит типовую логику, API и базовые реализации основых функций системы

## Описание
 
Содержит логику работы с метой + основные интерфейсы

* Загружает метаописание из xml в момент старта
* Предоставляет базовое
* Загружает маппинг метаописания из xml в момент старта 

### Содержимое ресурсов

* jwt - для публичных ключей проверки JWT
* meta - для xml c метаописанием
* metamaps - для xml с маппингом метаописания

## Системные типы

| Код  | Java тип     | Описание  |
| -----| ------------- | ------------------ |
| backReference | - | Обратная ссылка |
| biginteger | BigInteger | Длинное целочисленное |
| boolean | Boolean | Да/Нет |
| date | LocalDate | Дата | 
| datetime | LocalDateTime | Полная дата (без учета часового пояса) | 
| double | Double | Вещественное (64 бита) | 
| file | - | Файл |
| float | Float | Вещественное (32 бита) |
| integer | Integer | Целочисленное (32 бита) |
| long | Long | Целочисленное (64 бита) | 
| string | String | Строка | 
| timestamp | LocalDateTime | Полная дата (c учетом часового пояса) |
| time | LocalTime | Время |
| uuid | UUID | Глобальный идентификатор |  
| virtualReference | - | Виртуальная ссылка |

### Виртуальная ссылка

| Код  | Java тип     | Описание  |
| -----| ------------- | ------------------ | 
| virtualReference | - | Глобальный идентификатор |

Виртуальная ссылка позволяет отобразить значение связанного объекта, как свойство текущего объекта
Для построения виртуальной ссылки необходима прямая ссылка на класс, свойство которого хотим виртуализировать

Для полноценной работы виртуальной ссылки необходимо также указать `virtualReference` (путь виртуальной ссылки) +
`virtualType` (тип итогового значения виртуальной ссылки)

Пример: 

`sprWorker`

| Код  | Описание | 
| -----| ------------- | 
| ouid | Идентификатор |
| fio | Фамилия/Имя/Отчество |

`sprOrg`

| Код  | Описание | 
| -----| ------------- | 
| ouid | Идентификатор |
| name | Название |
| director | Ссылка на sprWorker.ouid |
| directorFIO | Виртуальная ссылка на sprWorker.fio |

, где

```
 <jpAttr><code>directorFIO</code><type>virtualReference</type><virtualReference>director.fio</virtualReference><virtualType>string</virtualType><length>100</length><description>ФИО директора</description></jpAttr>
```


### Глобальный идентификатор

| Код  | Java тип     | Описание  |
| -----| ------------- | ------------------ | 
| uuid | java.util.UUID | Глобальный идентификатор |

### Прямая ссылка (Nx1)

Прямая ссылка не реализуется отдельным типом, а расширяет свойства атрибута полями `refJpClass` и `refJpAttr`
Тип атрибута указывается один из простых (строка, целочисленное и т.д.), важно, что тип поля соответствовал типу атрибута класса, 
указанного в настройках  `refJpClass` и `refJpAttr` (кодовое имя класса и кодовое имя атрибута соответственно)

### Обратная ссылка

| Код  | Java тип     | Описание  |
| -----| ------------- | ------------------ | 
| backReference | - | Глобальный идентификатор |

Обратная ссылка не является полноценным свойством объекта: у него нет значения, хранящегося отдельно.
Это, скорее визуальная настройка, позволяющяя вывести все объекты другого класса, связанные с текущим объектом

Для полноценной работы обратной ссылки необходимо указать `refJpClass` и `refJpAttr` (кодовое имя класса и кодовое имя атрибута соответственно),
где указанный `refJpAttr` - является прямой ссылкой на идентификатор текущего класса 

### Строка

## Преобразование java типов

Для преобразовавания T1 в T2 разных классов рекомендуется использовать `mp.jprime.parsers.ParserService` 

```
  private ParserService parserService;

  @Autowired
  private void setParserService(ParserService parserService) {
    this.parserService = parserService;
  }
  ...
  
  private LocalDate getLocalDate(Object value) {
    if (value == null) {
      return null;
    }
    return parserService.parseTo(LocalDate.class, value);
  }
```

Само преобразование описывается наследником от `mp.jprime.parsers.TypeParser`. Причем справочник может расширятся, как в коде JPrime, так и в прикладном коде 

## Метаописание

Описание структуры данных и их взаимосвязей. Позволяет статически или динамически описывать классы, их свойства (атрибуты) и связи между ними. 

### Структура меты

Описание класса ``JPClass``

| Свойство     | Описание  |
| ------------- | ------------------ | 
| guid | Глобальный идентификатор |
| code | Кодовое имя класса |
| pluralCode |  Множественный код кодового имени |
| qName | Полный код класса |
| name | Название класса |
| shortName | Короткое название класса |
| description | Описание класса |
| jpPackage | Настройки доступа |
| attrs | Список атрибутов класса |

Описание атрибута класса ``JPAttr``

| Свойство     | Описание  |
| ------------- | ------------------ | 
| guid | Глобальный идентификатор |
| code | Кодовое имя атрибута |
| qName | Полный код атрибута |
| name | Название атрибута |
| shortName | Короткое название атрибута |
| description | Описание атрибута |
| jpPackage | Настройки доступа |
| identifier | Признак идентификатора |
| mandatory | Признак обязательности |
| type | Тип атрибута |
| refJpClass | Кодовое имя класса, на который ссылается |
| refJpAttr | Кодовое имя атрибута ссылочного класса |
| refJpClass | Кодовое имя класса, на который ссылается |
| virtualReference | Путь виртуальной ссылки |
| virtualType | Тип виртуальной ссылки |
| length | Длина (для строковых полей, в том числе для строковых виртуальных) |

### Способы описания меты

#### Аннотации

* Аннотация детализирует наследника от ``mp.jprime.meta.JPMeta``
 
 ```
@JPClass(
    guid = "cc53f898-ceec-49b3-81a3-43eb3fdc43f0",
    code = "userEvent",
    pluralCode = "userEvents",
    qName = "common.userEvent",
    name = "Таблица с информацией о пользовательских событиях",
    shortName = null,
    description = null,
    jpPackage = "adminAccess",
    attrs = {
        @JPAttr(
            guid = "a6f40c06-e41f-4003-a4df-a72d304adc7e",
            code = "eventGuid",
            type = JPType.UUID,
            identifier = true,
            qName = "common.userevent.eventGuid",
            name = "Гуид события",
            shortName = null,
            description = null            
        )
    }
)
```    
        
#### XML

* xml файл, указанной структуры, должен быть размещен в src/main/resources/meta

```
<?xml version="1.0" encoding="utf-8"?>
<jpClasses>
  <jpClass>
    <guid>cc53f898-ceec-49b3-81a3-43eb3fdc43f0</guid>
    <code>userEvent</code>
    <pluralCode>userEvents</pluralCode>
    <qName>common.userEvent</qName>
    <name>Таблица с информацией о пользовательских событиях</name>
    <jpAttrs>
      <jpAttr>
        <guid>a6f40c06-e41f-4003-a4df-a72d304adc7e</guid>
        <code>eventGuid</code>
        <type>uuid</type>
        <length>0</length>
        <identifier>true</identifier>
        <qName>common.userevent.eventGuid</qName>
        <name>Гуид события</name>
      </jpAttr>
    </jpAttrs>
  </jpClass>
</jpClasses>      
```

## Описание маппинга

Настройка хранения данных в определенном хранилище

### Структура маппинга

Описание класса ``JPClassMap``

| Свойство     | Описание  |
| ------------- | ------------------ | 
| code | Кодовое имя класса |
| storage | Кодовое имя хранилища |
| map | Мап на хранилище |
| attrs | Список маппинга атрибутов класса |

Описание атрибута класса ``JPAttrMap``

| Свойство     | Описание  |
| ------------- | ------------------ | 
| code | Кодовое имя атрибута |
| map | Мап на хранилище |
| fuzzyMap | Мап на поле с индексами нечеткого поиска |
| cs | Регистр значений |
| readOnly | Запрет на изменение значений |

### Способы описания маппинга

#### Аннотации

* Аннотация детализирует наследника от ``mp.jprime.meta.JPMeta``

```
@JPClassMap(
    code = "userEvent",
    storage = "userevents",
    map = "userevent",
    attrs = {
        @JPAttrMap(
            code = "eventGuid",
            map = "eventguid"
        )
    }
)
```

#### XML

* xml файл, указанной структуры, должен быть размещен в src/main/resources/metamaps

```
<?xml version="1.0" encoding="utf-8"?>
<jpClassMaps>
  <jpClassMap code="userEvent" storage="userevents" map="userevent">
    <jpAttrMaps>
      <jpAttrMap code="eventGuid" map="eventguid"/>
    </jpAttrMaps>
  </jpClassMap>
</jpClassMaps>
```

## Настройки доступа

Настройки доступа разрешительные и запретительные для определенных ролей

### Структура настроек

| Свойство     | Описание  |
| ------------- | ------------------ | 
| code | Уникальный код настроек доступа |
| access | Список настроек по ролям|

| Свойство     | Описание  |
| ------------- | ------------------ | 
| type | Тип настройки: разрешительный или на запрет |
| role | Кодовое имя роли |
| read | Доступ на чтение |
| create | Доступ на создание |
| update | Доступ на изменение |
| delete | Доступ на удаление |

### Способы описания настроек

#### Аннотации

* Аннотация детализирует наследника от ``mp.jprime.security.JPSecuritySettings``

```
@JPPackages(
    {
        @JPPackage(
            code = "commonAccess",
            access = {
                @JPAccess(
                    type = JPAccessType.PERMIT,
                    role = "AUTH_ACCESS",
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        ),
        @JPPackage(
            code = "commonDenied",
            access = {
                @JPAccess(
                    type = JPAccessType.PROHIBITION,
                    role = "AUTH_ACCESS",
                    read = true,
                    create = true,
                    update = true,
                    delete = true
                )
            }
        )
    }
)
```

#### XML

* xml файл, указанной структуры, должен быть размещен в src/main/resources/security

```
<?xml version="1.0" encoding="utf-8"?>

<jpSecurity>
  <jpPackages>
    <jpPackage code="commonAccess">
      <jpPermitAccess>
        <jpAccess read="true" create="true" update="true" delete="true" role="AUTH_ACCESS"/>
      </jpPermitAccess>
    </jpPackage>
    <jpPackage code="commonDenied">
      <jpProhibitionAccess>
        <jpAccess  read="true" create="true" update="true" delete="true" role="AUTH_ACCESS"/>
      </jpProhibitionAccess>
    </jpPackage>
  </jpPackages>
</jpSecurity>
```

## JPrime API

Взаимодействие с объектами происходит через метамодель и кодовые имена классов/атрибутов

### Чтение данных

Пример:
```
  @Autowired
  private JPObjectRepository repo;
  
  JPSelect jpSelect = JPSelect
        .from(jpClass.getCode())
        .auth(authInfo)
        .where(Filter.attr(attrCode1).eq(value1))
        .limit(limit)
        .offset(offset)
        .orderBy(attrCode2, OrderDirection.DESC)
        .build()

  return repo.getAsyncList(jpSelect)  
```

### Создание данных

Пример:
```
  @Autowired
  private JPObjectRepository repo;
  
  JPCreate jpCreate = JPCreate
        .create(jpClass.getCode())
        .auth(authInfo)
        .set(attrCode1, value1)
        .set(attrCode2, value2)
        .build()

  return repo.asyncCreate(jpCreate)  
```

### Обновление данных

Пример:
```
  @Autowired
  private JPObjectRepository repo;
  
  JPUpdate jpUpdate = JPUpdate
        .update(JPId.get(jpClass.getCode(), objectId))
        .auth(authInfo)
        .set(attrCode1, value1)
        .set(attrCode2, value2)
        .build()

  return repo.asyncUpdate(jpUpdate)  
```

### Удаление данных

Пример:
```
  @Autowired
  private JPObjectRepository repo;
  
  JPDelete jpDelete = JPDelete
        .delete(JPId.get(jpClass.getCode(), objectId))
        .auth(authInfo)
        .build();

  return repo.asyncDelete(jpDelete)  
```

### Поиск данных


#### Критерии поиска

| Свойство     | Описание  | backReference | biginteger | boolean | date | datetime | double | float | integer | long | string | timestamp | time | uuid |
| ------------- | ------------------ |  ------------- |  ------------- |  ------------- |  ------------- |  ------------- |  ------------- |  ------------- |    ------------- |  ------------- |  ------------- |  ------------- | ------------- | ------------- |
| isNull | Значение не указано | - | + | + | + | + | + | + | + | + | + | + | + | + |
| isNotNull | Значение указано | - | + | + | + | + | + | + | + | + | + | + | + | + |
| eq | Значение равно указанному | - | + | + | + | + | + | + | + | + | + | + | + | + |
| neq | Значение не равно указанному | - | + | + | + | + | + | + | + | + | + | + | + | + |
| gt | Значение больше указанного | - | + | - | - | - | + | + | + | + | - | - | - | - |
| gte | Значение больше или равно указанному | - | + | - | - | - | + | + | + | + | - | - | - | - |
| lt | Значение меньше указанному | - | + | - | - | - | + | + | + | + | - | - | - | - |
| lte | Значение меньше или равно указанному | - | + | - | - | - | + | + | + | + | - | - | - | - |
| eqYear | Значение равно указанному году | - | - | - | + | + | - | - | - | - | - | + | - | - |
| neqYear | Значение не равно указанному году  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gtYear | Значение больше указанного года  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gteYear | Значение больше или равно указанному года | - | - | - | + | + | - | - | - | - | - | + | - | - |
| ltYear | Значение меньше указанного года | - | - | - | + | + | - | - | - | - | - | + | - | - |
| lteYear | Значение меньше или равно указанного года | - | - | - | + | + | - | - | - | - | - | + | - | - |
| eqMonth | Значение равно указанному месяцу  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| neqMonth | Значение не равно указанному месяцу  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gtMonth | Значение больше указанного месяца  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gteMonth | Значение больше или равно указанному месяца  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| ltMonth | Значение меньше указанного месяца  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| lteMonth | Значение меньше или равно указанного месяца  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| eqDay | Значение равно указанному дню | - | - | - | + | + | - | - | - | - | - | + | - | - |
| neqDay | Значение не равно указанному дню  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gtDay | Значение больше указанного дня  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| gteDay | Значение больше или равно указанному дню  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| ltDay | Значение меньше указанного дня  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| lteDay | Значение меньше или равно указанного дню  | - | - | - | + | + | - | - | - | - | - | + | - | - |
| between | Значение находится в указанном диапазоне | - | + | - | - | - | + | + | + | + | - | - | - | - |
| exists | Ссылочный атрибут содержит объекты по указанному условию | + | - | - | - | - | - | - | - | - | - | - | - | - |
| notExists | Ссылочный атрибут не содержит объекты по указанному условию | + | - | - | - | - | - | - | - | - | - | - | - | - |
| like | Значение содержит указанное | - | - | - | - | - | - | - | - | - | + | - | - | - |
| fuzzyLike | Нечеткий поиск | - | - | - | - | - | - | - | - | - | + | - | - | - |
| fuzzyOrderLike | Нечеткий поиск с учетом порядка слов | - | - | - | - | - | - | - | - | - | + | - | - | - |
| startsWith | Начинается с указанного | - | - | - | - | - | - | - | - | - | + | - | - | - |
| in | Значение находится в указанном списке | - | + | + | + | + | + | + | + | + | + | + | + | + | 

Дополнительно, для ссылочных атрибутов со свойствами `refJPClass`+`refJPAttr` так же можно использовать
для поиска по ссылочным объектам: `exists` и `notExists`

Значения поиска для `virtualReference` соответствуют конечному типу данных, указанному в 'virtualType'

## Бины

Любые данные после получения из хранилища сериализуются в `mp.jprime.dataaccess.beans.JPObject`
При необходимости создать кастомный бин достаточно наследовать его от `mp.jprime.dataaccess.beans.JPObject` 
и указать аннотацией для каких классов он будет использоваться

```
@JPClassesLink(
    jpClasses = {Address.CLASS_CODE}
)
public class AddressBean extends JPObject {

}
``` 

Если на метаописание настроены два разных бина, один из которых является наследником другого, то для обработки будет использоваться наследник

### Аннотация `mp.jprime.annotations.JPBeanInfo`

С помощью аннотации `mp.jprime.annotations.JPBeanInfo` можно:

#### Свойство `defaultJpAttrCollection`

Указывает набор атрибутов, которые будут всегда получаться из хранилища в случае ссылки на текущий класс  

Пример:
```
@JPClassesLink(
    jpClasses = {Users.CLASS_CODE}
)
@JPBeanInfo(
    defaultJpAttrCollection = {Users.Attr.FULLNAME}
)
public class UsersBean extends JPObject {

}
```

Пояснение:

При наличии в любом классе атрибута, ссылающегося на users, при получении объекта этого класса, всегда будет дополнительно заполнятся linkedData

```
{
    "id": 9970000303,
    "classCode": "companyJobFair",
    "data": {
        "...
    },
    "linkedData": {
        "userOwnerId": {
            "id": 9999999912,
            "classCode": "users",
            "data": {
                "fullName": "Роман",
                "userId": 11111
            }
        }
    }   
}    
``` 

## Хендлера операций над объектами

Хендлер над операциями CRUD предоставляет собой реализацию интерфейса `JPClassHandler` и позволяет обработать следущие события
```
  void beforeCreate(JPCreate query);

  void beforeUpdate(JPUpdate query);

  void afterCreate(Comparable newObjectId, JPCreate query);

  void afterUpdate(JPUpdate query);

  void beforeDelete(JPDelete query);

  void afterDelete(JPDelete query);
```
 
### Связь java-хендлера и метаописания класса

Хендлер используется для обработки событий над объектами класса, указанных в аннотации `JPClassesLink`

```
@JPClassesLink(
    jpClasses = {PersBook.CLASS_CODE}
)
public class PersBookHandler extends JPClassHandlerBase {
  ...
}
```

При необходимости вызвать хендлер для всех объектов системы следует использовать следующий синтаксис

```
@JPClassesLink(
    jpClasses = {"*"}
)
public class CommonHandler extends JPClassHandlerBase {
  ...
}
```

Если на метаописание настроены два разных хендлера, один из которых является наследником другого, то для обработки будет использоваться наследник 

## Валидаторы

Перед действиями над объектами есть возможность произвести проверки с помощью валидаторов `mp.jprime.dataaccess.validators.JPObjectValidator`

Вызов валидатора происходит перед всеми остальными действиями и по-умолчанию производится перед открытием транзации (если она не была принудительно открыта ранее)

### Реализация валидатора

Валидатор реализует указанный выше интерфейс, обычно является наследником класса `mp.jprime.dataaccess.validators.JPObjectValidatorBase` и применяется к объектам класса, указанных в аннотации `JPClassesLink`

На один класс может быть настроено множество валидаторов, а один валидатор может применятся ко многим классам

```
/**
 * Проверка СНИЛС
 */
@JPClassesLink(
    jpClasses = {Contact.CLASS_CODE}
)
public class ContactValidator extends JPObjectValidatorBase {
  @Override
  public void beforeCreate(JPCreate query) {
    ...
  }
}
```

В случае некорректных ситуаций реализация обычно выбрасывает JPRuntimeException

Если на метаописание настроены два разных валидатора, один из которых является наследником другого, то для обработки будет использоваться наследник

### Вызов валидации в прикладном коде

Управлением всеми валидаторами осуществляется через `mp.jprime.dataaccess.validators.JPClassValidatorService`

```
  private JPClassValidatorService jpClassValidatorService;

  @Autowired(required = false)
  private void setJPClassValidatorService(JPClassValidatorService jpClassValidatorService) {
    this.jpClassValidatorService = jpClassValidatorService;
  }

  ...
  
  private void validate(JPCreate query) {
    if (query == null) {
      return;
    }
    jpClassValidatorService.beforeCreate(query);
  }
```

## Слушатели

Все события в системе являются ассинхронными. 

## Системные события

События инициируются и обрабатываются программным кодом и используются для передачи информации между сервисами

Описание в модуле `jprime-common-systemevents-starter` 

## Пользовательские события

Событие является наследником класса ``mp.jprime.events.userevents.JPUserEvent``
и может быть инициировано в любом сервисе системы 

События инициируются программным кодом и используются для отображении информации конечному пользователю 

### Инициализация события

```
  private UserEventPublisher eventPublisher;

  @Autowired(required = false)
  private void setUserEventPublisher(UserEventPublisher eventPublisher) {
    this.eventPublisher = eventPublisher;
  }

  ...
  
  private void publishEvent(Report r, AuthInfo authInfo) {
    if (eventPublisher == null) {
      return;
    }
    eventPublisher.publishEvent(JPUserEventData.newBuilder()
        .typeCode("report.created")
        .typeTitle("Отчет создан")
        .description("Отчет " + r.getFileTitle() + " сформирован")
        .objectClassCode("file")
        .objectId(r.getGuid())
        .userId(authInfo != null ? authInfo.getUserId() : null)
        .userDescription(authInfo != null ? authInfo.getUsername() : null)
        .build()
    );
  }
```

### Реализация UserEventPublisher

Существует две реализации передачи системных событий

* ``KafkaUserEventPublisher``. Поддержка обмена событиями через Kafka.

Включается опцией `jprime.events.userevents.kafka.enabled=true` и рекомендуется для микросервисных сборок

* ``ApplicationUserEventPublisher ``. Поддержка обмена событиями внутри приложения

Включается опцией `jprime.events.userevents.app.enabled=true` и рекомендуется для монолитных сборок

## Авторизация

Данные о пользователе предоставлены реализацией `mp.jprime.security.AuthInfo`, содержащей информацию о идентикаторе пользователя,
его имени, организации и подразделении

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