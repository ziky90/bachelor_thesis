package bp.taxi.system.passenger.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessengerData;

/*
 * initial bidding message data structure
 */
public class PassengerMessengerData implements MessengerData {

	public final long currentPassengerPosition;
	public final long passengerWantedDestination;
	public final int bidingPrice;
	
	public PassengerMessengerData(long currentPassengerPosition, long passengerWantedDestination, int bidingPrice) {	
		this.currentPassengerPosition = currentPassengerPosition;
		this.passengerWantedDestination = passengerWantedDestination;
		this.bidingPrice=bidingPrice;
	}
	
	
	
}
