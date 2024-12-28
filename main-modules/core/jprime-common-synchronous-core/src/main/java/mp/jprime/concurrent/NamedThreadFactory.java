package mp.jprime.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Фабрика именованных потоков
 */
public final class NamedThreadFactory implements ThreadFactory {
  private final AtomicInteger sequence = new AtomicInteger(1);
  private final String prefix;
  private final boolean daemon;

  private NamedThreadFactory(String prefix, boolean daemon) {
    this.prefix = prefix;
    this.daemon = daemon;
  }

  private NamedThreadFactory(String prefix) {
    this(prefix, true);
  }

  /**
   * Фабричный метод
   * @param prefix Префикс имени потока
   * @param daemon признак daemon-потока
   * @return фабрика потоков
   */
  public static NamedThreadFactory of(String prefix, boolean daemon) {
    return new NamedThreadFactory(prefix, daemon);
  }

  /**
   * Фабричный метод (создает daemon потоки)
   * @param prefix Префикс имени потока
   * @return фабрика потоков
   */
  public static NamedThreadFactory of(String prefix) {
    return NamedThreadFactory.of(prefix, true);
  }

  @Override
  public Thread newThread(Runnable r) {
    Thread thread = new Thread(r);
    int seq = sequence.getAndIncrement();
    thread.setName(prefix + (seq > 1 ? "-" + seq : ""));
    thread.setDaemon(daemon);
    return thread;
  }
}
