package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class uss {
    public void m1(int j) {
        Store s = new Store(3, 12);
        for (int i = -5; i < 1; i++) {
            if (j == i) {
                s.get_delivery(i);
            }
        }
    }

    public void m2(int j) {
        Store s = new Store(3, 12);
        for (int i = -5; i < 1; i++) {
            if (j == i) {
                s.get_delivery(j);
            }
        }
    }

    public void m3(int j) {
        Store s = new Store(3, 12);
        for (int i = -5; i < 1; i++) {
            s.get_delivery(i);
        }
    }

    public void m4() {
        int j = 4;
        for (int i = 1; i < 4; i++) {
            j -= i;
        }
        Store s = new Store(10, 20);
        s.get_delivery(j);
    }

}