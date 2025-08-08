package mp.jprime.dataaccess.transaction.events;

/**
 * Событие транзакции - Runnable
 */
public final class JPRunnableTransactionEvent implements JPTransactionEvent {
  public static final String CODE = "jpRunnableEvent";

  private final Runnable runnable;

  private JPRunnableTransactionEvent(Runnable runnable) {
    this.runnable = runnable;
  }

  public static JPRunnableTransactionEvent of(Runnable runnable) {
    return new JPRunnableTransactionEvent(runnable);
  }

  @Override
  public String getCode() {
    return CODE;
  }

  public Runnable getRunnable() {
    return runnable;
  }
}
