package mp.jprime.concurrent;

import java.util.concurrent.ThreadFactory;

/**
 * Декоратор переопределяющий ContextClassLoader
 */
public final class ContextClassLoaderThreadFactoryWrapper implements ThreadFactory {
  private final ThreadFactory delegate;
  private final ClassLoader classLoader;

  public ContextClassLoaderThreadFactoryWrapper(ThreadFactory delegate, ClassLoader classLoader) {
    this.delegate = delegate;
    this.classLoader = classLoader == null ? this.getClass().getClassLoader() : classLoader;
  }

  public static ThreadFactory of(ThreadFactory delegate) {
    return new ContextClassLoaderThreadFactoryWrapper(delegate, null);
  }

  public static ThreadFactory of(ThreadFactory delegate, ClassLoader classLoader) {
    return new ContextClassLoaderThreadFactoryWrapper(delegate, classLoader);
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread t = delegate.newThread(r);
    t.setContextClassLoader(this.classLoader);
    return t;
  }
}