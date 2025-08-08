# Описание

Модуль работы с утилитами

## Назначение

Блок утилит позволяет легко опубликовать любую бизнесс-логику в виде сервиса, с описаниим входных и выходных параметров

## Общие настройки

Для публикации класса в качестве утилиты, необходимо пометить его интерфейсом ``mp.jprime.utils.JPUtil``
и аннотацией `@JPUtilLink` с указанием урла вызова, прав доступа и списка метаклассов, для которых актуальная утилита

```
@JPUtilLink(
    authRoles = "AUTH_ACCESS",
    code = "test/testutil",
    jpClasses = "testClass",
    title = "Тестовая утилита"
)
```

Выполняющий метод (или методы утилиты в случае многошаговости) помечаются аннотацией `@JPUtilModeLink`

```
  @JPUtilModeLink(
      code = "check",
      title = "Проверка",
      outClass = Out.class
  )
  public Mono<Out> check(JPUtilCheckInParams in, AuthInfo auth) {
    check(in.getId(), auth);
    return new Out();
  }
```

с указанием кода выполняемого шага

### Настройки JPUtilLink

| Код         | Описание                                     |
|-------------|----------------------------------------------|
| code        | Уникальный код утилиты                       |
| title       | Название утилиты                             |
| qName       | QName утилиты                                |
| uni         | Признак универсального применения            |
| jpClasses   | Список классов, для которых доступна утилита |
| jpClassTags | Теги классов, обрабатываемые этой утилитой   |
| jpPackage   | Настройки доступа                            |
| authRoles   | Роли, имеющиеся доступ к этой утилите        |

### Настройки JPUtilModeLink

| Код             | Описание                                         |
|-----------------|--------------------------------------------------|
| jpPackage       | Настройки доступа                                |
| authRoles       | Роли, имеющиеся доступ к этому шагу              |
| code            | Уникальный код шага                              |
| title           | Название шага                                    |
| qName           | QName шага                                       |
| actionLog       | Признак логирования действий                     |
| type            | Тип доступности шага (список/объект/произвольно) |
| jpAttrs         | Настройки доступа на атрибутах                   |
| confirm         | Сообщение перед запуском шага                    |
| inParams        | Список входящих параметров                       |
| infoMessage     | Сообщение на форму утилиты                       |
| outClass        | Выходной класс параметров                        |
| outCustomParams | Список кастомных исходящих параметров            |

## Описание методов

Обязательно наличие бинов с входными и выходными параметрами, корректно преобразующихся из/в JSON

Кроме того, опционально в методе можно описать

* AuthInfo - Данные авторизации `mp.jprime.security.AuthInfo`

* ServerWebExchange - Данные http запроса `org.springframework.web.server.ServerWebExchange`

### Метод check

С помощью этого метода можно управлять предварительной проверкой возможности запуска утилиты. Для запрета запуска:

* метод должен бросить любое исключение (не рекомендуется)
* вернуть `JPUtilCheckOutParams` c `denied == true` (рекомендовано)

Если в утилите нет метода, помеченного `code = "check"`, то по умолчанию код c `mode/check` отработает с 200 кодом
и результатом `denied: false`

### Метод inParamsDefValues

С помощью этого метода можно получить значения по умолчанию для утилиты.

* метод должен вернуть `JPUtilDefValuesOutParams` с полем `result` в котором лежит json объект со значениями по умолчанию

Вызов этого метода регулируется флагом `isInParamsDefValues` в рамках аннотации `@JPUtilModeLink` основного метода утилиты.
По умолчанию, значение флага `false`. 

Важно, чтобы формат и наименования полей объекта, возвращаемого методом `inParamsDefValues` совпадали со значениями
во входящем объекте основного методы утилиты и с параметрами аннотации `@JPUtilModeLink`.
Например, нам необходимо в утилиту передать значения по умолчанию для полей СНИЛС и Телефон, вот как это будет выглядеть:
Параметры утилиты

```java
@JPUtilModeLink(code = "update",
      title = "Обновить электронный сертификат",
      type = JPAppendType.OBJECT,
      inParams = {
          @JPParam(
              code = mp.jprime.common.JPParam.OBJECT_CLASS_CODE,
              type = JPType.STRING,
              description = "кодовое имя метакласса объекта/ов",
              qName = UTIL_CODE + ".in.objectClassCode"
          ),
          @JPParam(
              code = mp.jprime.common.JPParam.OBJECT_IDS,
              type = JPType.STRING,
              description = "идентификатор объекта",
              qName = UTIL_CODE + ".in.objectIds"
          ),
          @JPParam(
              code = "snils",
              type = JPType.STRING,
              description = "СНИЛС",
              qName = UTIL_CODE + ".in.snils"
          ),
          @JPParam(
              code = "phoneNumber",
              type = JPType.STRING,
              description = "Номер телефона",
              qName = UTIL_CODE + ".in.phoneNumber"
          )
      },
      outClass = JPUtilJPIdOutParams.class
    )
```
То поля объекта со значениями по умолчанию должны выглядеть так
```java
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Out {
    private String snils;
    private String phoneNumber;
}
```
Т.е. имя поля в аннотации `@JPUtilModeLink` должно совпадать с соответствующим именем параметра в классе возвращаемого з
Далее объект Out передается в поле `result` объекта `JPUtilDefValuesOutParams`.

Опционально, если пользователь обязан внести изменения в поля, можно передать флаг `changeNeed`

### Входные параметры

Класс входящих параметров представляет собой реализацию `mp.jprime.utils.JPUtilInParams`, обычно наследника
от `mp.jprime.utils.BaseJPUtilInParams`, и может содержать произвольные параметры.
Для корректной публикации и работы утилиты в реалтайме все входящие параметры необходимо описать в
интерфейсе `@JPUtilModeLink.inParams`

#### Особые параметры

`objectClassCode` и `objectIds` заполняются данными объектов, для которых вызыватся утилита
`rootObjectClassCode` и `rootObjectId` заполняются данными корневого объекта

* rootObjectClassCode - кодовое имя метакласса корневого объекта
* rootObjectId - идентификатор корневого объекта
* objectClassCode - кодовое имя метакласса объекта/ов
* objectIds - идентификатор или идентификаторы объектов через запятую, в случае списка

Например, при вызове утилиты по списку документов личного дела, `root*` параметры будут заполнены идентификатором и
кодом ЛД
, а `object*` параметры идентификаторами и кодовым именем документов

### Выходные параметры

Класс входящих параметров представляет собой реализацию `mp.jprime.utils.JPUtilOutParams` и может содержать произвольные
параметры.
Для корректной публикации и работы утилиты в реалтайме все исходящие параметры необходимо описать в
интерфейсе `@JPUtilModeLink.outParams`

#### Типовые реализации `mp.jprime.utils.JPUtilOutParams`

| Код Название                | Код resultType    | Описание                                                            |
|-----------------------------|-------------------|---------------------------------------------------------------------|
| JPUtilCheckOutParams        | check             | результат check-проверки утилиты                                    |
| JPUtilMessageOutParams      | message           | результатом является только строка-описание                         |
| JPUtilJPCompConfOutParams   | jpCompConf        | результатом является отображение указанного конфигуратора компонент |
| JPUtilJPCreateOutParams     | jpCreate          | результатом является создание объекта указанного класса             |
| JPUtilJPIdOutParams         | jpId              | результатом является JPId объекта                                   |
| JPUtilJPObjectOutParams     | jpObject          | результатом является объект JPObject                                |
| JPUtilJPObjectListOutParams | jpObjectList      | результатом является список объектов JPObject                       |
| JPUtilJPUpdateOutParams     | jpUpdate          | результатом является переход на редактирование JPId                 |
| JPUtilCustomOutParams       | custom            | результатом является произвольный набор данных                      |
| JPUtilVoidOutParams         | void              | отсутствие результата                                               |
| JPUtilDefValuesOutParams    | inParamsDefValues | результатом является объект со значениями по умолчанию              |

## Привязка утилиты

| Свойство | Описание                            |
|----------|-------------------------------------|
| object   | Обработка 1 объекта                 |
| list     | Обработка списка объектов           |
| uni      | Обработка объекта и списка объектов |
| custom   | Произвольная                        |

### Логика отображения утилиты

1. в объекте, если `objectClassCode` попадает в настройку `jpClasses` + `type` = OBJECT/UNI
2. в списке, если `objectClassCode` попадает в настройку `jpClasses` + `type` = LIST/UNI
3. в списке, открытом по обратной ссылке: `rootObjectClassCode` попадает в настройку `jpClasses` + `type` = OBJECT/UNI +
   текущий атрибут в настройке `jpAttrs`

Для списка, открытого по обратной ссылке, работают правила п.2 ИЛИ п.3

#### Игнорирование условия на `objectClassCode`

1. утилита с признаком `uni=true`отображается для всех классов
2. утилита с настройкой`jpClassTags` отображается для классов, `jpClass.tags` которых пересекается с настройкой

## Настройка доступа

Запуск шага утилиты разрешен ролям, указанным в `authRoles` метода или же в `authRoles` утилиты, если настройки метода
пусты.
При пустых `authRoles` в обоих аннотациях запуск разрешен всем

## Использование утилит

### Использование в прикладном коде

Получение утилиты по ее коду осуществляется методом

```
 @Autowired
 private IJPUtilService jpUtilService;
 ... 
 IJPUtil util = jpUtilService.getUtil("test/testutil");
```

Вызов определенного шага утилиты из кода

```
    In in = new In();
    in.setValue("test");
    Mono<Out> out = jpUtilService.apply("test/testutil", "print", in);
```

### Вызов по REST

Детальнее см. `jprime-utils-rest`

`POST utils/v1/<код утилиты>/mode/<код шага>`