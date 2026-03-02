package mp.jprime.schedule.editor.beans;

import mp.jprime.schedule.JpScheduleType;
import mp.jprime.schedule.editor.JpScheduleEditorCron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JpScheduleEditorCronBeanTest {
  @Test
  void test() {
    JpScheduleEditorCron c1 = JpScheduleEditorCronBean.of(
        null,
        null,
        null,
        null,
        null,
        null
    );
    Assertions.assertEquals("* * * * * *", c1.getExpression());

    JpScheduleEditorCron c2 = JpScheduleEditorCronBean.of(
        JpScheduleEditorCron.ofSecondConfig(JpScheduleType.EVERY_N, 10, null),
        null,
        null,
        null,
        null,
        null
    );
    Assertions.assertEquals("*/10 * * * * *", c2.getExpression());

    JpScheduleEditorCron c3 = JpScheduleEditorCronBean.of(
        JpScheduleEditorCron.ofSecondConfig(JpScheduleType.SPECIFIC, null, List.of(20, 40)),
        null,
        null,
        null,
        null,
        JpScheduleEditorCron.ofDayOfWeekConfig(JpScheduleType.SPECIFIC, null, List.of(1, 3))
    );
    Assertions.assertEquals("20,40 * * * * 1,3", c3.getExpression());
  }
}
