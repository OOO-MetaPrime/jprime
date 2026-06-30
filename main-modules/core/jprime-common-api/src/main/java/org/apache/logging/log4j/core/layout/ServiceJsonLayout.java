package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.annotation.JsonInclude;
import mp.jprime.log.AppLogger;
import mp.jprime.system.AppProperty;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.util.StringBuilderWriter;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.util.ReadOnlyStringMap;
import org.apache.logging.log4j.util.Strings;
import tools.jackson.core.JsonGenerator;
import tools.jackson.core.util.DefaultPrettyPrinter;
import tools.jackson.core.util.MinimalPrettyPrinter;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.json.JsonMapper;
import tools.jackson.databind.module.SimpleModule;
import tools.jackson.databind.ser.std.SimpleBeanPropertyFilter;
import tools.jackson.databind.ser.std.SimpleFilterProvider;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static mp.jprime.formats.DateFormat.ISO8601;

@Plugin(name = "ServiceJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public class ServiceJsonLayout extends AbstractStringLayout {
  private static final String DEFAULT_FOOTER = "]";
  private static final String DEFAULT_HEADER = "[";
  private static final String DEFAULT_EOL = "\r\n";
  private static final String COMPACT_EOL = Strings.EMPTY;

  protected String eol;
  protected JsonMapper objectWriter;
  protected boolean compact;


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


    Set<String> except = new HashSet<>(3);
    except.add(JsonConstants.ELT_TIME_MILLIS);
    except.add(JsonConstants.ELT_NANO_TIME);

    SimpleFilterProvider filters = new SimpleFilterProvider();
    filters.addFilter(Log4jLogEvent.class.getName(), SimpleBeanPropertyFilter.serializeAllExcept(except));

    SimpleModule module = new SimpleModule();
    if (stacktraceAsString) {
      module.addSerializer(Throwable.class, new ValueSerializer<>() {
        @Override
        public void serialize(Throwable value, JsonGenerator gen, SerializationContext serializers) {
          // Превращаем стектрейс в одну строку
          gen.writeString(ExceptionUtils.getStackTrace(value));
        }
      });
    }

    JsonMapper.Builder jsonBuilder = JsonMapper.builder();
    jsonBuilder.changeDefaultPropertyInclusion(incl -> incl.withValueInclusion(JsonInclude.Include.NON_EMPTY));
    jsonBuilder.defaultPrettyPrinter(compact ? new MinimalPrettyPrinter() : new DefaultPrettyPrinter());
    jsonBuilder.addModule(module);

    this.objectWriter = jsonBuilder.build();
    this.compact = compact;
    this.eol = compact && !eventEol ? COMPACT_EOL : DEFAULT_EOL;
  }

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

    public Map<String, String> getContextData() {
      ReadOnlyStringMap contextData = event.getContextData();
      return contextData != null ? contextData.toMap() : null;
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

    public String getLevel() {
      Level level = event.getLevel();
      return level != null ? level.getStandardLevel().name() : null;
    }

    public String getLoggerName() {
      return event.getLoggerName();
    }

    public String getMessage() {
      Message message = event.getMessage();
      return message != null ? message.getFormattedMessage() : null;
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
