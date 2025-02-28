package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class While_USS2 {

  public static void m1() {
    Store s = new Store(10, 10);
    int j = 6;
    while(j>0) {
      j = j - 1;
    }
    s.get_delivery(j);
  }
}