package mp.jprime.log;

import mp.jprime.formats.DateFormat;
import mp.jprime.security.ConnectionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Системный журнал
 */
@Service
public class AppLogger {
  private static final Logger LOG = LoggerFactory.getLogger(AppLogger.class);

  public static final String MESSAGETYPE = "messageType";
  public static final String OBJECT = "object";
  public static final String OBJECT_CLASSCODE = "objectClassCode";
  public static final String SUBJECT = "subject";
  public static final String SUCCESS = "success";
  public static final String DATE = "curDate";
  public static final String USERNAME = "userName";
  public static final String USERIP = "userIp";

  public void debug(Event type, String msg) {
    debug(type, null, null, msg, null);
  }

  public void debug(Event type, String msg, ConnectionInfo connInfo) {
    debug(type, null, null, msg, connInfo);
  }

  public void debug(Event type, String subject, String object, String msg, ConnectionInfo connInfo) {
    debug(type, subject, object, null, msg, connInfo);
  }

  public void debug(Event type, String subject, String object, String objectClassCode, String msg,
                    ConnectionInfo connInfo) {
    fillMDC(type, subject, object, objectClassCode, connInfo);
    LOG.debug(msg);
    MDC.clear();
  }

  public void info(Event type, String msg) {
    info(type, null, null, msg, null);
  }

  public void info(Event type, String msg, ConnectionInfo connInfo) {
    info(type, null, null, msg, connInfo);
  }

  public void info(Event type, String subject, String object, String msg,
                   ConnectionInfo connInfo) {
    info(type, subject, object, null, msg, connInfo);
  }

  public void info(Event type, String subject, String object, String objectClassCode, String msg, ConnectionInfo connInfo) {
    fillMDC(type, subject, object, objectClassCode, connInfo);
    LOG.info(msg);
    MDC.clear();
  }

  public void warn(Event type, String msg) {
    warn(type, null, null, msg, null);
  }

  public void warn(Event type, String msg, ConnectionInfo connInfo) {
    warn(type, null, null, msg, connInfo);
  }

  public void warn(Event type, String subject, String object, String msg, ConnectionInfo connInfo) {
    warn(type, subject, object, null, msg, connInfo);
  }

  public void warn(Event type, String subject, String object, String objectClassCode, String msg,
                   ConnectionInfo connInfo) {
    fillMDC(type, subject, object, objectClassCode, connInfo);
    LOG.warn(msg);
    MDC.clear();
  }

  public void error(Event type, String msg) {
    error(type, null, null, msg, null);
  }

  public void error(Event type, String msg, ConnectionInfo connInfo) {
    error(type, null, null, msg, connInfo);
  }

  public void error(Event type, String subject, String object, String msg, ConnectionInfo connInfo) {
    error(type, subject, object, null, msg, connInfo);
  }

  public void error(Event type, String subject, String object, String objectClassCode, String msg,
                    ConnectionInfo connInfo) {
    fillMDC(type, subject, object, objectClassCode, connInfo);
    LOG.error(msg);
    MDC.clear();
  }

  private void fillMDC(Event type, String subject, String object, String objectClassCode,
                       ConnectionInfo connInfo) {
    MDC.put(MESSAGETYPE, type != null ? type.getCode() : "");
    MDC.put(SUBJECT, subject != null ? subject : "");
    MDC.put(OBJECT, object != null ? object : "");
    MDC.put(OBJECT_CLASSCODE, objectClassCode != null ? objectClassCode : "");
    MDC.put(SUCCESS, String.valueOf(type != null && type.isSuccess() ? 1 : 0));
    if (connInfo != null && connInfo.getUsername() != null) {
      MDC.put(USERNAME, connInfo.getUsername());
    } else {
      MDC.put(USERNAME, "");
    }
    if (connInfo != null && connInfo.getUserIP() != null) {
      MDC.put(USERIP, connInfo.getUserIP());
    } else {
      MDC.put(USERIP, "");
    }
    MDC.put(DATE, ZonedDateTime.now().format(DateTimeFormatter.ofPattern(DateFormat.ISO8601)));
  }
}
