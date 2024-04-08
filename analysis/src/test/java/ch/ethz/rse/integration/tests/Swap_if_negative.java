package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE SAFE

public class Swap_if_negative {

	public void m1(int j) {
		int l = 3;
		Store s = new Store(l, 4);
		if(j == 0) return;
		if(j != 0) j = l;
		s.get_delivery(j);
    }
}
