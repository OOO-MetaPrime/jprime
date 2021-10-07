package mp.jprime.fonts;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
class JPFontsTest {

  @Autowired
  private JPFonts jpFonts;

  @Test
  @DisplayName("Тест доступности шрифтов 'jprime-fonts'")
  void shouldReturnNonNull_forAllDeclaredFontsPaths() throws IllegalAccessException {
    Field[] fields = JPFonts.Paths.class.getDeclaredFields();
    for (Field field : fields) {
      if (field.getType().isAssignableFrom(String.class)) {
        String path = (String) field.get(null);
        assertNotNull(jpFonts.getFont(path), "Not found font by path=\"" + path + '\"');
      }
    }
  }

  @Lazy(value = false)
  @Configuration
  @ComponentScan(value = {"mp.jprime.fonts"})
  public static class Config {
  }

}