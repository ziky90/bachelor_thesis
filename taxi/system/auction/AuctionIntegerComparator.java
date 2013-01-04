package bp.taxi.system.auction;

import java.util.Comparator;

/*
 * Implementation of the comparator for the auction evaluation
 */
public class AuctionIntegerComparator implements Comparator<Integer> {

	/*
	 * Comparison of values is exactly opposite than in the implicit comparator
	 */
	public int compare(Integer o1, Integer o2) {
		if(o1<=o2){
			return 1;
		}else{
			return -1;
		}		
	}

}
