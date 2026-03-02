package mp.jprime.xml;

/**
 * Настройки формирования XML
 */
public final class XMLOutputOptions {
  public static final String DEFAULT_ENCODING = "UTF-8";
  /**
   * Формировать узел инструкций по обработке
   */
  private boolean noProcessingInstruction;
  /**
   * Формировать форматированный XML
   */
  private boolean indent;
  /**
   * Кодировка
   */
  private String encoding;

  private XMLOutputOptions(Builder builder) {
    noProcessingInstruction = builder.noProcessingInstruction;
    indent = builder.indent;
    encoding = builder.encoding != null ? builder.encoding : DEFAULT_ENCODING;
  }

  public static Builder newBuilder() {
    return new Builder();
  }

  public boolean isNoProcessingInstruction() {
    return noProcessingInstruction;
  }

  public boolean isIndent() {
    return indent;
  }

  public String getEncoding() {
    return encoding;
  }

  public static final class Builder {
    private boolean noProcessingInstruction;
    private boolean indent;
    private String encoding;

    public Builder() {
    }

    public Builder(XMLOutputOptions copy) {
      this.noProcessingInstruction = copy.noProcessingInstruction;
      this.indent = copy.indent;
      this.encoding = copy.encoding;
    }

    public Builder noProcessingInstruction(boolean processingInstruction) {
      this.noProcessingInstruction = processingInstruction;
      return this;
    }

    public Builder indent(boolean indent) {
      this.indent = indent;
      return this;
    }

    public Builder encoding(String encoding) {
      this.encoding = encoding;
      return this;
    }

    public XMLOutputOptions build() {
      return new XMLOutputOptions(this);
    }
  }
}