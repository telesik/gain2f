package org.gain2f.threading;

/**
 * Created by kiselev on 12.02.2016.
 */
public class FibCalcImpl implements FibCalc {

  @Override
  public long fib(int n) {
    if (n < 1) throw new RuntimeException("index should be natural number");
    long a = 0;
    long b = 1;
    long num = 1;
    for (int i = 1; i < n; i++) {
//      System.out.print(num + ", ");
      num = a + b;
      a = b;
      b = num;
    }
/*
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
*/
    return num;
  }
}
