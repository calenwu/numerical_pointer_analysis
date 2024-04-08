package ch.ethz.rse.integration.tests;

// expected results:
// NON_NEGATIVE SAFE
// FITS_IN_TROLLEY SAFE
// FITS_IN_RESERVE UNSAFE
import ch.ethz.rse.Store;
public class Unsound {
  public static void loop() {
    Store s = new Store(1,2);

    for(int i = 0; i < 2; i++){
        Store a = new Store(1, 1);

        if(i == 0){
            s = a;
        }
        s.get_delivery(1);
    }
  }
}
