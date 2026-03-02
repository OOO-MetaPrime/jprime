package mp.jprime.parsers.stringformat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
    JpStringFormatParserTest.Config.class
})
public class JpStringFormatParserTest {
  @Autowired
  private JpStringFormatParser stringFormatParser;

  @Configuration
  @ComponentScan(
      basePackages = {"mp.jprime.parsers.stringformat.services"}
  )
  @EnableConfigurationProperties
  public static class Config {
  }

  @Test
  void testBik() {
    assertEquals("111111111", stringFormatParser.parseBik("111111111").getParseValue());
    assertFalse(stringFormatParser.parseBik("1111111111").isCheck());
  }

  @Test
  void testBankCardNumber() {
    assertEquals("4014878948807545", stringFormatParser.parseBankCardNumber("4014 8789 4880 7545").getParseValue());
    assertFalse(stringFormatParser.parseBankCardNumber("4014878948807546").isCheck());
  }

  @Test
  void testEmail() {
    assertEquals("test@mail.ru", stringFormatParser.parseEmail("test@mail.ru").getParseValue());
    assertTrue(stringFormatParser.parseEmail(null).isCheck());
    assertFalse(stringFormatParser.parseEmail("testmail.ru").isCheck());
    assertFalse(stringFormatParser.parseEmail("qwertyuiopas").isCheck());
    assertFalse(stringFormatParser.parseEmail("testmail@").isCheck());
    assertFalse(stringFormatParser.parseEmail("@ru").isCheck());
    assertFalse(stringFormatParser.parseEmail("@ru.").isCheck());;
    assertFalse(stringFormatParser.parseEmail("@ru.ru").isCheck());;
  }

  @Test
  void testInn() {
    assertEquals("773370857141", stringFormatParser.parseInn("773370857141").getParseValue());
    assertEquals("7802732322", stringFormatParser.parseInn10("7802732322").getParseValue());
    assertEquals("773370857141", stringFormatParser.parseInnAny("773370857141").getParseValue());
    assertEquals("7802732322", stringFormatParser.parseInnAny("7802732322").getParseValue());

    assertTrue(stringFormatParser.parseInn(null).isCheck());
    assertTrue(stringFormatParser.parseInn10(null).isCheck());
    assertTrue(stringFormatParser.parseInnAny(null).isCheck());
    assertFalse(stringFormatParser.parseInn("77337085714").isCheck());
    assertFalse(stringFormatParser.parseInn10("77337085714").isCheck());
    assertFalse(stringFormatParser.parseInnAny("77337085714").isCheck());
    assertFalse(stringFormatParser.parseInn("qwertyuiopas").isCheck());
    assertFalse(stringFormatParser.parseInn10("qwertyuiopas").isCheck());
    assertFalse(stringFormatParser.parseInnAny("qwertyuiopas").isCheck());
  }

  @Test
  void testKbk() {
    assertEquals("11111111111111111111", stringFormatParser.parseKbk("11111111111111111111").getParseValue());
    assertFalse(stringFormatParser.parseKbk("1111111111111111111").isCheck());
  }

  @Test
  void testKpp() {
    assertEquals("111111111", stringFormatParser.parseKpp("111111111").getParseValue());
    assertFalse(stringFormatParser.parseKpp("1111111111").isCheck());
  }

  @Test
  void testOktmo() {
    assertEquals("01123123", stringFormatParser.parseOktmo("01123123").getParseValue());
    assertFalse(stringFormatParser.parseOktmo("011231231").isCheck());
  }

  @Test
  void testOktmo11() {
    assertEquals("01123123123", stringFormatParser.parseOktmo11("01123123123").getParseValue());
    assertFalse(stringFormatParser.parseOktmo11("011231231231").isCheck());
  }

  @Test
  void testPhone() {
    assertEquals("78121234567", stringFormatParser.parsePhone("88121234567").getParseValue());
    assertTrue(stringFormatParser.parsePhone(null).isCheck());
    assertFalse(stringFormatParser.parsePhone("881212345").isCheck());
    assertFalse(stringFormatParser.parsePhone("qwertyuiopas").isCheck());
  }


  @Test
  void testSnils() {
    assertEquals("13351440622", stringFormatParser.parseSnils("133-514 406 22").getParseValue());
    assertTrue(stringFormatParser.parseSnils(null).isCheck());
    assertFalse(stringFormatParser.parseSnils("12345678905").isCheck());
    assertFalse(stringFormatParser.parseSnils("qwertyuiopas").isCheck());
  }

  @Test
  void testErn() {
    assertEquals("123456789012", stringFormatParser.parseErn("123456789012").getParseValue());;

    assertTrue(stringFormatParser.parseErn(null).isCheck());
    assertFalse(stringFormatParser.parseErn("12345678901").isCheck());
    assertFalse(stringFormatParser.parseErn("12345678901, abc").isCheck());
    assertFalse(stringFormatParser.parseErn("1234567890123").isCheck());
    assertFalse(stringFormatParser.parseErn(" 23456789012").isCheck());
    assertFalse(stringFormatParser.parseErn("12345678901 ").isCheck());
    assertFalse(stringFormatParser.parseErn("a23456789012").isCheck());
    assertFalse(stringFormatParser.parseErn("12345678901a").isCheck());
    assertFalse(stringFormatParser.parseErn("1234567890,?").isCheck());
    assertFalse(stringFormatParser.parseErn("qwertyuiopas").isCheck());
  }

  @Test
  void testFio() {
    assertEquals("Петров", stringFormatParser.parseFio(" ПЕТРОВ ").getParseValue());
    assertEquals("Петров", stringFormatParser.parseFio(" петров").getParseValue());
    assertEquals("Петров", stringFormatParser.parseFio("пЕтРов ").getParseValue());

    assertTrue(stringFormatParser.parseFio(null).isCheck());
    assertTrue(stringFormatParser.parseFio("     ").isCheck());
  }
}
