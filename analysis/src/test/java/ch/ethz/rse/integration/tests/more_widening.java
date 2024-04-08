package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class more_widening {

    public static void m1() {
        Store s = new Store(50, 120);
        for (int i = 0; i <= 20; i++) {
            if (i < 5) s.get_delivery(2);
            else if (i > 5) s.get_delivery(2);
            else if (i == 5) s.get_delivery(i);
        }
    }
}
