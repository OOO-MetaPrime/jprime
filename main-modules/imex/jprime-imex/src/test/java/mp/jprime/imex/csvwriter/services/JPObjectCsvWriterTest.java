package mp.jprime.imex.csvwriter.services;

import mp.jprime.concurrent.JPCompletableFuture;
import mp.jprime.dataaccess.beans.JPData;
import mp.jprime.dataaccess.beans.JPObject;
import mp.jprime.dataaccess.beans.JPObjectBase;
import mp.jprime.meta.beans.JPAttrBean;
import mp.jprime.parsers.ParserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class JPObjectCsvWriterTest {
  private static final Collection<JPObject> TEST_VALUES = List.of(
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 1", "secondCode", "Второе значение 1"))
      ),
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 2", "secondCode", "Второе значение 2"))
      ),
      JPObjectBase.newBaseInstance(
          "jpClassCode",
          "jpClassId",
          JPData.of(Map.of("firstCode", "Id 3", "secondCode", "Второе значение 3"))
      )
  );
  private static final String EXPECTED_RESULT = "\"Имя первого\",\"Второе имя\"\n" +
      "\"Id 1\",\"Второе значение 1\"\n" +
      "\"Id 2\",\"Второе значение 2\"\n" +
      "\"Id 3\",\"Второе значение 3\"\n";

  @Test
  void test() {
    ParserService mockParserService = Mockito.mock(ParserService.class);
    Mockito.when(mockParserService.toString(Mockito.any())).thenAnswer(x -> x.getArgument(0));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try (JPObjectCsvWriter writer = JPObjectCsvWriter.newBuilder(
            baos,
            mockParserService,
            List.of(
                JPAttrBean.newBuilder().code("firstCode").name("Имя первого").build(),
                JPAttrBean.newBuilder().code("secondCode").name("Второе имя").build()
            )
        )
        .build()) {
      JPCompletableFuture.runAsync(() -> writer.write(TEST_VALUES)).join();
    }
    Assertions.assertEquals(EXPECTED_RESULT, baos.toString());
  }
}
