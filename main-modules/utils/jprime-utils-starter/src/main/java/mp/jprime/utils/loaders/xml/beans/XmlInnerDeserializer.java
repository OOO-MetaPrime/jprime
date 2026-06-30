package mp.jprime.utils.loaders.xml.beans;

import tools.jackson.core.JsonParser;
import tools.jackson.databind.deser.std.StdDeserializer;

public class XmlInnerDeserializer extends StdDeserializer<String> {
  public XmlInnerDeserializer() {
    super(JsonParser.class);
  }

  @Override
  public String deserialize(JsonParser p, tools.jackson.databind.DeserializationContext ctxt) {
    return p.toString();
  }
}
