package bp.taxi.system.transporter.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessengerData;
import cz.agents.agentpolis.publictransport.trip.Trip;


/*
 * class for the data format sent in message from the taxi to customer
 */
public class TransportDriverMessengerData implements MessengerData {

	public final String taxiVehicleId;
	public final Trip tripToPassenger;	
		public final int actualPrice;
	
	public TransportDriverMessengerData(String taxiVehicleId, Trip tripToPassenger, int actualPrice) {		
		this.taxiVehicleId = taxiVehicleId;
		this.tripToPassenger = tripToPassenger;
		this.actualPrice = actualPrice;
	}

	
	
	
	
}
