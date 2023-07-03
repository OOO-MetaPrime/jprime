package mp.jprime.json.versioning.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mp.jprime.json.versioning.beans.JsonV3Bean;
import mp.jprime.json.versioning.services.JPJsonVersionBaseConverter;
import mp.jprime.lang.JPJsonNode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class JsonV3Converter extends JPJsonVersionBaseConverter<JsonV3Bean> {
  private final static Collection<Integer> FROM_VERSIONS = Collections.singleton(2);

  @Override
  public String getGroupCode() {
    return "test";
  }

  @Override
  public Integer getVersion() {
    return 3;
  }

  @Override
  public Class<JsonV3Bean> getBeanClass() {
    return JsonV3Bean.class;
  }

  @Override
  public Collection<Integer> fromVersions() {
    return FROM_VERSIONS;
  }

  @Override
  public JPJsonNode convertToNode(Integer version, JPJsonNode value) {
    if (version == null || version != 2) {
      return null;
    }
    JsonNode node = value.toJsonNode();
    ObjectNode object = (ObjectNode) node;
    JsonNode field2 = object.get("field2");

    object.set("field3", field2);
    object.remove("field2");
    return value;
  }
}
