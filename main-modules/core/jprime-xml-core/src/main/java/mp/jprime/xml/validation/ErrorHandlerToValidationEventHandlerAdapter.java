package mp.jprime.xml.validation;

import org.w3c.dom.Node;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;
import jakarta.xml.bind.ValidationEvent;
import jakarta.xml.bind.ValidationEventHandler;
import jakarta.xml.bind.ValidationEventLocator;

import java.net.URL;

/**
 * Адаптер org.xml.sax.ErrorHandler в jakarta.xml.bind.ValidationEventHandler
 */
public final class ErrorHandlerToValidationEventHandlerAdapter implements ErrorHandler {

  private final ValidationEventHandler validationEventHandler;

  private ErrorHandlerToValidationEventHandlerAdapter(ValidationEventHandler validationEventHandler) {
    this.validationEventHandler = validationEventHandler;
  }

  public static ErrorHandlerToValidationEventHandlerAdapter of(ValidationEventHandler validationEventHandler) {
    return new ErrorHandlerToValidationEventHandlerAdapter(validationEventHandler);
  }

  @Override
  public void warning(SAXParseException exception) {
    handleEvent(ValidationEvent.WARNING, exception);
  }

  @Override
  public void error(SAXParseException exception) {
    handleEvent(ValidationEvent.ERROR, exception);
  }

  @Override
  public void fatalError(SAXParseException exception) {
    handleEvent(ValidationEvent.FATAL_ERROR, exception);
  }

  private boolean handleEvent(int severity, SAXParseException exception) {
    ValidationEventLocator locator = new ValidationEventLocator() {
      @Override
      public Object getObject() { return null; }
      @Override
      public int getLineNumber() { return exception.getLineNumber(); }
      @Override
      public int getColumnNumber() { return exception.getColumnNumber(); }
      @Override
      public int getOffset() { return -1; }
      @Override
      public Node getNode() { return null; }
      @Override
      public URL getURL() {
        return null;
      }
    };
    ValidationEvent event = new ValidationEvent() {
      @Override
      public int getSeverity() { return severity; }
      @Override
      public String getMessage() { return exception.getMessage(); }
      @Override
      public Throwable getLinkedException() { return exception; }
      @Override
      public ValidationEventLocator getLocator() { return locator; }
    };
    try {
      // Return value indicates whether to continue processing.
      return validationEventHandler.handleEvent(event);
    } catch (Exception e) {
      // In case of exception from user handler, stop processing.
      return false;
    }
  }
}

