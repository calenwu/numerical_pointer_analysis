package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class zero_SSS {

	public void m1() {
		Store s = new Store(4, 1);
    	s.get_delivery(0);
	  }
}
