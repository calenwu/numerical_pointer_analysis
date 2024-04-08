package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class While_Widening_100 {

  public static void m1() {
    Store s = new Store(10, 95);
    int j = 100;
    while(j>0) {
      j = j - 1;
      s.get_delivery(1);
    }
  }
}