package mp.jprime.mail.services;

import mp.jprime.mail.JPMailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import java.util.Properties;

/**
 * Логика работы с электронной почтой
 */
@Service
@ConditionalOnProperty(value = "jprime.mail.enabled", havingValue = "true")
public final class JPMailCommonService implements JPMailService {
  private final static Logger LOG = LoggerFactory.getLogger(JPMailCommonService.class);

  private JavaMailSender mailSender;

  @Value("${jprime.mail.host}")
  private String host;
  @Value("${jprime.mail.username}")
  private String username;
  @Value("${jprime.mail.password}")
  private String password;
  @Value("${jprime.mail.port}")
  private int port;
  @Value("${jprime.mail.protocol}")
  private String protocol;

  private JavaMailSender getMailSender() {
    if (this.mailSender == null) {
      JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
      mailSender.setHost(host);
      mailSender.setPort(port);
      mailSender.setUsername(username);
      mailSender.setPassword(password);

      Properties properties = mailSender.getJavaMailProperties();
      properties.setProperty("mail.transport.protocol", protocol);

      this.mailSender = mailSender;
    }
    return this.mailSender;
  }


  @Override
  public void sendMessage(String subject, String text, String... emailList) {
    if (emailList == null) {
      return;
    }

    for (String email : emailList) {
      try {
        sendMessage(email, subject, text);
      } catch (Exception e) {
        LOG.error(e.getMessage(), e);
      }
    }
  }

  @Override
  public void sendMessageOrThrow(String subject, String text, String... emailList) {
    if (emailList == null) {
      return;
    }

    for (String email : emailList) {
      sendMessage(email, subject, text);
    }
  }

  private void sendMessage(String to, String subject, String text) {
    if (to == null || !to.contains("@")) {
      return;
    }
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(username);
    message.setTo(to.trim());
    message.setSubject(subject);
    message.setText(text);
    getMailSender().send(message);
  }
}
