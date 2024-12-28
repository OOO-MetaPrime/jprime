package mp.jprime.monitoring.jvm;

/**
 * Типы служебных событий
 */
public enum Event implements mp.jprime.log.Event {
  /**
   * Предупреждение о приближении к выделенному объекму памяти
   */
  JVM_HEAP_MEMORY_LIMIT_WARNINIG("jvm.heap.memory.limit.warning", true),
  /**
   * Предупреждение о приближении к выделенному объекму памяти
   */
  JVM_NONHEAP_MEMORY_LIMIT_WARNINIG("jvm.nonheap.memory.limit.warning", true);

  private final String code;
  private final boolean success;

  Event(String code, boolean success) {
    this.code = code;
    this.success = success;
  }

  @Override
  public String getCode() {
    return code;
  }

  @Override
  public boolean isSuccess() {
    return success;
  }
}
