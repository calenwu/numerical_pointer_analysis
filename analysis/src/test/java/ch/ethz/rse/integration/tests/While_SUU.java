package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class While_SUU {
    public void m1(int i) {
		Store s = new Store(2, 4);
        if(i > 0) return;
        else s.get_delivery(1);
	  }
}
