package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Multi_Deliv {

	public void m1() {
		Store s = new Store(4, 6);
    	s.get_delivery(1);
    	s.get_delivery(4);
        s.get_delivery(1);
    }
}
