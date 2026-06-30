package mp.jprime.schedule.beans;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JpScheduleCronBeanTest {
  @Test
  void test() {
    Assertions.assertEquals("Каждую секунду минуты", JpScheduleCronBean.of("* * * * * *").getDescription());
    Assertions.assertEquals("Каждую 10 секунду минуты", JpScheduleCronBean.of("*/10 * * * * *").getDescription());
    Assertions.assertEquals("Каждый час суток в 15 минут в 0 секунд", JpScheduleCronBean.of("0 15 * * * *").getDescription());
    Assertions.assertEquals("Каждый день месяца в 1 час в 1 минуту в 0 секунд", JpScheduleCronBean.of("0 1 1 * * *").getDescription());
    Assertions.assertEquals("Каждый день месяца c 8 до 10 часов в 0 минут в 0 секунд", JpScheduleCronBean.of("0 0 8-10 * * *").getDescription());
    Assertions.assertEquals("Каждый месяц года с понедельника по пятницу в 10 часов в 15 минут в 0 секунд", JpScheduleCronBean.of("0 15 10 * * 1-5").getDescription());
    Assertions.assertEquals("Каждый месяц года 1 числа в 0 часов в 0 минут в 0 секунд", JpScheduleCronBean.of("0 0 0 1 * *").getDescription());
    Assertions.assertEquals("Каждый день месяца c 9 до 17 часов каждую 15 минуту часа в 0 секунд", JpScheduleCronBean.of("0 */15 9-17 * * *").getDescription());
    Assertions.assertEquals("В июне 3 числа каждую секунду минуты", JpScheduleCronBean.of("* * * 3 6 *").getDescription());
    Assertions.assertEquals("Каждый день месяца в 13 часов в 0 минут в 0 секунд", JpScheduleCronBean.of("0 0 13 * * *").getDescription());
    Assertions.assertEquals("В октябре 10 числа каждый вторник в 10 часов в 10 минут в 10 секунд", JpScheduleCronBean.of("10 10 10 10 10 2").getDescription());
    Assertions.assertEquals("В октябре 10 числа с четверга по субботу в 10 часов в 10 минут в 10 секунд", JpScheduleCronBean.of("10 10 10 10 10 4-6").getDescription());
    Assertions.assertEquals("С января по декабрь каждую секунду минуты", JpScheduleCronBean.of("* * * * JAN-DEC * ").getDescription());
    Assertions.assertEquals("С понедельника по воскресенье каждую секунду минуты", JpScheduleCronBean.of("* * * * * mon-sun").getDescription());
    Assertions.assertEquals("Каждый 5 месяц года каждый 5 день месяца каждый 5 день недели каждый 5 час суток каждую 5 минуту часа каждую 5 секунду минуты", JpScheduleCronBean.of("*/5 */5 */5 */5 */5 */5").getDescription());
    Assertions.assertEquals("Каждый 2 месяц года каждый 2 день месяца каждый 2 день недели каждый 5 час суток каждую 5 минуту часа каждую 5 секунду минуты", JpScheduleCronBean.of("*/5 */5 */5 */2 */2 */2").getDescription());
    Assertions.assertEquals("В мае, октябре 5, 10 числа в пятницу, воскресенье каждую секунду минуты", JpScheduleCronBean.of("* * * 5,10 5,10 5,7").getDescription());
    Assertions.assertEquals("Каждую секунду минуты", JpScheduleCronBean.of("*/1 * * * * *").getDescription());
    Assertions.assertEquals("Каждый день месяца каждую секунду минуты", JpScheduleCronBean.of("* * * */1 * *").getDescription());
    Assertions.assertEquals("Каждый месяц года каждую секунду минуты", JpScheduleCronBean.of("* * * * */1 *").getDescription());
    Assertions.assertEquals("Каждый день недели каждую секунду минуты", JpScheduleCronBean.of("* * * * * */1").getDescription());
    Assertions.assertEquals("Каждый день месяца в 0 часов в 0 минут каждую секунду минуты", JpScheduleCronBean.of("* 0 0 * * *").getDescription());
    Assertions.assertEquals("Каждый месяц года каждую пятницу в 0 часов в 0 минут в 0 секунд", JpScheduleCronBean.of("0 0 0 * * 5").getDescription());
    Assertions.assertEquals("Каждый день месяца в 0 часов в 0 минут каждую секунду минуты", JpScheduleCronBean.of("* 0 0 * * *").getDescription());
    Assertions.assertEquals("Каждый день месяца в 0 часов каждую минуту часа в 0 секунд", JpScheduleCronBean.of("0 * 0 * * *").getDescription());
    Assertions.assertEquals("Каждый день месяца в 5, 10 часов в 5, 10 минут в 5, 10 секунд", JpScheduleCronBean.of("5,10 5,10 5,10 * * *").getDescription());
    Assertions.assertEquals("Каждую секунду минуты", JpScheduleCronBean.of("*/1 * * * * *").getDescription());
  }
}
