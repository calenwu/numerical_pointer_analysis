package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE SAFE

public class Swapping_fits_unsanfe {

	public void m1(int j) {
		Store s1 = new Store(55, 100);
		Store s2 = new Store(12, 100);
		Store s;

		s = s1;
		s1 = s2;
		s2 = s;

		s1.get_delivery(40);
		s2.get_delivery(40);
	  }
}
