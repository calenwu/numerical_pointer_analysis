package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class if_while_SSS {

	public void m1(int j) {
		Store s = new Store(2, 2);
		while(j<0) {
			j = j + 1;
		}
		while(j>0) {
			j = j - 1;
		}
    	s.get_delivery(j);
    }
}
