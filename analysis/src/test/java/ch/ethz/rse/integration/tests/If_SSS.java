package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class If_SSS {
    public void m1(int i) {
		Store s = new Store(3, 3);
        if(i > 0) {
            s.get_delivery(1);
        }
	  }
}
