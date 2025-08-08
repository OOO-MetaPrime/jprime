package mp.kafka.concurrent;

import jakarta.annotation.Nonnull;
import mp.jprime.concurrent.PriorityTask;
import mp.jprime.concurrent.PriorityTaskScheduler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
@Tag("manualTests")
public class PriorityTaskSchedulerTest {
  @Lazy(value = false)
  @Configuration
  @ComponentScan(value = {"mp"})
  public static class Config {
  }

  static class TimePriorityTask implements PriorityTask {
    private final int idx;
    private final Collection<Integer> runs;
    private final CountDownLatch cdl;
    private final long sendAfter;

    public TimePriorityTask(int idx, Collection<Integer> runs, CountDownLatch cdl, long sendAfter) {
      this.idx = idx;
      this.runs = runs;
      this.cdl = cdl;
      this.sendAfter = sendAfter;
    }

    @Override
    public int compareTo(@Nonnull PriorityTask o) {
      if (!(o instanceof TimePriorityTask other)) {
        return -1;
      }
      int comparison = Long.compare(this.sendAfter, other.sendAfter);
      return comparison != 0 ? comparison : Long.compare(this.idx, other.idx);
    }

    @Override
    public void run() {
      runs.add(this.idx);
      // System.out.println("RUN " + this.idx);
      try {
        TimeUnit.MILLISECONDS.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
      cdl.countDown();
    }
  }

  @Test
  void priority_test() {
    final PriorityTaskScheduler pts = new PriorityTaskScheduler(2, "PriorityTaskScheduler-", 10);

    Collection<Integer> runs = new ConcurrentLinkedQueue<>();
    CountDownLatch cdl = new CountDownLatch(5);

    long ts = System.currentTimeMillis();
    pts.schedule(Arrays.asList(
        new TimePriorityTask(1, runs, cdl, ts + 1_000),
        new TimePriorityTask(2, runs, cdl, ts + 5_000),
        new TimePriorityTask(3, runs, cdl, ts + 4_000),
        new TimePriorityTask(4, runs, cdl, ts + 2_000),
        new TimePriorityTask(5, runs, cdl, ts + 3_000)
    ));

    pts.run();

    try {
      cdl.await();
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    pts.shutdown();

    // System.out.println(runs);

    Assertions.assertIterableEquals(
        Arrays.asList(1,4,5,3,2),
        runs
    );
  }
}
