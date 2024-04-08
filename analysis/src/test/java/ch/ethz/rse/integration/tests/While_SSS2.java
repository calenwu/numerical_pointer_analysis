// DISABLED
package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class While_SSS2 {

  public static void m1() {
    Store s = new Store(5, 9);
    int i = 4;
    int j = 3;
    while(j>0) {
      i = i - 1;
      j = j - 1;
      s.get_delivery(i);
    }
    s.get_delivery(j);
  }
}