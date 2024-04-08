package ch.ethz.rse.integration.tests;

import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class If2 {

	public void m1(int j) {
		Store s = new Store(2, 3);
		if (j < -3){
            j = 0;
        } else if (j < 0){
            j = j + 3;
        } else {
            j = j * 2;
        }
        s.get_delivery(j);
	  }
}
