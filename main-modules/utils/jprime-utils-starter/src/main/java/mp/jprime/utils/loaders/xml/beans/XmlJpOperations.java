package mp.jprime.utils.loaders.xml.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.Arrays;

@JacksonXmlRootElement(localName = "operations")
@JsonIgnoreProperties(ignoreUnknown = true)
public class XmlJpOperations {
  private boolean useTransaction;
  private String storages;
  @JacksonXmlElementWrapper(useWrapping = false)
  @JacksonXmlProperty(localName = "operation")
  private XmlJpOperation[] operation;

  public boolean isUseTransaction() {
    return useTransaction;
  }

  public void setUseTransaction(boolean useTransaction) {
    this.useTransaction = useTransaction;
  }

  public String getStorages() {
    return storages;
  }

  public void setStorages(String storages) {
    this.storages = storages;
  }

  public XmlJpOperation[] getOperation() {
    return operation;
  }

  public void setOperation(XmlJpOperation[] operation) {
    this.operation = operation;
  }

  @Override
  public String toString() {
    return "XmlJpModeAction{" +
        "useTransaction=" + useTransaction +
        ",storages=" + storages +
        ",operation=" + Arrays.toString(operation) +
        '}';
  }
}
