package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Swapping_conditional_safe {

	public void m1(int j) {
		Store s;
		Store s1 = new Store(7, 13);
		Store s2 = new Store(4, 8);
		if (j <= 0){
			s = s2;
			s.get_delivery(4);
		} else {
			s = s1;
			s.get_delivery(6);
		}
		s = new Store(11, 16);
		s.get_delivery(7);
	  }
}
