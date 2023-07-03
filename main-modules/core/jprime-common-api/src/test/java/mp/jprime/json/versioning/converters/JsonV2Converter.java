package mp.jprime.json.versioning.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import mp.jprime.json.versioning.beans.JsonV2Bean;
import mp.jprime.json.versioning.services.JPJsonVersionBaseConverter;
import mp.jprime.lang.JPJsonNode;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class JsonV2Converter extends JPJsonVersionBaseConverter<JsonV2Bean> {
  private final static Collection<Integer> FROM_VERSIONS = Collections.singleton(1);

  @Override
  public String getGroupCode() {
    return "test";
  }

  @Override
  public Integer getVersion() {
    return 2;
  }

  @Override
  public Class<JsonV2Bean> getBeanClass() {
    return JsonV2Bean.class;
  }

  @Override
  public Collection<Integer> fromVersions() {
    return FROM_VERSIONS;
  }

  @Override
  public JPJsonNode convertToNode(Integer version, JPJsonNode value) {
    if (version == null || version != 1) {
      return null;
    }
    JsonNode node = value.toJsonNode();
    ObjectNode object = (ObjectNode) node;
    JsonNode field1 = object.get("field1");

    object.set("field2", field1);
    object.remove("field1");
    return value;
  }
}
