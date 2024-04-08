package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class uuu {
    public void m2(int j) {
        Store s = new Store(1, 2);
        if (-1 <= j && j <= 3)
            s.get_delivery(j);
        s.get_delivery(1);
    }

    public void m3(int j) {
        Store s = new Store(1, 2);
        if (-1 < j && j <= 3)
            s.get_delivery(j);
        s.get_delivery(1);
    }


    public static void m1() {
        Store s = new Store(0, 0);
        s.get_delivery(-5);
        s.get_delivery(1);
    }
}