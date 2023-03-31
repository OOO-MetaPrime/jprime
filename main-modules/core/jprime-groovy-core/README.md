# Описание

Модуль выполенения groovy-скриптов

## Особенности

Модуль предусматривает выполнение groovy кода в безопасном режиме, так как называемой песочнице, без доступа к системным ресурсам и вызовам JVM

## Использование

```
  public void shellRun() {
    JPGroovyShell shell = JPGroovyShell.newInstance();
    shell.setVariable("x", 2);
    shell.setVariable("y", 3);
    Object result = shell.evaluate("z=x+y"); // 5
    Object z = shell.getVariable("z"); // 5
  }
```

## Запрещенные классы

Использование указанных классов и их методов запрещены в исполняемом скрипте:
- groovy.util.Eval
- java.lang.System

## Разрешенные классы

Разрешено использование только указанных классов и их методов в исполняемом скрипте:
- java.math.BigInteger
- java.math.BigDecimal
- java.lang.String
- java.lang.Byte
- java.lang.Short
- java.lang.Integer
- java.lang.Long
- java.lang.Float
- java.lang.Double
- java.lang.Math
- java.lang.Boolean
- java.lang.StringBuilder
- java.util.Date
- java.util.Objects
- java.util.Optional
- org.apache.commons.lang3.StringUtils
- groovy.json.JsonGenerator
- groovy.json.JsonSlurper
- groovy.lang.IntRange
- groovy.lang.Range
- java.time.DayOfWeek
- java.time.Month
- java.time.Year
- java.time.YearMonth
- java.time.Instant
- java.time.LocalDate
- java.time.LocalDateTime
- java.time.LocalTime
- java.time.ZonedDateTime
- java.time.ZoneId
- java.time.Period
- java.time.format.DateTimeFormatter
- java.time.format.DateTimeFormatterBuilder
- java.time.format.FormatStyle
- java.time.format.TextStyle
- java.time.format.SignStyle
- java.util.ArrayList
- java.util.Collections
- java.util.HashMap
- java.util.HashSet
- java.util.TreeMap
- java.util.Arrays
- java.util.UUID
- java.sql.Timestamp
- java.sql.Date
- java.text.NumberFormat
- java.text.DecimalFormat
- java.text.DecimalFormatSymbols
- java.text.SimpleDateFormat

## Разрешенные пакеты

- java.util.stream
- com.google.common.collect
- groovy.xml
- mp.jprime.time

## Автоматический импорт

Следующие классы уже импортированны и могут использоваться в скрипте:

- groovy.json.JsonGenerator
- groovy.json.JsonSlurper
- java.time.LocalDate
- java.time.Period
- org.apache.commons.lang3.StringUtils

Следующие пакеты уже импортированны и могут использоваться в скрипте:

- mp.jprime.time
