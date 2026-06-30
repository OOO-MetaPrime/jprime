package mp.jprime.json.view;

/**
 * Объект трансформируется в формат универсального представления
 */
public interface JsonViewAware {
  void toJson(JsonViewBuilder jvb);
}
