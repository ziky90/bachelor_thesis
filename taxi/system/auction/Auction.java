package bp.taxi.system.auction;

import java.util.PriorityQueue;
import java.util.Queue;

/*
 * class for all the auction logic
 */
public class Auction{
	
	
	/*
	 * number of customers in this particular auction
	 */
	public int customersNumber;
	
	/*
	 * boolean if this auction is running, 
	 * maybe not needed at all
	 */
	public boolean running;
	
	/*
	 * starting price calculated and delivered by the transporter
	 */
	public int minimalPrice;
	
	/*
	 * maximum price given by the law or some internal regulations
	 */
	public int maximumPrice;
	
	/*
	 * actual price per unit 
	 */
	public int actualPrice;
	
	/*
	 * queue in which will be held all the requests
	 */
	private Queue<Integer> bidQueue = new PriorityQueue<Integer>(11, new AuctionIntegerComparator());
	
	/*
	 * constructor
	 */
	public Auction(int minimalPrice, int maximumPrice){
		this.minimalPrice=minimalPrice;
		this.maximumPrice=maximumPrice;
		this.running=true;
	}
	
	
		
	/*
	 * method for placing the bid
	 * @param int bid
	 */
	public void placeBid(int bid){ 
		if(bid>=minimalPrice){
			if(bid<=maximumPrice){
				bidQueue.add(bid);
				customersNumber++;
			}
			else{
				bidQueue.add(maximumPrice);
				customersNumber++;
			}
		}else{
			bidQueue.add(minimalPrice);
			customersNumber++;
		}		
	}
	
	/*
	 * method for refreshing the auction eg. deleting all the bids after the certain time
	 */
	public void refreshAuction(){
		bidQueue.clear();
		customersNumber=0;
	}

	
	/*
	 * method for finding the winning price of the auction
	 * 
	 */
	public int evaluate(){
		if (bidQueue.isEmpty()){
			return minimalPrice;
		}
		else if (bidQueue.size()==1){
			return bidQueue.peek();
		}
		else{
			for(int i=0;i<1;i++){					
				bidQueue.poll();
			}
			return bidQueue.peek();
		}
						
	}
		
}
