package mp.jprime.parsers;

import mp.jprime.parsers.doctypeformat.JpDocTypeFormatParser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = JpDocTypeFormatParserTest.Config.class)
public class JpDocTypeFormatParserTest {
  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers.doctypeformat"},
      excludeFilters = {
          @ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE,
              value = {
              }
          )
      })
  @EnableConfigurationProperties
  public static class Config {
  }

  @Autowired
  private JpDocTypeFormatParser docTypeFormatParser;

  @Test
  void testDocType() {
    assertTrue(docTypeFormatParser.parseRussiaBirthCertificate("lX-ИК", "123322").isCheck());
    assertTrue(docTypeFormatParser.parseRussiaBirthCertificate("lXИК", "123322").isCheck());
    assertTrue(docTypeFormatParser.parseRussiaBirthCertificate("XI-ВВ", "123322").isCheck());
    assertTrue(docTypeFormatParser.parseRussiaBirthCertificate("XIВВ", "123322").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaBirthCertificate("X.I-ВВ", "123322").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaBirthCertificate("XV-МММ", "123322").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaBirthCertificate("123322", "lX-ИК").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaBirthCertificate("lX-ИК 123322", null).isCheck());
    assertFalse(docTypeFormatParser.parseRussiaBirthCertificate("Ф", "11").isCheck());

    assertTrue(docTypeFormatParser.parseRussiaMilitatyId("АА", "111111").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaMilitatyId("ZZ", "111111").isCheck());

    assertTrue(docTypeFormatParser.parseRussiaPassport("4004", "374958").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("4004", "").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("4004", null).isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("374958", "4004").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("4004 374958", null).isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("40041", "374958").isCheck());
    assertFalse(docTypeFormatParser.parseRussiaPassport("Ф", "11").isCheck());
  }
}
