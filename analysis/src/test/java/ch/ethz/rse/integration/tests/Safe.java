package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Safe {

  public static void m1() {
    Store s = new Store(1, 43);
    s.get_delivery(1);
  }
}