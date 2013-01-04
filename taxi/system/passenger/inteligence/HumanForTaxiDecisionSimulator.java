package bp.taxi.system.passenger.inteligence;

/*
 * class for simulation of human decision for the transporter
 */
public class HumanForTaxiDecisionSimulator {

	private final byte necessity;
	private final int maximalPrice;	
	
	
	public HumanForTaxiDecisionSimulator(byte necessity, int maximalPrice) {
		super();
		this.necessity = necessity;
		this.maximalPrice = maximalPrice;		
	}
	
	/*
	 * method for decision making if to take taxi or not
	 */
	public boolean decideForTransport(int lastBid, int pricePerRide, int length){
		if(necessity==3){
			return true;
		}else if(necessity==2){			
			if((pricePerRide/length<maximalPrice-(maximalPrice*5/100)) || (lastBid<maximalPrice-(maximalPrice*10/100))){
				return true;
			}else{
				return false;
			}
		}else{
			if((pricePerRide/length<maximalPrice-(maximalPrice*15/100)) || (lastBid<maximalPrice-(maximalPrice*20/100))){
				return true;
			}else{
				return false;
			}
		}				
	}
	
	/*
	 * method that simulates passengers decision if to try to search for the taxi again
	 */
	public boolean stopSearching(int counter){
		switch (necessity) {
			case 1:{
				if(counter<5){
					return false;
				}else{
					return true;
				}
			}case 2:{
				if(counter<15){
					return false;
				}else{
					return true;
				}
			}			
		}
		return false;
	}
	
}
