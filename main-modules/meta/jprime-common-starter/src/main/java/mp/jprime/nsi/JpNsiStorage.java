package mp.jprime.nsi;

import mp.jprime.security.AuthInfo;

import java.util.Collection;

/**
 * Хранилище НСИ
 */
public interface JpNsiStorage {
  int CROSS_SEARCH_LIMIT = 300;

  /**
   * Возвращает каталоги НСИ
   *
   * @return Список каталогов НСИ
   */
  Collection<JpNsiCatalog> getCatalogs();

  /**
   * Возвращает каталог по коду
   *
   * @param catalog Код каталога
   * @return Каталог по коду
   */
  JpNsiCatalog getCatalog(String catalog);

  /**
   * Возвращает каталог ближайший к НСИ коду
   *
   * @param nsiCode Код НСИ
   * @return Каталог
   */
  JpNsiCatalog getCatalogByNsi(String nsiCode);

  /**
   * Возвращает справочники НСИ
   *
   * @return Список справочники НСИ
   */
  Collection<JpNsi<?>> getNsi();

  /**
   * Возвращает справочник по коду
   *
   * @param nsiCode Код НСИ
   * @return НСИ по коду
   */
  JpNsi<?> getNsi(String nsiCode);

  /**
   * Возвращает справочники НСИ, обертка над обычной метой
   *
   * @return Список справочники НСИ, обертка над обычной метой
   */
  Collection<JpMetaNsi<?>> getMetaNsi();

  /**
   * Возвращает справочник НСИ,, обертку над обычной метой, по коду
   *
   * @param nsiCode Код НСИ
   * @return НСИ по код
   */
  JpMetaNsi<?> getMetaNsi(String nsiCode);

  /**
   * Возвращает значение справочника по id
   *
   * @param nsiCode Код НСИ
   * @param id      Код справочника
   * @return Значение
   */
  JpNsiValue<Integer> getValueById(String nsiCode, Integer id);

  /**
   * Возвращает значение справочника по id
   *
   * @param nsiCode Код НСИ
   * @param id      Код справочника
   * @return Значение
   */
  JpNsiValue<String> getValueById(String nsiCode, String id);

  /**
   * Возвращает значения справочника
   *
   * @param nsiCode          Код НСИ
   * @param search           Поисковые строки
   * @param nameSearch       Признак поиска по имени
   * @param propertiesSearch Признак поиска по доп.полям
   * @param auth             AuthInfo
   * @return Значение
   */
  Collection<JpNsiValue<?>> getValuesByAuth(String nsiCode, SearchQuery search,
                                            boolean nameSearch, boolean propertiesSearch, AuthInfo auth);

  record SearchQuery(String searchQuery, String[] tokens) {
    private static final String RUS_REGEXP = "\\B[АаЯяУуЮюОоЕеЁёЭэИиЫы]\\b";


    public static SearchQuery of(String searchQuery) {
      return new SearchQuery(searchQuery, normalize(searchQuery));
    }

    /**
     * Нормализует строку для поиска убирая гласные в конце
     *
     * @param query Строка запроса
     * @return Строка, с убранными последними гласными
     */
    private static String[] normalize(String query) {
      if (query == null || query.trim().isEmpty()) {
        return null;
      }
      int queryLength = query.length();
      if (queryLength > 3) {
        query = query.replaceAll(RUS_REGEXP, "");
        while (queryLength != query.length()) {
          query = query.replaceAll(RUS_REGEXP, "");
          queryLength = query.length();
        }
      }
      return query.split(" ");
    }
  }
}
