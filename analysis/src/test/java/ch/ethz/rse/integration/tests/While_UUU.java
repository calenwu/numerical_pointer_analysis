package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class While_UUU {

  public static void m1(int a) {
    Store s = new Store(1, 5);
    Store s1 = new Store(2, 7);
    s1.get_delivery(21);
    s.get_delivery(6);
    s = s1;
    s1 = new Store(5, 8);
    s1.get_delivery(10);
    if (a > 5){
      s1 = new Store(8, 9);
    }
    else {
      s1 = new Store(12, 14);
    }
    int j = 10;
    s1.get_delivery(9);
    while(j>0) {
      j = j - 1;
      s.get_delivery(j);
    }
    s1.get_delivery(j);
    s.get_delivery(j);
  }
}