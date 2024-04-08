package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class Conditional_constructor_SUU {

	public void m1(int j) {
		Store s;
		if (j != 0){
			s = new Store(5, 11);
		}
		else if (j == 5){
			s = new Store(16, 43);
		}
		else {
			s = new Store(110, 1000);
		}
		s.get_delivery(50);
	  }
}	
