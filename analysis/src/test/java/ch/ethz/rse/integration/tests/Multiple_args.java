package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class Multiple_args {

	public void m1(int i, int j) {

		Store s = new Store(2, 3);
        s.get_delivery(i * j);
	  }
}
