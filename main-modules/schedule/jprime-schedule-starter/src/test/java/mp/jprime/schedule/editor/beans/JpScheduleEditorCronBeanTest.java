package mp.jprime.schedule.editor.beans;

import mp.jprime.schedule.JpScheduleType;
import mp.jprime.schedule.editor.JpScheduleEditorCron;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
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
    Assertions.assertNull(c1.getExpression());

    JpScheduleEditorCron c2 = JpScheduleEditorCronBean.of(
        JpScheduleEditorCron.ofSecondConfig(JpScheduleType.EVERY_N, 10, null),
        JpScheduleEditorCron.ofMinuteConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofHourConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofDayConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofMonthConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofDayOfWeekConfig(JpScheduleType.EVERY, null, null)
    );
    Assertions.assertEquals("*/10 * * * * *", c2.getExpression());

    JpScheduleEditorCron c3 = JpScheduleEditorCronBean.of(
        JpScheduleEditorCron.ofSecondConfig(JpScheduleType.SPECIFIC, null, List.of(20, 40)),
        JpScheduleEditorCron.ofMinuteConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofHourConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofDayConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofMonthConfig(JpScheduleType.EVERY, null, null),
        JpScheduleEditorCron.ofDayOfWeekConfig(JpScheduleType.SPECIFIC, null, List.of(1, 3))
    );
    Assertions.assertEquals("20,40 * * * * 1,3", c3.getExpression());
  }

  @Test
  void when_emptyOrNullSpecific_then_nullExpression() {
    JpScheduleEditorCron cron = JpScheduleEditorCronBean.of(
        JpScheduleEditorCron.ofSecondConfig(JpScheduleType.SPECIFIC, null, null),
        JpScheduleEditorCron.ofMinuteConfig(JpScheduleType.SPECIFIC, null, Collections.emptyList()),
        JpScheduleEditorCron.ofHourConfig(JpScheduleType.SPECIFIC, 5, null),
        JpScheduleEditorCron.ofDayConfig(JpScheduleType.SPECIFIC, 5, Collections.emptyList()),
        JpScheduleEditorCron.ofMonthConfig(JpScheduleType.SPECIFIC, null, Collections.emptyList()),
        JpScheduleEditorCron.ofDayOfWeekConfig(JpScheduleType.SPECIFIC, null, Collections.emptyList())
    );
    Assertions.assertNull(cron.getExpression());
  }
}
