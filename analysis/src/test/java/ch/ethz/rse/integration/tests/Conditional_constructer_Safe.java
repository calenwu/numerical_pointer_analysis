package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Conditional_constructer_Safe {

	public void m1() {
		Store s = new Store(10, 10);
		int d = 2;
		if(d>2) {
			s = new Store(2, 12);
		}
		else if (d == 0) {
			s = new Store(3, 8);
		}
		else {
			d = 6;
		}
		s.get_delivery(d);
	  }
}
