package bp.taxi.system.transporter.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessengerData;
import cz.agents.agentpolis.publictransport.trip.Trip;


/*
 * class for the data format sent in message from the taxi to customer
 */
public class TaxiDriverMessengerData implements MessengerData {

	public final String taxiVehicleId;
	public final Trip tripToPassenger;	
	public final Trip tripWithPassenger;
	public final int actualPrice;
	
	public TaxiDriverMessengerData(String taxiVehicleId, Trip tripToPassenger, Trip tripWithPassenger, int actualPrice) {		
		this.taxiVehicleId = taxiVehicleId;
		this.tripToPassenger = tripToPassenger;
		this.tripWithPassenger = tripWithPassenger;
		this.actualPrice = actualPrice;
	}

	
	
	
	
}
