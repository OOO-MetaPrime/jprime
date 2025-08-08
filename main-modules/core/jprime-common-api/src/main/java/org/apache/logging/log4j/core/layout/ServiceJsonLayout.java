package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.databind.ObjectWriter;
import mp.jprime.log.AppLogger;
import mp.jprime.system.AppProperty;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static mp.jprime.formats.DateFormat.ISO8601;

@Plugin(name = "ServiceJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class ServiceJsonLayout extends AbstractStringLayout {
  private static final String DEFAULT_FOOTER = "]";
  private static final String DEFAULT_HEADER = "[";
  private static final String DEFAULT_EOL = "\r\n";
  private static final String COMPACT_EOL = Strings.EMPTY;

  @PluginBuilderFactory
  public static <B extends Builder<B>> B newBuilder() {
    return new Builder<B>().asBuilder();
  }

  public static class Builder<B extends Builder<B>> extends AbstractStringLayout.Builder<B> implements org.apache.logging.log4j.core.util.Builder<ServiceJsonLayout> {
    @PluginBuilderAttribute
    private boolean eventEol;
    @PluginBuilderAttribute
    private boolean compact;
    @PluginBuilderAttribute
    private boolean stacktraceAsString = false;

    public Builder() {
      super();
      setCharset(StandardCharsets.UTF_8);
    }

    public boolean isStacktraceAsString() {
      return stacktraceAsString;
    }

    public boolean getEventEol() {
      return eventEol;
    }

    public boolean isCompact() {
      return compact;
    }


    public B setEventEol(final boolean eventEol) {
      this.eventEol = eventEol;
      return asBuilder();
    }

    public B setCompact(final boolean compact) {
      this.compact = compact;
      return asBuilder();
    }

    public B setStacktraceAsString(final boolean stacktraceAsString) {
      this.stacktraceAsString = stacktraceAsString;
      return asBuilder();
    }


    protected String toStringOrNull(final byte[] header) {
      return header == null ? null : new String(header, Charset.defaultCharset());
    }

    @Override
    public ServiceJsonLayout build() {
      final String headerPattern = toStringOrNull(getHeader());
      final String footerPattern = toStringOrNull(getFooter());
      return new ServiceJsonLayout(getConfiguration(), isCompact(), getEventEol(), headerPattern, footerPattern, getCharset(), isStacktraceAsString());
    }
  }


  private ServiceJsonLayout(final Configuration config,
                            final boolean compact, final boolean eventEol,
                            final String headerPattern, final String footerPattern, final Charset charset,
                            final boolean stacktraceAsString) {
    super(config, charset,
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern).setDefaultPattern(DEFAULT_HEADER).build(),
        PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern).setDefaultPattern(DEFAULT_FOOTER).build());
    this.objectWriter = new JacksonFactory.JSON(false, true, stacktraceAsString, false)
        .newWriter(false, false, compact);
    this.compact = compact;
    this.eol = compact && !eventEol ? COMPACT_EOL : DEFAULT_EOL;
  }

  protected String eol;
  protected ObjectWriter objectWriter;
  protected boolean compact;

  protected ServiceJsonLayout(Charset charset) {
    super(charset);
  }

  @Override
  public String toSerializable(final LogEvent event) {
    final StringBuilderWriter writer = new StringBuilderWriter();
    try {
      toSerializable(event, writer);
      return writer.toString();
    } catch (final IOException e) {
      LOGGER.error(e);
      return Strings.EMPTY;
    }
  }

  private void toSerializable(final LogEvent event, final Writer writer) throws IOException {
    objectWriter.writeValue(writer, new Event(event));
    writer.write(eol);
    markEvent();
  }

  private static class Event {
    private final LogEvent event;

    private Event(LogEvent event) {
      this.event = event;
    }

    public ThreadContext.ContextStack getContextStack() {
      return event.getContextStack();
    }

    public ReadOnlyStringMap getContextData() {
      return event.getContextData();
    }

    public StackTraceElement[] getThrownStackTrace() {
      Throwable thrown = event.getThrown();
      StackTraceElement[] trace = thrown != null ? thrown.getStackTrace() : null;
      return trace != null ? Arrays.copyOf(trace, Math.min(trace.length, 10)) : null;
    }

    public String getThrownMessage() {
      Throwable thrown = event.getThrown();
      return thrown != null ? thrown.toString() : null;
    }

    public Level getLevel() {
      return event.getLevel();
    }

    public String getLoggerName() {
      return event.getLoggerName();
    }

    public Message getMessage() {
      return event.getMessage();
    }

    public long getTimeMillis() {
      return event.getTimeMillis();
    }

    public StackTraceElement getSource() {
      return event.getSource();
    }

    public String getThreadName() {
      return event.getLoggerName();
    }

    public String getEventDate() {
      return Instant.ofEpochMilli(event.getTimeMillis()).atZone(ZoneId.systemDefault()).format(DateTimeFormatter.ofPattern(ISO8601));
    }

    public String getServiceName() {
      return AppProperty.getServiceName();
    }

    public String getServiceIp() {
      return AppProperty.getServiceIp();
    }

    public String getMessageType() {
      return event.getContextData().getValue(AppLogger.MESSAGETYPE);
    }

    public String getSubject() {
      return event.getContextData().getValue(AppLogger.SUBJECT);
    }

    public String getObject() {
      return event.getContextData().getValue(AppLogger.OBJECT);
    }

    public String getObjectClassCode() {
      return event.getContextData().getValue(AppLogger.OBJECT_CLASSCODE);
    }

    public String isSuccess() {
      return event.getContextData().getValue(AppLogger.SUCCESS);
    }

    public String getUserName() {
      return event.getContextData().getValue(AppLogger.USERNAME);
    }

    public String getUserIp() {
      return event.getContextData().getValue(AppLogger.USERIP);
    }
  }
}
