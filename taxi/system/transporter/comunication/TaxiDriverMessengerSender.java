package bp.taxi.system.transporter.comunication;


import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageData;
import cz.agents.agentpolis.publictransport.trip.Trip;

/*
 * messenger sender core taken from public transport
 * 
 */
public class TaxiDriverMessengerSender {

	private final MessageProtocol messageProtocol;

	public TaxiDriverMessengerSender(MessageProtocol messageProtocol) {	
		this.messageProtocol = messageProtocol;
	}
	
	
	/**
	 * Sends taxi driver response message to passenger, which information about trip and vehicle 
	 * 
	 * @param taxiDriverId
	 * @param passengerId
	 * @param taxiVehicleId
	 * @param tripToPassenger
	 * @param tripWithPassenger
	 */
	public void sendPassengerMessage(String taxiDriverId,String passengerId,String taxiVehicleId,Trip tripToPassenger, Trip tripWithPassenger, int actualPrice){		
		MessageData<TaxiDriverMessengerData> messageData = new MessageData<TaxiDriverMessengerData>(taxiDriverId,ETaxiPassengerMessageType.TAXI_RESPONSE,new TaxiDriverMessengerData(taxiVehicleId, tripToPassenger, tripWithPassenger, actualPrice));
		messageProtocol.sendMessage(passengerId,messageData);
	}
	
	
	
	
}
