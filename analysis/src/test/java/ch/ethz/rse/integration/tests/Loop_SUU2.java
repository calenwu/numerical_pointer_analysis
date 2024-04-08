package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class Loop_SUU2 {

	public void m1() {
		int j = 0;
		Store s = new Store(3, 4);
		while (j < 5){
			j = j + 1;
			s.get_delivery(j);
		}
	}
}
