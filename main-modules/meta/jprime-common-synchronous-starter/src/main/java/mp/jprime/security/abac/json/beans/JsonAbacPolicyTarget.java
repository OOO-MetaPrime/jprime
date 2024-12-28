package mp.jprime.security.abac.json.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Collection;
import java.util.Collections;

/**
 * Условие - метаописание класса
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JsonAbacPolicyTarget {
  private Collection<String> jpClasses;

  public JsonAbacPolicyTarget() {

  }

  private JsonAbacPolicyTarget(Collection<String> jpClasses) {
    this.jpClasses = jpClasses != null ? Collections.unmodifiableCollection(jpClasses) : Collections.emptyList();
  }

  /**
   * Значение
   *
   * @return Значение
   */
  public Collection<String> getJpClasses() {
    return jpClasses;
  }

  public void setJpClasses(Collection<String> jpClasses) {
    this.jpClasses = jpClasses;
  }

  public static JsonAbacPolicyTarget from(Collection<String> jpClasses) {
    return new JsonAbacPolicyTarget(jpClasses);
  }
}

