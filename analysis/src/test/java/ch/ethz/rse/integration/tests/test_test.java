package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class test_test {

	public void m3(int j) {
		Store s = new Store(1, 2);
		for (int i = 0; i < 2; i++) {
			s.get_delivery(i);
		}
		if (-1 < j && j <= 3) {
			j = j + 3;
			s.get_delivery(j);
		}
	}
}