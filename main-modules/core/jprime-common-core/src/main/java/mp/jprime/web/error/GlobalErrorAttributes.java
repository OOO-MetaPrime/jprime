package mp.jprime.web.error;

import mp.jprime.exceptions.CompositeException;
import mp.jprime.exceptions.JPAppRuntimeException;
import mp.jprime.exceptions.JPObjectNotFoundException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.reactive.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class GlobalErrorAttributes extends DefaultErrorAttributes {
  @Override
  public Map<String, Object> getErrorAttributes(ServerRequest request, ErrorAttributeOptions options) {
    Map<String, Object> map = super.getErrorAttributes(request, options);
    Throwable error = getError(request);
    if (error instanceof CompositeException) {
      map.put("details", ((CompositeException) error).getExceptions().stream().map(x -> fill((Exception) x)).collect(Collectors.toList()));
    } else if (error instanceof Exception) {
      map.put("details", Collections.singleton(fill((Exception) error)));
    }
    return map;
  }

  private Map<String, Object> fill(Exception error) {
    String code = null;
    String message = null;
    if (error instanceof JPAppRuntimeException) {
      code = ((JPAppRuntimeException) error).getMessageCode();
      message = error.getMessage();
    } else if (error instanceof JPObjectNotFoundException) {
      JPObjectNotFoundException e = (JPObjectNotFoundException) error;
      code = e.getMessageCode();
      message = error.getMessage();
    }
    Map<String, Object> result = new HashMap<>(2);
    result.put("code", code != null ? code : "server.error");
    result.put("message", message);
    return result;
  }
}
