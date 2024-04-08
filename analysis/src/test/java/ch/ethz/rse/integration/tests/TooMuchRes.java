package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class TooMuchRes {

  public static void m1() {
    Store s = new Store(2, 5);
    s.get_delivery(2);
    s.get_delivery(2);
    s.get_delivery(2);
  }
}