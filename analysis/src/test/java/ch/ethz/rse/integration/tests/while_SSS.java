package ch.ethz.rse.integration.tests;
import ch.ethz.rse.Store;


// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class while_SSS {

  public static void m1() {
    Store s = new Store(3, 4);
    int i = 2;
    int j = 5;
    while(0 < j) {
      i = i - 1;
      j = j - 1;
    }
    s.get_delivery(i);
  }
}