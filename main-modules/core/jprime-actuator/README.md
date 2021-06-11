# Описание

Модуль мониторинга состояния сервиса. Основан на *Spring Boot Actuator*

## Подключение

Кроме стандартной информации, публикуемой *Spring Boot Actuator*, 
может также публиковать следующую информацию о сборке:

* дата сборки
* версия JPrime*
* версия NeatUI*
* проектная версия*

\* - короткий хэш коммита

Для этого необходимо при подключении в `build.gradle`, кроме добавления модуля в зависимости, 
так же добавить _следующий_ код в блок ``bootJar{...}``:

```groovy
  apply from: "${rootProject.project(':jprime-actuator').projectDir}\\actuator.gradle"
  doFirst {
    manifest {
      Map<String, GString> attrs = common.getManifestAttrs()
      attributes(attrs)
      // При наличии подмодулей:
      Map<String, GString> submodulesVersion = common.getSubmodulesVersion()
      attributes(submodulesVersion, "${SUBMODULES_SECTION_NAME}")
    }
  }
```