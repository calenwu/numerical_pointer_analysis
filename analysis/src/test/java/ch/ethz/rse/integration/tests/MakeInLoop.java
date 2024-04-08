package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class MakeInLoop {

	public void foo (int i) {
		Store s;
		for (; i >= 0; i--) {
            s = new Store(4, 6);
            s.get_delivery(2);
        }
	}
}