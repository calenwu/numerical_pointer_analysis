package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class SwapStores {

  public static void m1() {
    Store s1 = new Store(3, 5);
    Store s2 = new Store(2, 4); 
    s1.get_delivery(2);
    s2.get_delivery(1);
    Store s;
    s = s2;
    s2 = s1;
    s1 = s;
    s1.get_delivery(2);
    s2.get_delivery(2);
    s2.get_delivery(2);
  }
}