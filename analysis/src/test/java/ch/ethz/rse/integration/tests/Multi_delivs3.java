package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Multi_delivs3 {
	public void m1(int j) {
		Store s = new Store(4, 7);
		j = 4;
    	s.get_delivery(j);
		j = -1;
        s.get_delivery(j);
		j = 2;
    	s.get_delivery(j);
    }
}
