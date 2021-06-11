package mp.jprime.monitoring.jvm;

import mp.jprime.log.AppLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.lang.management.*;
import java.text.DecimalFormat;
import java.util.function.ToLongFunction;

/**
 * Контроль памяти JVM
 */
@EnableScheduling
@Lazy(value = false)
@Configuration
public class JvmMemoryMonitoring {
  private static final long MB = 1024L * 1024L;
  private static final long GB = 1024L * MB;

  /**
   * Системный журнал
   */
  private AppLogger appLogger;

  @Autowired
  private void setAppLogger(AppLogger appLogger) {
    this.appLogger = appLogger;
  }

  @Value("${jprime.monitoring.jvm.memory.alarmPercents:90}")
  private int alarmPercents;

  @Scheduled(cron = "${jprime.monitoring.jvm.memory.checkTimeout:0 */1 * * * *}")
  public void check() {
    Values heap = new Values();
    Values nonheap = new Values();
    for (MemoryPoolMXBean memoryPoolBean : ManagementFactory.getPlatformMXBeans(MemoryPoolMXBean.class)) {
      Values area = MemoryType.HEAP.equals(memoryPoolBean.getType()) ? heap : nonheap;

      area.used += getUsageValue(memoryPoolBean, MemoryUsage::getUsed);
      area.max += getUsageValue(memoryPoolBean, MemoryUsage::getMax);
    }
    if (heap.used * 100 / heap.max >= alarmPercents) {
      appLogger.warn(Event.JVM_HEAP_MEMORY_LIMIT_WARNINIG, "The limit of used heap memory " + getWarning(heap.used, heap.max));
    }
    if (nonheap.used * 100 / nonheap.max >= alarmPercents) {
      appLogger.warn(Event.JVM_NONHEAP_MEMORY_LIMIT_WARNINIG, "The limit of used nonheap memory " + getWarning(nonheap.used, nonheap.max));
    }
  }

  private static MemoryUsage getUsage(MemoryPoolMXBean memoryPoolMXBean) {
    try {
      return memoryPoolMXBean.getUsage();
    } catch (InternalError e) {
      // Defensive for potential InternalError with some specific JVM options. Based on its Javadoc,
      // MemoryPoolMXBean.getUsage() should return null, not throwing InternalError, so it seems to be a JVM bug.
      return null;
    }
  }

  private static double getUsageValue(MemoryPoolMXBean memoryPoolMXBean, ToLongFunction<MemoryUsage> getter) {
    MemoryUsage usage = getUsage(memoryPoolMXBean);
    if (usage == null) {
      return Double.NaN;
    }
    return getter.applyAsLong(usage);
  }

  private static class Values {
    private double used;
    private double max;
  }

  private String getWarning(double used, double max) {
    double m = used / MB;
    double g = used / GB;

    DecimalFormat dec = new DecimalFormat("0.00");

    if (g > 0) {
      return dec.format(g) + " GB / " + dec.format(max / GB) + " GB";
    } else {
      return dec.format(m) + " MB / " + dec.format(max / MB) + " MB";
    }
  }
}
