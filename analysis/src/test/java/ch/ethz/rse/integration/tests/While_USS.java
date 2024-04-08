package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class While_USS {

	public void m1(int j) {
		Store s = new Store(3, 5);
		while(j>=0) {
			j = j - 1;
		}
    	s.get_delivery(j);
    }
}
