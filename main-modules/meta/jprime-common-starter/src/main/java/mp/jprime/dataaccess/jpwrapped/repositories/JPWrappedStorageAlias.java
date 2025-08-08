package mp.jprime.dataaccess.jpwrapped.repositories;

public final class JPWrappedStorageAlias extends JPWrappedStorage {
  /**
   * WrappedStorage
   */
  private final JPWrappedStorage wrappedStorage;

  public JPWrappedStorageAlias(String code, String title, JPWrappedStorage wrappedStorage) {
    super(code, title);
    this.wrappedStorage = wrappedStorage;
  }
}
