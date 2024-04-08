package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Multi_deliv2 {

	public void m1() {
		Store s = new Store(3, 2);
    	s.get_delivery(-1);
        s.get_delivery(1);
    	s.get_delivery(0);
    }
}
