package mp.jprime.json.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Список объектов атрибута
 */
@JsonPropertyOrder({
    "attr",
    "objects"
})
public class JsonLinkedList {
  @JsonProperty("attr")
  private String attr;
  @JsonProperty("objects")
  private Collection<JsonJPObject> objects = new ArrayList<>();
}
