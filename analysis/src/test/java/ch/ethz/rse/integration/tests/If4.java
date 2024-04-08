package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class If4 {

	public void m1(int j) {
		int i = 2;
		int k = 4;
		Store s = new Store(2, 5);
		if (j > 4) j = i;
		else j = k;
		s.get_delivery(j);
		s.get_delivery(k);
    }
}
