package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class For_Widening {

  public static void m1() {
    int j = 1;
    for (int i = 0; i < 7; i++){
        j = j * 2;
    }
    Store s = new Store(10, 400);
    s.get_delivery(j);
  }
}