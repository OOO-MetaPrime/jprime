# Модуль содержащий шрифты

> При добавлении нового шрифта необходимо так же добавить константу с путем к нему в `JPFonts.Paths`

## Использование шрифтов

Для загрузки шрифта рекомендуется использовать `JPFonts`, передавая в метод `getFont(path)` константу соответствующего
шрифта из `JPFonts.Paths.*`.

* Пример:

```java

@Service
public class MyAwesomeClass {

  private JPFonts jpFonts;

  @Autowired
  private void setJpFonts(JPFonts jpFonts) {
    this.jpFonts = jpFonts;
  }

  public Resource loadFont() {
    Resource font = jpFonts.getFont(JPFonts.Paths.ROBOTO_MONO_PATH);
    if (font == null) {
      throw new JPRuntimeException("Font not found");
    }
  }
}
```

## Поддерживаемые шрифты:

| Семейство   | Тип    | Файл                             |
| ----------- | ------ | -------------------------------- |
| Roboto Mono | normal | RobotoMono-VariableFont_wght.ttf |