# Описание модуля

Модуль обработки файлов

## Парсер .csv файлов

Предоставляет сервис парсера `JPCsvParserService`.

### Принцип работы

Сервис имеет два перегруженных метода `parse`, один из которых является базовым, использует
настройки парсера `JPCsvReaderSettings` и дает возможность прочитать файл и получить его строки,
тогда же второй метод дополнительно позволяет использовать настройки `JPMapRules` и замаппить столбцы
с некоторой сущностью.

Если у файла нет заголовков столбцов, то необходимо использовать настройки `JPMapRulesIndexedBean` с указанием
индекса столбца, а если есть заголовки - с использованием `JPMapRulesNamedBean`.

### Пример создания настроек парсера

Такие настройки, как: кодировка, разделитель - являются обязательными.
Если установлен флаг игнорирования кавычек, то обязательно также указать символ кавычек.

Если в файле есть кавычки:

```Java
private final static JPCsvReaderSettings PARSER_SETTINGS = JPCsvReaderSettingsBean.newBuilder()
    .encoding(StandardCharsets.UTF_8.name())
    .delimiter(';')
    .ignoreQuite(Boolean.TRUE)
    .quote('\"')
    // ...
    .build();
```

Если в файле нет кавычек:

```Java
private final static JPCsvReaderSettings PARSER_SETTINGS = JPCsvReaderSettingsBean.newBuilder()
    .encoding(StandardCharsets.UTF_8.name())
    .delimiter(';')
    .ignoreQuite(Boolean.FALSE)
    // ...
    .build();
```

Также есть возможность указать значение в настройке skipLines, чтобы пропустить некоторое количество строк.
В случае, если установлен флаг наличия заголовка, строки будут пропущены уже после него.

### Пример создания правил маппинга

Если необходимо, правила маппинга позволяют связать один столбец с несколькими атрибутами сущности.

В случае, если столбцы указаны:

```java
  JPMapRules<String> RULES=JPMapRulesNamedBean.newBuilder()
    .addColumn("CODE",Boolean.TRUE)
    .addAttr(ATTR_CODE,JPType.STRING)
    .addAttr(ATTR_CODE_COPY,JPType.STRING)
    .addColumn("NAME",Boolean.FALSE)
    .addAttr(ATTR_NAME,JPType.STRING)
    .build();
```

В случае, если столбцы не указаны:

```java
  JPMapRules<Integer> RULES=JPMapRulesIndexedBean.newBuilder()
    .addColumn(1,Boolean.TRUE)
    .addAttr(ATTR_CODE,JPType.STRING)
    .addColumn(2,Boolean.FALSE)
    .addAttr(ATTR_NAME,JPType.STRING)
    .build();
```

## Выгрузка информации в .csv файл

### Базовый класс `JPCsvBaseWriter`

#### Базовые методы

* `public void write(Collection<T> values)` - запись коллекции значений в выходной поток с
  последующим очищением буфера
* `public void close()` - очищение буфера с последующим закрытием выходного потока

#### Необходимо реализовать в наследниках

* `protected abstract Collection<String[]> toBatch(Collection<T> values)` - метод преобразования входных значений в
  коллекцию массивов строк

#### Базовые настройки `JPCsvBaseWriterSettings`

Предоставляют возможность настройки параметров записи в файл:

* `separator` - Разделителя
* `quoteChar` - Символа кавычек
* `escapeChar` - Символа для экранирования кавычек
* `lineEnd` - Окончания строки

### Выгрузка `JPObjects` в .csv файл

> При создании `JPObjectCsvWriter` или `JPObjectCsvZipWriter` необходимо передавать <strong>упорядоченную</strong>
> коллекцию атрибутов.
> Именно она определяет порядок расположения атрибутов и заголовков в результирующем файле.

#### Выгрузка в 1 файл

Для выгрузки `JPObjects` в файл требуется `JPObjectCsvWriter`.

#### Выгрузка в архив файлов

Для выгрузки `JPObjects` в архив файлов требуется `JPObjectCsvZipWriter`.<br/>
Имена файлов внутри архива строятся по определенным правилам, на примере следующих файлов:
> Важный файл (1).csv<br/>
> Важный файл (2).csv

1. При создании экземпляра класса указывается `filesName` = "Важный файл".
2. К `filesName` через символ пробела в скобках добавляется порядковый номер файла, начиная с 1.
3. В конце добавляется расширение файла `.csv`

#### Базовый сервис выгрузки `JPObjects` - `JPObjectCsvWriterService`

Предназначен для упрощения выгрузки `JPObjects` в `InputStream` с использованием выше перечисленных классов.




