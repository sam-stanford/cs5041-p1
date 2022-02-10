package utils;

import java.util.Random;

public class Randomiser {

  private static Random generator = new Random();

  public static int intZeroToN(int n) {
    return generator.nextInt(n);
  }

  public static int int0To100() {
    return intZeroToN(100);
  }

  public static boolean bool() {
    return generator.nextBoolean();
  }

  public static float floatZeroToOne() {
    return generator.nextFloat();
  }
}
