package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class Loop_SUU {

	public void m1() {
		Store s = new Store(2, 3);
		for (int i = 0; i < 8; i++){
			s.get_delivery(i);
		}
	  }
}
