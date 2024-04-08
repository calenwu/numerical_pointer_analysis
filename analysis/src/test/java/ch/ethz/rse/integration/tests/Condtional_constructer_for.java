package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class Condtional_constructer_for {

  public static void m1(int j) {
    Store s = new Store(4, 4);
    if(5 < j) {
      for(int i = 0; i<j; i++) {
        s = new Store(7, 3);
      }
    }
    else {
      s = new Store(6, 4);
    }
    s.get_delivery(j);
  }
}