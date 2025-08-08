package mp.jprime.mail;

/**
 * Логика работы с электронной почтой
 */
public interface JPMailService {
  /**
   * Отправка сообщения
   *
   * @param subject Заголовок сообщения
   * @param text    Текст сообщения
   * @param  emailList Список адресов
   */
  void sendMessage(String subject, String text, String... emailList);

  /**
   * Отправка сообщения или ошибка отправки.
   *
   * @param subject Заголовок сообщения
   * @param text    Текст сообщения
   * @param  emailList Список адресов
   */
  void sendMessageOrThrow(String subject, String text, String... emailList);
}
