package ch.ethz.rse.integration.tests;
import ch.ethz.rse.Store;

// expected results:
// NON_NEGATIVE UNSAFE
// FITS_IN_TROLLEY UNSAFE
// FITS_IN_RESERVE UNSAFE

public class If3 {

	public void m1(int j) {
        
        int i;
		Store s = new Store(4, 4);
		if (j > 0) i = 5;
        else i = -5; 
        s.get_delivery(i);
	  }
}
