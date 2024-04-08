package ch.ethz.rse.integration.tests;


import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE
public class more_constructors {

    public static void m1(int i) {
        Store s1 = new Store(6, 6);
        Store s2 = new Store(13, 15);
        Store s;
        if (i >= 0 && i <= 10) {
            if (i <= 5) {
                s = s1;
            } else {
                s = s2;
            }
            s.get_delivery(i);
        }
    }
}