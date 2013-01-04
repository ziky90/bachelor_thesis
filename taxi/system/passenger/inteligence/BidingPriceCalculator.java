package bp.taxi.system.passenger.inteligence;

/*
 * class that cares of intelligent calculation of the bidding price
 */
public class BidingPriceCalculator {

	private final int maximalPrice;
	private final byte necessity; // number that is from 1 to 3 and shows how important is for passenger to go
				 

	/*
	 * constructor
	 * 
	 * @param int maximalPrice
	 * 
	 * @param byte necessity
	 */
	public BidingPriceCalculator(int maximalPrice, byte necessity) {
		super();
		this.maximalPrice = maximalPrice;
		this.necessity = necessity;
	}

	/*
	 * method to calculate biding price
	 * 
	 * @param int lastBidingPrice
	 * 
	 * @param int numberOfTransporters
	 * 
	 * @param int lastWiningPrice
	 */
	public int calculateBidingPrice(int lastBidingPrice, int numberofIterations) {
		if (lastBidingPrice != -1) {
			int price;
			switch (necessity) {
				case 1:
					return lastBidingPrice - ((lastBidingPrice * 5 / 100)); // -5% rule in case if there is low need to go
				case 2:
					price=lastBidingPrice + ((lastBidingPrice * 5 / 100)*numberofIterations);
					if (price < maximalPrice && price!=lastBidingPrice) {
						return price;
					} else if(price==lastBidingPrice){
						return lastBidingPrice+1;
					}else{
						return maximalPrice;
					}
				case 3:
					price=lastBidingPrice + ((lastBidingPrice * 20 / 100)*numberofIterations);
					if (price < maximalPrice && price!=lastBidingPrice) {
						return price;
					} else if(lastBidingPrice==price){
						return lastBidingPrice+2;
					} else{
						return maximalPrice;
					}
								
			}
			return lastBidingPrice;			
		}else{
			switch (necessity) {
			case 1:
				return (maximalPrice/2)+1;
			case 2:
				return (maximalPrice*7/10)+1;				
			case 3:
				return (maximalPrice*9/10)+1;							
			}
			return lastBidingPrice*-1;
		}		
	}

}
