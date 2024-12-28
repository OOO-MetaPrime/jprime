package mp.jprime.groovy;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyCodeSource;
import groovy.lang.GroovyShell;
import groovy.util.Eval;
import mp.jprime.groovy.exceptions.JPGroovyRestrictiveException;
import mp.jprime.groovy.interceptors.JPGroovyRestrictiveInterceptor;
import mp.jprime.groovy.sandbox.SandboxTransformer;
import org.codehaus.groovy.control.CompilationFailedException;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import org.codehaus.groovy.control.customizers.SecureASTCustomizer;
import org.codehaus.groovy.runtime.EncodingGroovyMethods;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.io.IOException;
import java.net.URI;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Реализация JPGroovyShell с учетом запрета выполнения различных операций
 */
public final class JPGroovyCommonShell implements JPGroovyShell {
  private static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<>();

  public static final String DEFAULT_CODE_BASE = "/groovy/script";

  private final AtomicInteger counter = new AtomicInteger(0);
  private final Binding binding = new Binding();
  private final JPGroovyRestrictiveInterceptor interceptor;
  private final GroovyClassLoader loader;
  private final GroovyShell shell;

  private JPGroovyCommonShell(CompilerConfiguration conf, JPGroovyRestrictiveInterceptor interceptor) {
    ClassLoader classLoader = GroovyShell.class.getClassLoader();

    this.interceptor = interceptor;
    this.loader = new GroovyClassLoader(classLoader, conf);

    CompilerConfiguration debugConf = new CompilerConfiguration(conf);
    debugConf.setDebug(true);
    for (CompilationCustomizer customizers : conf.getCompilationCustomizers()) {
      debugConf.addCompilationCustomizers(customizers);
    }
    this.shell = new GroovyShell(classLoader, binding, debugConf);
  }

  /**
   * Возвращает значение переменной
   *
   * @param name Имя переменной
   * @return Значение переменной
   */
  @Override
  public Object getVariable(String name) {
    return binding.getVariable(name);
  }


  /**
   * Устанавливает значение переменной
   *
   * @param name  Имя переменной
   * @param value Значение переменной
   */
  @Override
  public void setVariable(String name, Object value) {
    binding.setVariable(name, value);
  }

  /**
   * Убирает значение переменной из контекста
   *
   * @param name Имя переменной
   */
  @Override
  public void removeVariable(String name) {
    binding.removeVariable(name);
  }

  /**
   * Очищает переменные из контекста
   */
  @Override
  public void clearVariables() {
    Set<String> vars = new HashSet<>(getVariables().keySet());
    vars.forEach(binding::removeVariable);
  }

  /**
   * Признак наличия переменной в контексте
   *
   * @param name Имя переменной
   * @return Да/Нет
   */
  @Override
  public boolean hasVariable(String name) {
    return binding.hasVariable(name);
  }

  /**
   * Возвращает полный список переменных из контекста
   *
   * @return Мап переменных
   */
  @Override
  public Map<String, Object> getVariables() {
    return binding.getVariables();
  }

  @Override
  public <T> T evaluate(String script) {
    return this.evaluate(script, false);
  }

  @Override
  public <T> T evaluateWithDebug(URI script) {
    return this.debug(script);
  }

  @Override
  public <T> T evaluateByCache(String script) {
    return this.evaluate(script, true);
  }


  private <T> T debug(URI script) {
    interceptor.register();
    try {
      GroovyCodeSource groovyCodeSource = new GroovyCodeSource(script);

      return (T) shell.parse(groovyCodeSource).run();
    } catch (IOException | MultipleCompilationErrorsException e) {
      throw new JPGroovyRestrictiveException(e.getMessage(), e);
    } finally {
      interceptor.unregister();
    }
  }

  private <T> T evaluate(String script, boolean cachable) {
    interceptor.register();
    try {
      GroovyCodeSource groovyCodeSource = new GroovyCodeSource(script, generateScriptName(), DEFAULT_CODE_BASE);
      groovyCodeSource.setCachable(cachable);

      Class<?> cls = null;
      if (cachable) {
        try {
          StringBuilder strToDigest = new StringBuilder((int) (script.length() * 1.2));
          strToDigest.append("scriptText:").append(script);
          String key = EncodingGroovyMethods.md5(strToDigest);

          cls = CLASS_CACHE.computeIfAbsent(key, x -> parseClass(groovyCodeSource));
        } catch (NoSuchAlgorithmException e) {
          cls = parseClass(groovyCodeSource);
        }
      }
      if (cls == null) {
        cls = parseClass(groovyCodeSource);
      }

      return (T) InvokerHelper.createScript(cls, binding).run();
    } catch (MultipleCompilationErrorsException e) {
      throw new JPGroovyRestrictiveException(e.getMessage(), e);
    } finally {
      interceptor.unregister();
    }
  }

  private Class<?> parseClass(final GroovyCodeSource codeSource) throws CompilationFailedException {
    return loader.parseClass(codeSource, codeSource.isCachable());
  }

  private String generateScriptName() {
    return "Script" + counter.incrementAndGet() + ".groovy";
  }

  public static JPGroovyCommonShell newInstance() {
    return newBuilder().build();
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public static final class Builder {
    private Collection<String> imports;
    private Collection<String> starImports;
    private Collection<String> staticStarImports;
    private Collection<Class<?>> additionalAllowedClasses;
    private Collection<Class<?>> additionalAllAllowedClasses;
    private Collection<JPGroovyRestrictiveInterceptor.AllowedPrefix> additionalAllowedPackages;

    private Builder() {
      this.imports = new HashSet<>();
      this.starImports = new HashSet<>();
      this.staticStarImports = new HashSet<>();
      this.additionalAllowedClasses = new HashSet<>();
      this.additionalAllAllowedClasses = new HashSet<>();
      this.additionalAllowedPackages = new HashSet<>();
    }

    /**
     * Добавить импорт класса
     *
     * @param classImport полное имя класса
     */
    public Builder addImport(String classImport) {
      this.imports.add(classImport);
      return this;
    }

    /**
     * Импорты классов
     *
     * @param imports полные имена классов
     */
    public Builder imports(Collection<String> imports) {
      this.imports = new HashSet<>(imports);
      return this;
    }

    /**
     * Добавить импорт пакета
     *
     * @param starImport имя пакета (без '*')
     */
    public Builder addStarImport(String starImport) {
      this.starImports.add(starImport);
      return this;
    }

    /**
     * Импорты пакетов
     *
     * @param starImports имена пакетов (без '*')
     */
    public Builder starImports(Collection<String> starImports) {
      this.starImports = new HashSet<>(starImports);
      return this;
    }

    /**
     * Добавить статический импорт всех статических членов класса
     *
     * @param staticStarImport полное имя класса (без '*')
     */
    public Builder addStaticStarImport(String staticStarImport) {
      this.staticStarImports.add(staticStarImport);
      return this;
    }

    /**
     * Статический импорт всех статических членов классов
     *
     * @param staticStarImports полные имена классов (без '*')
     */
    public Builder staticStarImports(Collection<String> staticStarImports) {
      this.staticStarImports = new HashSet<>(staticStarImports);
      return this;
    }

    /**
     * Добавить разрешенные классы
     *
     * @param allowedClass класс
     */
    public Builder addAllowedClass(Class<?> allowedClass) {
      this.additionalAllowedClasses.add(allowedClass);
      return this;
    }

    /**
     * Разрешенные классы
     *
     * @param additionalAllowedClasses классы
     */
    public Builder allowedClasses(Collection<Class<?>> additionalAllowedClasses) {
      this.additionalAllowedClasses = new HashSet<>(additionalAllowedClasses);
      return this;
    }

    public Builder addAllowedAllClass(Class<?> allowedAllClass) {
      this.additionalAllAllowedClasses.add(allowedAllClass);
      return this;
    }

    public Builder allowedAllClasses(Collection<Class<?>> additionalAllAllowedClasses) {
      this.additionalAllAllowedClasses = new HashSet<>(additionalAllAllowedClasses);
      return this;
    }

    /**
     * Добавить разрешенный пакет
     *
     * @param allowedPackage Разрешенный пакет
     */
    public Builder addAllowedPackage(JPGroovyRestrictiveInterceptor.AllowedPrefix allowedPackage) {
      this.additionalAllowedPackages.add(allowedPackage);
      return this;
    }

    /**
     * Разрешенные пакеты
     *
     * @param additionalAllowedPackages Разрешенные пакеты
     */
    public Builder allowedPackages(Collection<JPGroovyRestrictiveInterceptor.AllowedPrefix> additionalAllowedPackages) {
      this.additionalAllowedPackages = new HashSet<>(additionalAllowedPackages);
      return this;
    }

    public JPGroovyCommonShell build() {

      SecureASTCustomizer customizer = new SecureASTCustomizer();
      customizer.setDisallowedReceivers(
          Arrays.asList(
              Eval.class.getName(),
              System.class.getName()
          )
      );

      CompilerConfiguration conf = new CompilerConfiguration();
      conf.addCompilationCustomizers(customizer);
      // Disable handling Groovy Grape annotations.
      conf.setDisabledGlobalASTTransformations(new HashSet<>());
      conf.getDisabledGlobalASTTransformations().add("groovy.grape.GrabAnnotationTransformation");
      // restriction
      conf.addCompilationCustomizers(new SandboxTransformer());
      // auto import
      conf.addCompilationCustomizers(
          new ImportCustomizer()
              .addImports(
                  "groovy.json.JsonGenerator",
                  "groovy.json.JsonSlurper",
                  "java.time.LocalDate",
                  "java.time.Period",
                  "groovy.xml.slurpersupport.GPathResult",
                  "org.apache.commons.lang3.StringUtils"
              )
              .addStarImports(
                  "mp.jprime.time"
              )
              .addImports(this.imports.toArray(new String[0]))
              .addStarImports(this.starImports.toArray(new String[0]))
              .addStaticStars(this.staticStarImports.toArray(new String[0]))
      );

      JPGroovyRestrictiveInterceptor interceptor = new JPGroovyRestrictiveInterceptor(
          this.additionalAllowedClasses,
          this.additionalAllAllowedClasses,
          this.additionalAllowedPackages
      );

      return new JPGroovyCommonShell(conf, interceptor);
    }

  }

}
