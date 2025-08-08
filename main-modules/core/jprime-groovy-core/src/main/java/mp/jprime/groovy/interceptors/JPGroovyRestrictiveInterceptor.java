package mp.jprime.groovy.interceptors;

import groovy.lang.Closure;
import groovy.lang.GString;
import groovy.lang.MissingPropertyException;
import groovy.lang.Script;
import mp.jprime.groovy.exceptions.JPGroovyRestrictiveException;
import mp.jprime.groovy.sandbox.GroovyInterceptor;
import org.codehaus.groovy.runtime.InvokerHelper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Реализация GroovyInterceptor {@see GroovyInterceptor.java}
 * на основе {@see https://github.com/halestudio/hale/blob/master/util/plugins/eu.esdihumboldt.util.groovy.sandbox/src/eu/esdihumboldt/util/groovy/sandbox/internal/RestrictiveGroovyInterceptor.java}
 */
public final class JPGroovyRestrictiveInterceptor extends GroovyInterceptor {
  /**
   * Классы, которые можно использовать
   */
  private static final Collection<Class<?>> ALLOW_CLASSES = new HashSet<>();
  /**
   * Общие запрещенные методы
   */
  private static final Collection<String> DISALLOWED_METHODS = new HashSet<>();
  /**
   * Общие запрещенные свойства
   */
  private static final Collection<String> DISALLOWED_PROPERTIES = new HashSet<>();
  /**
   * Запрещенные методы groovy
   */
  private static final Collection<String> DISALLOWED_GROOVY_METHODS = new HashSet<>();
  /**
   * Запрещенные свойства groovy
   */
  private static final Collection<String> DISALLOWED_GROOVY_PROPERTIES = new HashSet<>();
  /**
   * Запрещенные Closure методы
   */
  private static final Collection<String> DISALLOWED_CLOSURE_METHODS = new HashSet<>();
  /**
   * Запрещенные Closure свойства
   */
  private static final Collection<String> DISALLOWED_CLOSURE_PROPERTIES = new HashSet<>();
  /**
   * Разрешенные пакеты
   */
  private static final Collection<AllowedPrefix> ALLOWED_PACKAGES = new ArrayList<>();

  static {
    // standard classes
    ALLOW_CLASSES.add(java.math.BigInteger.class);
    ALLOW_CLASSES.add(java.math.BigDecimal.class);
    ALLOW_CLASSES.add(java.lang.String.class);
    ALLOW_CLASSES.add(java.lang.Byte.class);
    ALLOW_CLASSES.add(java.lang.Short.class);
    ALLOW_CLASSES.add(java.lang.Integer.class);
    ALLOW_CLASSES.add(java.lang.Long.class);
    ALLOW_CLASSES.add(java.lang.Float.class);
    ALLOW_CLASSES.add(java.lang.Double.class);
    ALLOW_CLASSES.add(java.lang.Math.class);
    ALLOW_CLASSES.add(java.lang.Boolean.class);
    ALLOW_CLASSES.add(java.lang.StringBuilder.class);
    ALLOW_CLASSES.add(java.util.Date.class);
    ALLOW_CLASSES.add(java.util.Objects.class);
    ALLOW_CLASSES.add(java.util.Optional.class);
    ALLOW_CLASSES.add(org.apache.commons.lang3.StringUtils.class);
    ALLOW_CLASSES.add(org.apache.commons.lang3.tuple.Pair.class);
    ALLOW_CLASSES.add(org.apache.commons.lang3.tuple.ImmutablePair.class);
    ALLOW_CLASSES.add(java.math.RoundingMode.class);

    // Collections & Classes used by Groovy
    ALLOW_CLASSES.add(groovy.json.DefaultJsonGenerator.class);
    ALLOW_CLASSES.add(groovy.json.JsonGenerator.class);
    ALLOW_CLASSES.add(groovy.json.JsonSlurper.class);
    ALLOW_CLASSES.add(groovy.json.JsonOutput.class);
    ALLOW_CLASSES.add(groovy.lang.IntRange.class);
    ALLOW_CLASSES.add(groovy.lang.Range.class);
    ALLOW_CLASSES.add(org.apache.groovy.json.internal.LazyMap.class);
    ALLOW_CLASSES.add(org.codehaus.groovy.runtime.GStringImpl.class);

    // Java 8 date/time classes
    ALLOW_CLASSES.add(java.time.DayOfWeek.class);
    ALLOW_CLASSES.add(java.time.Month.class);
    ALLOW_CLASSES.add(java.time.Year.class);
    ALLOW_CLASSES.add(java.time.YearMonth.class);
    ALLOW_CLASSES.add(java.time.Instant.class);
    ALLOW_CLASSES.add(java.time.LocalDate.class);
    ALLOW_CLASSES.add(java.time.LocalDateTime.class);
    ALLOW_CLASSES.add(java.time.LocalTime.class);
    ALLOW_CLASSES.add(java.time.ZonedDateTime.class);
    ALLOW_CLASSES.add(java.time.ZoneId.class);
    ALLOW_CLASSES.add(java.time.Period.class);
    ALLOW_CLASSES.add(java.time.temporal.ChronoUnit.class);

    ALLOW_CLASSES.add(java.time.format.DateTimeFormatter.class);
    ALLOW_CLASSES.add(java.time.format.DateTimeFormatterBuilder.class);
    ALLOW_CLASSES.add(java.time.format.FormatStyle.class);
    ALLOW_CLASSES.add(java.time.format.TextStyle.class);
    ALLOW_CLASSES.add(java.time.format.SignStyle.class);

    // Some more collections
    ALLOW_CLASSES.add(java.util.ArrayList.class);
    ALLOW_CLASSES.add(java.util.Collections.class);
    ALLOW_CLASSES.add(java.util.HashMap.class);
    ALLOW_CLASSES.add(java.util.LinkedHashMap.class);
    ALLOW_CLASSES.add(java.util.HashSet.class);
    ALLOW_CLASSES.add(java.util.TreeMap.class);
    ALLOW_CLASSES.add(java.util.UUID.class);
    ALLOW_CLASSES.add(java.util.Arrays.class);

    // Binding classes
    ALLOW_CLASSES.add(java.sql.Timestamp.class);
    ALLOW_CLASSES.add(java.sql.Date.class);

    // Number formatting
    ALLOW_CLASSES.add(java.text.NumberFormat.class);
    ALLOW_CLASSES.add(java.text.DecimalFormat.class);
    ALLOW_CLASSES.add(java.text.DecimalFormatSymbols.class);
    ALLOW_CLASSES.add(java.text.SimpleDateFormat.class);

    // Google collect
    ALLOWED_PACKAGES.add(new AllowedPrefix("com.google.common.collect", true));
    ALLOWED_PACKAGES.add(new AllowedPrefix("groovy.xml", true));

    // Streams
    ALLOWED_PACKAGES.add(new AllowedPrefix("java.util.stream", true));

    // packages from mp.jprime
    ALLOWED_PACKAGES.add(new AllowedPrefix("mp.jprime.time", true));

    // general disallow access to specific Groovy methods
    DISALLOWED_METHODS.add("print");
    DISALLOWED_METHODS.add("println");
    DISALLOWED_METHODS.add("invokeMethod");
    DISALLOWED_METHODS.add("setMetaClass");
    DISALLOWED_METHODS.add("setProperty");
    DISALLOWED_PROPERTIES.add("metaClass");

    // forbid self-execution of script and overwriting of binding
    DISALLOWED_GROOVY_METHODS.add("run");
    DISALLOWED_GROOVY_METHODS.add("evaluate");
    DISALLOWED_GROOVY_METHODS.add("setBinding");
    DISALLOWED_GROOVY_PROPERTIES.add("binding");

    // forbid explicit setting of delegate, resolve strategy and directive
    DISALLOWED_CLOSURE_METHODS.add("setDelegate");
    DISALLOWED_CLOSURE_METHODS.add("setResolveStrategy");
    DISALLOWED_CLOSURE_METHODS.add("setDirective");
    // and rehydrate as that results basically in setting the owner,
    // delegate and thisObject
    DISALLOWED_CLOSURE_METHODS.add("rehydrate");
    DISALLOWED_CLOSURE_PROPERTIES.add("delegate");
    DISALLOWED_CLOSURE_PROPERTIES.add("resolveStrategy");
    DISALLOWED_CLOSURE_PROPERTIES.add("directive");
  }

  /**
   * AllowedPackage saves information to allow the use of all classes with a
   * given prefix.
   */
  public static class AllowedPrefix {
    private final String prefix;
    private final boolean allowChildren;

    /**
     * Default constructor which accepts a package name.
     *
     * @param prefix        the package prefix (final '.' is added if not present)
     * @param allowChildren whether child-packages are allowed, too
     */
    public AllowedPrefix(String prefix, boolean allowChildren) {
      if (!prefix.endsWith(".")) {
        prefix += '.';
      }
      this.prefix = prefix;
      this.allowChildren = allowChildren;
    }

    /**
     * Checks whether the given class is allowed to be used because of this
     * allowed package.
     *
     * @param cls the class to test
     * @return true, if the class may be used, false otherwise
     */
    public boolean checkAllowed(Class<?> cls) {
      String className = cls.getName();

      if (className.startsWith(prefix)) {
        return allowChildren || !className.substring(prefix.length()).contains(".");
      } else {
        return false;
      }
    }
  }

  private final Collection<Class<?>> instanceAllowedClasses = new HashSet<>(ALLOW_CLASSES);
  private final Collection<AllowedPrefix> instanceAllowedPackages = new ArrayList<>(ALLOWED_PACKAGES);

  public JPGroovyRestrictiveInterceptor() {
  }

  /**
   * Constructor using additional allowed classes.
   *
   * @param additionalAllowedClasses    classes, which may be initialized, and
   *                                    all their declared methods may be used
   * @param additionalAllAllowedClasses classes, which may be initialized, and
   *                                    any call on them is allowed (has to implement methodMissing or
   *                                    equal)
   * @param additionalAllowedPackages   packages whose classes and their
   *                                    declared methods may be used
   */
  public JPGroovyRestrictiveInterceptor(Collection<Class<?>> additionalAllowedClasses,
                                        Collection<Class<?>> additionalAllAllowedClasses,
                                        Collection<AllowedPrefix> additionalAllowedPackages) {
    instanceAllowedClasses.addAll(additionalAllowedClasses);
    instanceAllowedClasses.addAll(additionalAllAllowedClasses);
    instanceAllowedPackages.addAll(additionalAllowedPackages);
  }

  @Override
  public Object onStaticCall(Invoker invoker, @SuppressWarnings("rawtypes") Class receiver,
                             String method, Object... args) throws Throwable {
    if (isAllowedClass(receiver) || isScriptClass(receiver)) {
      return super.onStaticCall(invoker, receiver, method, args);
    } else {
      throw new JPGroovyRestrictiveException("using class " + receiver.getName() + " is not allowed!");
    }
  }

  @Override
  public Object onNewInstance(Invoker invoker, @SuppressWarnings("rawtypes") Class receiver,
                              Object... args) throws Throwable {
    // classes defined in the script would be okay, sadly it is not possible
    // to identify those?
    if (isAllowedClass(receiver) || isScriptClass(receiver)) {
      return super.onNewInstance(invoker, receiver, args);
    } else {
      throw new JPGroovyRestrictiveException("using class " + receiver.getName() + " is not allowed!");
    }
  }

  @Override
  public Object onMethodCall(Invoker invoker, Object receiver, String method, Object... args) throws Throwable {
    if (DISALLOWED_METHODS.contains(method)) {
      throw new JPGroovyRestrictiveException("using methods named " + method + " is not allowed in Groovy transformations!");
    } else if (receiver instanceof Closure && DISALLOWED_CLOSURE_METHODS.contains(method)) {
      throw new JPGroovyRestrictiveException("using the closure method " + method + " is not allowed in Groovy transformations!");
    }
    // Return value doesn't matter!
    // true -> allowed delegation found
    // false -> no disallowed delegation found
    checkMethodCall(receiver, method);
    return super.onMethodCall(invoker, receiver, method, args);
  }

  private boolean checkMethodCall(Object receiver, String method) throws JPGroovyRestrictiveException {
    if (receiver instanceof Closure) {
      // Closure method names were tested before.
      Closure<?> closure = (Closure<?>) receiver;
      Object owner = closure.getOwner();
      Object delegate = closure.getDelegate();
      int rs = closure.getResolveStrategy();
      // Check owner first.
      if (rs == Closure.OWNER_FIRST || rs == Closure.OWNER_ONLY) {
        if (checkMethodCall(owner, method)) {
          return true;
        }
      }
      // Check delegate first/second.
      if (rs == Closure.OWNER_FIRST || rs == Closure.DELEGATE_FIRST || rs == Closure.DELEGATE_ONLY) {
        if (delegate != null && delegate != closure) {
          if (checkMethodCall(delegate, method)) {
            return true;
          }
        }
      }
      // Check owner second.
      if (rs == Closure.DELEGATE_FIRST) {
        if (checkMethodCall(owner, method)) {
          return true;
        }
      }
      // Cannot be 100% sure whether the call will be handled by
      // delegation to this closure.
      return false;
    } else if (isAllowedClass(receiver.getClass())) {
      checkExecute(receiver, method);
      return !InvokerHelper.getMetaClass(receiver).respondsTo(receiver, method).isEmpty();
    } else if (isScriptClass(receiver.getClass()) && !DISALLOWED_GROOVY_METHODS.contains(method)) {
      return !InvokerHelper.getMetaClass(receiver).respondsTo(receiver, method).isEmpty();
    }
    throw new JPGroovyRestrictiveException("Possible access of method " + method + " on class "
        + receiver.getClass().getName() + " is not allowed in Groovy transformations!");
  }

  /**
   * Checks for an execute call on List, String, String[] and GString.
   *
   * @param receiver the receiver object
   * @param method   the method name
   */
  private void checkExecute(Object receiver, String method) {
    if ("execute".equals(method)) {
      if (receiver instanceof List || receiver instanceof String ||
          receiver.getClass().isArray() || receiver instanceof String[] ||
          receiver instanceof GString) {
        throw new JPGroovyRestrictiveException(
            "Possible access of method execute on List, String, String[] and GString is not allowed in Groovy transformations!");
      }
    }
  }

  private boolean isScriptClass(Class<?> receiver) {
    return Script.class.isAssignableFrom(receiver);
  }

  @Override
  public Object onGetProperty(Invoker invoker, Object receiver, String property) throws Throwable {
    if (receiver instanceof Class<?> && isAllowedClass((Class<?>) receiver) && !"class".equals(property)) {
      return super.onGetProperty(invoker, receiver, property);
    }
    checkPropertyAccess(receiver, property, false);
    return super.onGetProperty(invoker, receiver, property);
  }

  @Override
  public Object onSetProperty(Invoker invoker, Object receiver, String property, Object value) throws Throwable {
    if (DISALLOWED_PROPERTIES.contains(property)) {
      throw new JPGroovyRestrictiveException("setting the property " + property + " is not allowed in Groovy transformations!");
    }
    if (receiver instanceof Closure && DISALLOWED_CLOSURE_PROPERTIES.contains(property)) {
      throw new JPGroovyRestrictiveException("setting the closure property " + property
          + " is not allowed in Groovy transformations!");
    }
    checkPropertyAccess(receiver, property, true);
    return super.onSetProperty(invoker, receiver, property, value);
  }

  private boolean checkPropertyAccess(Object receiver, String property, boolean set) throws JPGroovyRestrictiveException {
    if (receiver instanceof Closure) {
      // Closure properties were tested before.
      Closure<?> closure = (Closure<?>) receiver;
      Object owner = closure.getOwner();
      Object delegate = closure.getDelegate();
      int rs = closure.getResolveStrategy();
      // Check owner first.
      if (rs == Closure.OWNER_FIRST || rs == Closure.OWNER_ONLY) {
        if (checkPropertyAccess(owner, property, set)) {
          return true;
        }
      }
      // Check delegate first/second.
      if (rs == Closure.OWNER_FIRST || rs == Closure.DELEGATE_FIRST || rs == Closure.DELEGATE_ONLY) {
        if (delegate != null && delegate != closure) {
          if (checkPropertyAccess(delegate, property, set)) {
            return true;
          }
        }
      }
      // Check owner second.
      if (rs == Closure.DELEGATE_FIRST) {
        if (checkPropertyAccess(owner, property, set)) {
          return true;
        }
      }
      // Cannot be 100% sure whether the property will be handled by
      // delegation to this closure.
      return false;
    } else if (isAllowedClass(receiver.getClass())) {
      return hasProperty(receiver, property);
    } else if (isScriptClass(receiver.getClass()) && (!set || !DISALLOWED_GROOVY_PROPERTIES.contains(property))) {
      return hasProperty(receiver, property);
    }
    throw new JPGroovyRestrictiveException("Possible " + (set ? "write " : "")
        + "access of property " + property + " on class " + receiver.getClass().getName()
        + " is not allowed in Groovy transformations!");
  }

  @Override
  public Object onGetAttribute(Invoker invoker, Object receiver, String attribute) throws Throwable {
    checkPropertyAccess(receiver, attribute, false);
    return super.onGetAttribute(invoker, receiver, attribute);
  }

  @Override
  public Object onSetAttribute(Invoker invoker, Object receiver, String attribute, Object value) throws Throwable {
    if (DISALLOWED_PROPERTIES.contains(attribute)) {
      throw new JPGroovyRestrictiveException("setting the property " + attribute + " is not allowed in Groovy transformations!");
    }
    if (receiver instanceof Closure && DISALLOWED_CLOSURE_PROPERTIES.contains(attribute)) {
      throw new JPGroovyRestrictiveException("setting the closure property " + attribute + " is not allowed in Groovy transformations!");
    }
    checkPropertyAccess(receiver, attribute, true);
    return super.onSetAttribute(invoker, receiver, attribute, value);
  }

  @Override
  public Object onGetArray(Invoker invoker, Object receiver, Object index) throws Throwable {
    return super.onGetArray(invoker, receiver, index);
  }

  @Override
  public Object onSetArray(Invoker invoker, Object receiver, Object index, Object value) throws Throwable {
    return super.onSetArray(invoker, receiver, index, value);
  }

  private static boolean hasProperty(Object object, String property) {
    if (InvokerHelper.getMetaClass(object).hasProperty(object, property) != null) {
      return true;
    }
    try {
      InvokerHelper.getProperty(object, property);
      return true;
    } catch (MissingPropertyException e) {
      return false;
    }
  }

  private boolean isAllowedClass(Class<?> cls) {
    // instanceAllowedClasses.add needs to be synchronized, as internal
    // state changes.
    // .contains does not need to be synchronized, worst case would be that
    // an element is added several times then, which doesn't matter.

    if (instanceAllowedClasses.contains(cls)) {
      return true;
    }

    // allow accessing arrays in general
    // (calls like execute are disallowed by another mechanism)
    if (cls.isArray()) {
      return true;
    }

    // allow nested classes of allowed classes
    Class<?> topLevelClass = cls;
    while (topLevelClass.getEnclosingClass() != null) {
      topLevelClass = topLevelClass.getEnclosingClass();
    }
    if (topLevelClass != cls) {
      if (instanceAllowedClasses.contains(topLevelClass)) {
        // cache result for nested class
        synchronized (instanceAllowedClasses) {
          instanceAllowedClasses.add(cls);
        }
        return true;
      }
    }

    // walk through prefixes
    for (AllowedPrefix allowedPackage : instanceAllowedPackages) {
      if (allowedPackage.checkAllowed(cls)) {
        // cache result for class within a allowed package
        synchronized (instanceAllowedClasses) {
          instanceAllowedClasses.add(cls);
        }
        return true;
      }
    }
    return false;
  }
}
