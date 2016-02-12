package org.gain2f.threading;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * Created by kiselev on 12.02.2016.
 */
public class PerformanceTesterImpl implements PerformanceTester {

  public PerformanceTestResult runPerformanceTest(Runnable task,
                                                  int executionCount,
                                                  int threadPoolSize) throws InterruptedException {
    ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
    Collection<Callable<Duration>> tasks = new HashSet<>();
    for (int i = 0; i < executionCount; i++)
      tasks.add(() -> {
        Instant start = Instant.now();
        task.run();
        return Duration.between(start, Instant.now());
      });

    Instant totalStart = Instant.now();
    try {
      List<Future<Duration>> futures = executor.invokeAll(tasks); // run all tasks
      List<Duration> sortedDurations = futures.stream().map(future -> {
        try {
          return future.get();
        } catch (Exception e) {
          throw new RuntimeException(e); // we can check failed tests here but given interface does not provide failing
        }
      }).sorted().collect(Collectors.toList()); // invoke each task and sort them by duration

      Duration totalDuration = Duration.between(totalStart, Instant.now());
      return new PerformanceTestResult(totalDuration.toMillis(),
                                       sortedDurations.get(0).toMillis(),
                                       sortedDurations.get(executionCount - 1).toMillis());
    } finally {
      executor.shutdown();
    }
  }
}
