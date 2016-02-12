package org.gain2f.threading;

import junit.framework.TestCase;

/**
 * Created by kiselev on 12.02.2016.
 */
public class Performance_UnitTest extends TestCase {

  private PerformanceTester performanceTester;

  public void setUp() throws Exception {
    performanceTester = new PerformanceTesterImpl();
  }

  public void test25_5_1() throws Exception {
    PerformanceTestResult performanceTestResult = performanceTester.runPerformanceTest(
           () -> /*System.out.println(*/new FibCalcImpl().fib(25)/*)*/, 5, 1);
    System.out.println("MinTime: " + performanceTestResult.getMinTime());
    System.out.println("MaxTime: " + performanceTestResult.getMaxTime());
    System.out.println("TotalTime: " + performanceTestResult.getTotalTime());
  }

  public void testFibCalcImpl() {
    System.out.println(new FibCalcImpl().fib(4));
  }
}
