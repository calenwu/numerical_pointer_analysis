package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE

public class Conditional_constructer_edge {

	public void m1(int j) {
		Store s = new Store(15, 10);
		int d = j;
		if(j>10) {
			d = 5;
		}
		else if (j > 2) {
			s = new Store(10, 8);
			d = j;
		}
		else {
			d = j;
		}
		s.get_delivery(d);
	  }
}
