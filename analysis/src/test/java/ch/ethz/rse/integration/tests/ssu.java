package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE
public class ssu {
	public void m1(int j) {
	    Store s = new Store(3, 12);
	    for (int i = 0; i < 3; i++) {
	        s.get_delivery(i);
	    }

	}
}
