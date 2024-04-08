package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE SAFE

public class testpointer {

    public void m3(int j) {
        Store s = new Store(3, 6);
        s.get_delivery(3);

        Store s2 = new Store(1, 4);
        s2.get_delivery(1);
        s2.get_delivery(3);
        s.get_delivery(3);
    }
}
