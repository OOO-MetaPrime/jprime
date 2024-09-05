package mp.jprime.meta.services;

import mp.jprime.beans.JPPropertyType;
import mp.jprime.json.services.JPJsonMapper;
import mp.jprime.meta.JPAttrCsvWriterService;
import mp.jprime.meta.JPClass;
import mp.jprime.meta.beans.*;
import mp.jprime.meta.json.converters.JPClassJsonBaseConverter;
import mp.jprime.parsers.ParserServiceAwareConfiguration;
import mp.jprime.parsers.base.BooleanToStringParser;
import mp.jprime.parsers.base.IntegerToStringParser;
import mp.jprime.parsers.services.ParserCommonService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

@SpringBootTest(classes = {JPAttrCsvWriterCommonService.class, JPClassJsonBaseConverter.class, JPJsonMapper.class,
    ParserCommonService.class, ParserServiceAwareConfiguration.class,
    IntegerToStringParser.class, BooleanToStringParser.class})
@ActiveProfiles("JPAttrCsvWriterTest")
public class JPAttrCsvWriterTest {
  private static final JPClass TEST_VALUE = JPClassBean.newBuilder().attrs(new ArrayList<>() {{
        this.add(JPAttrBean.newBuilder().code("code1").name("name1").virtualReference(JPVirtualPathBean.newInstance(new String[]{"class;", "attr"}, JPType.INT)).build());
        this.add(JPAttrBean.newBuilder().code("code2").name("name2").refJpFile(JPFileBean.newBuilder("attr").build()).build());
        this.add(JPAttrBean.newBuilder().code("code3").name("name3").build());
        this.add(JPAttrBean.newBuilder().code("code4").name("name4").schemaProps(List.of(JPPropertyBean.builder().code("prop1Code").type(JPPropertyType.INT).build())).build());
        this.add(JPAttrBean.newBuilder().build());
      }})
      .build();

  @Value("classpath:ExpectedWriterResult.csv")
  private Resource expectedWriterResult;
  @Autowired
  private JPAttrCsvWriterService writerService;

  @Test
  void shouldWrite() {
    String lineSplit = "\r\n";
    try (InputStream exp = expectedWriterResult.getInputStream();
         InputStream res = writerService.of(TEST_VALUE, lineSplit)) {
      String[] expLines = StringUtils.split(new String(exp.readAllBytes(), UTF_8), lineSplit);
      String[] resLines = StringUtils.split(new String(res.readAllBytes(), UTF_8), lineSplit);

      Assertions.assertEquals(expLines.length, resLines.length);
      for (int i = 0; i < expLines.length; i++) {
        Assertions.assertEquals(expLines[i], resLines[i]);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
