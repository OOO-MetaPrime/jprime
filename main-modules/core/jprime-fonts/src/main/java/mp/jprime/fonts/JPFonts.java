package mp.jprime.fonts;

import mp.jprime.exceptions.JPRuntimeException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class JPFonts {

  private ApplicationContext applicationContext;

  @Autowired
  private void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  /**
   * Возвращает {@link Resource шрифт} по переданному {@link JPFonts.Paths пути},
   * либо {@code null}, если шрифт по указанному пути не найден
   *
   * @param path {@link JPFonts.Paths путь}
   * @return {@link Resource шрифт}
   */
  public Resource getFont(String path) {
    try {
      Resource[] resources = applicationContext.getResources(ResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX + path);
      return resources.length > 0 ? resources[0] : null;
    } catch (IOException e) {
      throw new JPRuntimeException("Cant load font from resource \"" + path + "\": " + e.getMessage(), e);
    }
  }

  /**
   * Пути к доступным шрифтам
   */
  public interface Paths {
    String ROBOTO_MONO_PATH = "fonts/RobotoMono/RobotoMono-VariableFont_wght.ttf";
  }

}
