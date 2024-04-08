package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class While_wide_SUU {

	public void m1() {
		Store s = new Store(15, 100);
		int j = 0;
		while (j < 15){
			j = j + 1;
		}
		s.get_delivery(j);
	  }
}
