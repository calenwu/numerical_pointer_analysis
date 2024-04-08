package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE
public class sss {

    public static void m() {
        int k;
        int j = 2;
        j = 2;
        k = j;
        Store s = new Store(j, 4);
        s.get_delivery(2);
        s.get_delivery(1);
    }
}
