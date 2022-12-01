# Gradle-scripts

Список скриптов:

| Наименование                                      | Описание                      |
|---------------------------------------------------|-------------------------------|
| [versions](#versions)                             | Версии библиотек              |
| [dockers](#dockers)                               | Работа с докерами             |
| [jprime-settings](#jprime-settings)               | Обработчик проектных настроек |
| [main](#main)                                     | Базовые задачи                |
| [offline-build](#offline-build)                   | Сборка модулей без интернета  |
| [offline-plugin-manager](#offline-plugin-manager) | Оффлайн-менеджер пакетов      |
| [offline-subprojects](#offline-subprojects)       | Оффлайн-настройки подпроектов |
| [subprojects](#subprojects)                       | Настройки подпроектов         |

# Описание скриптов:

# <a name="versions"></a>

## Описание

Версии всех библиотек, используемых в системе.

Поддерживает такие вещи, как:

* Версии;
* Библиотеки;
* Группы библиотек.

Без подключения зависимостей, перечисленных в `libs-versions`, приложение упадёт.

## Подготовка

Необходимо создать или повторно использовать файл(ы) с расширением `.toml`. В этих файлах можно описать версии,
зависимости и группы.

## Подключение

В базовом `settings.gradle` библиотеки и их версии подключаются следующим образом:

```groovy
dependencyResolutionManagement {
  versionCatalogs {
    libs {                                                     // Каталог из которого будем брать все данные
      from(files("jprime/gradle-scripts/versions/libs.toml")) // Путь до файла с настройками
    }
  }
}
```

## Использование

```
versionCatalogs.[group].name[.name]
```

* versionCatalogs - каталог с данными;
* group - группа (versions, libraries (не нужно указывать), bundles);
* name - наименование (с разделителями через `.`).

В проекте все настройки можно использовать следующим образом:

### Версии

Объявление:

```
[versions]
springBootVersion = "2.6.2"
```

Использование:

```groovy
libs.versions.springBootVersion.get()
```

### Библиотеки

Объявление:

```
[libraries]
haulmont-yarg = { module="com.haulmont.yarg:yarg", version.ref="yargVersion" }
```

Использование:

```groovy
libs.haulmont.yarg
```

### Группы (подключение сразу нескольких библиотек)

Объявление:

```
[bundles]
jwt-v1 = ['jjwt-api', 'jjwt-impl', 'jjwt-jackson']
```

Использование:

```groovy
libs.bundles.jwt.v1
```

# <a name="dockers"></a>

Список выполняемых задач:

| Наименование      | Группа        | Описание                  |
|-------------------|---------------|---------------------------|
| dockerImages      | build offline | Создаёт архивы с докерами |
| buildLocalDockers | build offline | Собирает докеры           |

## Описание

Данный скрипт отвечает за создание и сборку docker-образов.

## Подключение

В корневой `build.gradle`, данный скрипт подключается, как:

```groovy
apply from: "/jprime/gradle-scripts/dockers.gradle"
```

## dockerImages

### Описание

Данный таск отвечает за генерацию docker-образов. В ходе выполнения таска, в корневом каталоге будет создана папка
`docker-images` со всеми созданными образами докеров.

### Настройка

TODO

## buildLocalDockers

### Описание

Данный таск отвечает за сборку docker-образов.

### Предварительная настройка

TODO

### Настройка

TODO

# <a name="jprime-settings"></a>

## Описание

Данный скрипт отвечает за генерацию и настройку .yml настроек сервисов. Идея данного решения заключается в том, чтобы
вместо двух файлов настроек (`application.yml` и `application-distrib.yml`)
вести одну настройку (`application.yml`).

На этапе сборки проекта, при переносе папок `resource` сервисов файл `application.yml` заполняется данными
из `jprime.properties`
(для локального запуска), а также создаётся файл `application-distrib.yml` (для запуска на сервере) без заполнения
данными

## Предварительная настройка окружения

Для использования данного скрипта, необходимо в корневом проекте создать папку `services-settings`, в которой необходимо
разместить файл `jprime.properties` с настройками, вида:

```properties
JPRIME_SERVICE_PORT=8081
JPRIME_APPLICATION_NAME=sandbox-service
```

Эти настройки будут автоматически подставляться в итоговый `application.yml`.

## Предварительная настройка `application.yml`

Для поддержания профилей (для локального запуска и запуска на сервере), **Spring** использует параметры `on-profile`
, `location`, которые необходимо задать следующим образом:

```yaml
spring:
  config:
    activate:
      on-profile: ${SPRING_PROFILE}
    location: classpath:${SPRING_PROFILE_LOCATION}.xml
```

При таком обозначении профиль будет автоматически проставляться при копировании файлов `application.yml`
и `application-distrib.yml`, как `default` и `distrib` соответственно, а `location`, как `application`
и `application-distrib`.

Также необходимо указать `logging.config`:

```yaml
logging:
  config: classpath:${LOGGING_CONFIG}.xml
```

При таком обозначении, `LOGGING_CONFIG` будет подставляться, как `log4j2` и `log4j2-distrib` соответственно.

Также, если вы хотите использовать какие-нибудь настройки только по умолчанию или только при запуске с сервера, вы
можете воспользоваться флагами `DEFAULT_ENABLED` и `DISTRIB_ENABLED`, которые возвращают `true` или `false` в
зависимости от задачи.

## Подключение

В `build.gradle` вашего сервиса, данный скрипт подключается, как:

```groovy
apply from: "${rootProject.projectDir}/jprime/gradle-scripts/jprime-settings.gradle"
```

## Особенности работы

Данный скрипт содержит 2 задачи: `copyDistrib` и `copyDefault`.

`copyDistrib` создаёт файл `application-distrib.yml`, для запуска на сервере.

`copyDefault` создаёт файл `application.yml`, для локального запуска.

Эти задачи подключаются к задаче `processResources` и выполняются по `finalizedBy`.

# <a name="main"></a>

Список выполняемых задач:

| Наименование   | Группа | Описание                               |
|----------------|--------|----------------------------------------|
| buildWithoutUi | build  | Сборка проекта без повторной сборки ui |

## Описание

Данный скрипт хранит базовые задачи для работы с проектами.

## Подключение

В корневой `build.gradle`, данный скрипт подключается, как:

```groovy
apply from: "jprime/gradle-scripts/main.gradle"
```

## buildWithoutUi

### Описание

Данная задача позволяет собрать проект без повторной сборки ui. При этом важно отметить, что сама по себе задача ничего
не делает, кроме передачи флага `ignoreUI` со значением `true`. Так что вся основная логика сборки ui ложится на сам ui.
Как пример, проект `neatui` и скрипт `ui-modules/jprime-neatui/neatui.gradle`.

# <a name="offline-build"></a>

Список выполняемых задач:

| Наименование      | Группа        | Описание                                              |
|-------------------|---------------|-------------------------------------------------------|
| cacheToMavenLocal | build offline | Подготовка всех зависимостей для сборки без интернета |
| buildOffline      | build offline | Сборка проекта без интернета                          |

## Описание

Данный скрипт позволяет подготовить проект для сборки без интернета, а также запустить проект без интернета.

## Подключение

В корневой `build.gradle`, данный скрипт подключается, как:

```groovy
apply from: "jprime/gradle-scripts/offline-build.gradle"
```

## cacheToMavenLocal

### Описание

Данная задача достаёт все библиотеки из `gradle-cache`, копирует их в папку `dependencies` корневого каталога, и в конце
архивирует папку с её содержимым в архив `dependencies.zip`.

P.S. по умолчанию считается, что `gradle-cache` находится в папке `gradle.gradleUserHomeDir`

+ `caches/modules-2/files-2.1`.

### Предварительная подготовка

Для того чтобы не архивировать лишних библиотек, рекомендуется очистить `gradle-cache` (можно просто удалить
папку `files-2.1`), и затем выполнить сборку проекта.

## buildOffline

### Описание

Данная задача позволяет собрать проект без доступа в интернет. Но при этом, сама по себе эта задача ничего не делает,
кроме передачи флага `offline` со значением `true`.

> Чтобы полностью отключить доступ в интернет, рекомендуется выполнять задачу с флагом: `--offline`:

```bash
./gradlew --offline buildOffline
```

Для того чтобы всё работало, необходимо в проект добавить локальные репозитории, например:

```groovy
subprojects {
  repositories {
    maven {
      url "${rootProject.rootDir}/project-modules/dependencies/"
      metadataSources {
        mavenPom()
        artifact()
      }
    }
    mavenCentral()
  }
}
```

А также не забыть перенастроить менеджер плагинов (в `settings.gradle`):

```groovy
pluginManagement {
  repositories {
    maven {
      url 'project-modules/dependencies/'
    }
    gradlePluginPortal()
  }
}
```

В таком случае, библиотеки сначала будут искаться в локальных репозиториях. Если их не будет, но будет подключение к
интернету, библиотеки будут качаться с удалённых репозиториев. Если подключения к интернету не будет (передан
флаг `--offline`), сборка будет падат с ошибкой на этапе конфигурации проекта.

### Подключение

```groovy
apply from: "${rootProject.rootDir}/jprime/gradle-scripts/offline-build.gradle"
```

# <a name="offline-plugin-manager"></a>

### Описание

Добавляет возможность брать плагины из локального репозитория. Является вспомогательным решением для
задачи [offline-build](#offline-build).

### Подключение

Подключать в корневой `settings.gradle` таким образом:

```groovy
pluginManagement {
  apply from: 'jprime/gradle-scripts/offline-plugin-manager.gradle', to: pluginManagement
}
```

> Подключение pluginManagement должно быть самой первой строчкой данного файла.

# <a name="offline-subprojects"></a>

Данный скрипт описывает базовые настройки подпроектов с учётом offline-запуска. Является вспомогательным решением для
задачи [offline-build](#offline-build).

В себя подключает следующие плагины:

* [subprojects](#subprojects)
* [dockers](#dockers)
* [offline-build](#offline-build)

### Подключение

В корневой `build.gradle`, данный скрипт подключается, как:

```groovy
apply from: "jprime/gradle-scripts/subprojects.gradle"
```

# <a name="subprojects"></a>

Данный скрипт описывает базовые настройки подпроектов.

### Подключение

В корневой `build.gradle`, данный скрипт подключается, как:

```groovy
apply from: "jprime/gradle-scripts/subprojects.gradle"
```