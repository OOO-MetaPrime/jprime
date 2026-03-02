package mp.jprime.xml.validation;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Node;

import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import jakarta.xml.bind.ValidationEventLocator;

import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Обработчик событий валидации
 * <p>
 * Собирает все события в список
 */
public sealed class ValidationEventCollector implements ValidationEventHandler permits MaskedValidationEventCollector {
  private final Collection<ValidationEvent> events = new ArrayList<>();

  @Override
  public boolean handleEvent(ValidationEvent event) {
    events.add(event);
    return true;
  }

  /**
   * Проверяет что есть ошибки проверки ERROR или FATAL_ERROR
   *
   * @return есть ошибки проверки
   */
  public boolean hasError() {
    return events.stream().anyMatch(e -> e.getSeverity() > ValidationEvent.WARNING);
  }

  /**
   * Возвращает описание всех ошибок
   *
   * @return описание всех ошибок
   */
  public String getDescription() {
    if (events.isEmpty()) {
      return "";
    }
    return events.stream().map(this::getEventDescription).collect(Collectors.joining("\n"));
  }

  /**
   * Получить ошибки
   *
   * @return ошибки
   */
  public Map<Integer, Collection<String>> getErrors() {
    Map<Integer, Collection<String>> errors = new HashMap<>();

    for (ValidationEvent event : events) {
      errors.computeIfAbsent(event.getSeverity(), k -> new ArrayList<>()).add(event.getMessage());
    }
    return errors;
  }

  private String getEventDescription(ValidationEvent e) {
    return "[" + getSeverityName(e.getSeverity()) + "] " + getEventMessage(e) + " (" + getLocation(e) + ")";
  }

  protected String getEventMessage(ValidationEvent event) {
    return event.getMessage();
  }

  private String getSeverityName(int i) {
    return switch (i) {
      case ValidationEvent.WARNING -> "WARNING";
      case ValidationEvent.ERROR -> "ERROR";
      case ValidationEvent.FATAL_ERROR -> "FATAL_ERROR";
      default -> "UNKNOWN";
    };
  }

  private String getLocation(ValidationEvent event) {
    StringBuilder msg = new StringBuilder();

    ValidationEventLocator locator = event.getLocator();

    if (locator != null) {

      URL url = locator.getURL();
      Object obj = locator.getObject();
      Node node = locator.getNode();
      int line = locator.getLineNumber();

      if (url != null || line != -1) {
        msg.append("line ").append(line);
        if (url != null)
          msg.append(" of ").append(url);
      } else if (obj != null) {
        msg.append(" obj: ").append(abbreviatePackageName(obj.getClass().getName()));
      } else if (node != null) {
        msg.append(" node: ").append(node);
      }
    } else {
      msg.append("Location unavailable");
    }

    return msg.toString();
  }

  private String abbreviatePackageName(String fullName) {
    String[] parts = StringUtils.split(fullName, '.');
    if (parts.length > 1) {
      StringBuilder sb = new StringBuilder();
      for (int i = 0; i < parts.length - 1; i++) {
        sb.append(parts[i].charAt(0)).append(".");
      }
      sb.append(parts[parts.length - 1]);
      return sb.toString();
    }
    return fullName;
  }
}
