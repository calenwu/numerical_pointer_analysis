package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Neg_Constructor_Reserve {

  public static void m1() {
    Store s = new Store(0, -5);
    s.get_delivery(-5);
  }
}