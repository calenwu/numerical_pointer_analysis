package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class breaking {

    public static void m1() {

        Store s1 = new Store(1, 2);
        for (int i = 0; i < 2; i++) {
            Store s2 = new Store(1, 3);
            if (i == 0) {
                s1 = s2;
            }
            s1.get_delivery(1);
        }
    }

}
