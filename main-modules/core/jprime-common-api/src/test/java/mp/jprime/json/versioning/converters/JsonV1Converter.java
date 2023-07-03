package mp.jprime.json.versioning.converters;

import mp.jprime.json.versioning.beans.JsonV1Bean;
import mp.jprime.json.versioning.services.JPJsonVersionBaseConverter;
import mp.jprime.lang.JPJsonNode;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class JsonV1Converter extends JPJsonVersionBaseConverter<JsonV1Bean> {
  @Override
  public String getGroupCode() {
    return "test";
  }

  @Override
  public Integer getVersion() {
    return 1;
  }

  @Override
  public Class<JsonV1Bean> getBeanClass() {
    return JsonV1Bean.class;
  }

  @Override
  public Collection<Integer> fromVersions() {
    return null;
  }

  @Override
  public JPJsonNode convertToNode(Integer version, JPJsonNode value) {
    return null;
  }
}
