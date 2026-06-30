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
class JpStringFormatParserTest {
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
  void testOgrn() {
    assertTrue(stringFormatParser.parseOgrn("9113797784036").isCheck());
    assertFalse(stringFormatParser.parseOgrn("011231231").isCheck());
    assertFalse(stringFormatParser.parseOgrn("1234567890123").isCheck());
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

  @Test
  void testZagsSeries() {
    assertEquals("IIIАБ", stringFormatParser.parseZagsSeries("IIIАБ").getParseValue());
    assertEquals("IVАБ", stringFormatParser.parseZagsSeries("IV-АБ").getParseValue());
    assertEquals("XVIIIАБ", stringFormatParser.parseZagsSeries("XVIII-АБ").getParseValue());
    assertEquals("IАБ", stringFormatParser.parseZagsSeries("  I-АБ !@#№$;%^:&?*").getParseValue());
    assertEquals("IЁЙ", stringFormatParser.parseZagsSeries("  I-ЁЙ  01(){}[]").getParseValue());

    assertTrue(stringFormatParser.parseZagsSeries(null).isCheck());

    assertFalse(stringFormatParser.parseZagsSeries("VI-А").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VI-АБВ").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VI-Аб").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VI-аб").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VIА").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VIАБВ").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VI-1").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("VI-III").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("АБ-III").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("III-?0%").isCheck());
    assertFalse(stringFormatParser.parseZagsSeries("$1-АБ").isCheck());
  }

  @Test
  void testZagsNumber() {
    assertEquals("123456", stringFormatParser.parseZagsNumber("123456").getParseValue());
    assertEquals("123456", stringFormatParser.parseZagsNumber(" 123456a").getParseValue());

    assertTrue(stringFormatParser.parseZagsNumber(null).isCheck());

    assertFalse(stringFormatParser.parseZagsNumber("1234567").isCheck());
    assertFalse(stringFormatParser.parseZagsNumber("12345").isCheck());
    assertFalse(stringFormatParser.parseZagsNumber("abc").isCheck());
    assertFalse(stringFormatParser.parseZagsNumber("     ").isCheck());
  }

  @Test
  void testZagsAgs() {
    assertEquals("110229010011100331005", stringFormatParser.parseZagsAgs("110229010011100331005").getParseValue());
    assertEquals("12345", stringFormatParser.parseZagsAgs("12345").getParseValue());
    assertEquals("1", stringFormatParser.parseZagsAgs("1").getParseValue());
    assertEquals("123", stringFormatParser.parseZagsAgs(" a 1 2 3 ").getParseValue());

    assertTrue(stringFormatParser.parseZagsAgs(null).isCheck());

    assertFalse(stringFormatParser.parseZagsAgs("123456789012345678901").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("12345678901234567890K").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("12345678901234567890").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("1234567890123456789012").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("1234567").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("abc").isCheck());
    assertFalse(stringFormatParser.parseZagsAgs("     ").isCheck());
  }

  @Test
  void testZagsDepartmentCode() {
    assertEquals("R1234567", stringFormatParser.parseZagsDepartmentCode("R1234567").getParseValue());
    assertEquals("12345678", stringFormatParser.parseZagsDepartmentCode("12345678").getParseValue());
    assertEquals("R1234567", stringFormatParser.parseZagsDepartmentCode("   R1234567   ").getParseValue());

    assertTrue(stringFormatParser.parseZagsDepartmentCode(null).isCheck());

    assertFalse(stringFormatParser.parseZagsDepartmentCode("R12345678").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("R123456").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("123456789").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("1234567").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("r1234567").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("RR1234567").isCheck());
    assertFalse(stringFormatParser.parseZagsDepartmentCode("     ").isCheck());
  }

  @Test
  void testAgsShortNumber() {
    assertEquals("45678", stringFormatParser.parseAgsShortNumber("12345678901234567890K").getParseValue());
    assertEquals("678", stringFormatParser.parseAgsShortNumber("12345678901230067890K").getParseValue());
    assertEquals("678", stringFormatParser.parseAgsShortNumber("00678").getParseValue());

    assertTrue(stringFormatParser.parseAgsShortNumber("12345678901230000090K").isCheck());
    assertTrue(stringFormatParser.parseAgsShortNumber(null).isCheck());
    assertTrue(stringFormatParser.parseAgsShortNumber("").isCheck());
    assertTrue(stringFormatParser.parseAgsShortNumber("     ").isCheck());
  }
}
