package bp.taxi.system.passenger.comunication;

import bp.taxi.system.transporter.comunication.ETaxiMessageType;
import cz.agents.agentpolis.environment.sensor.APositionSensor;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.EmptyMessengerData;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageData;

/*
 * class that deals with sending messages from the Taxi driver to the passenger
 */
public class TaxiPassengerMessengerSender {

	private final MessageProtocol messageProtocol;
	private final APositionSensor positionSensor;
		
	public TaxiPassengerMessengerSender(MessageProtocol messageProtocol, APositionSensor positionSensor) {
		super();
		this.messageProtocol = messageProtocol;
		this.positionSensor = positionSensor;
	}
	
	/**
	 * Sends message to taxi driver, which request taxi.
	 *  
	 * 
	 * @param passengerId
	 * @param taxiDriverId
	 * @param passengerWantedDestination
	 * @param bidingPrice
	 */
	public void sendPassengerMessage(final String passengerId, final String taxiDriverId, final long passengerWantedDestination, final int bidingPrice){
		
		long currentPassengerPosition = positionSensor.getCurrentPositionByNodeId();
		
		/*
		 * sending of the bid
		 */
		TaxiDriverPassengerMessengerData taxiDriverPassengerMessengerData = new TaxiDriverPassengerMessengerData(currentPassengerPosition, passengerWantedDestination, bidingPrice);
				
		MessageData<TaxiDriverPassengerMessengerData> messageData = new MessageData<TaxiDriverPassengerMessengerData>(passengerId, ETaxiMessageType.PLACE_BID, taxiDriverPassengerMessengerData);
		messageProtocol.sendMessage(taxiDriverId, messageData);
	}

	
	/**
	 * Sends message to taxi driver, that passenger accepted taxi offer 
	 * 
	 * @param passengerId
	 * @param taxiDriverId
	 */
	public void sendPassengerAcceptedOrderMessage(final String passengerId, final String taxiDriverId){
		MessageData<EmptyMessengerData> messageData = new MessageData<EmptyMessengerData>(passengerId, ETaxiMessageType.ACCEPTED_ORDER, new EmptyMessengerData());
		messageProtocol.sendMessage(taxiDriverId,messageData);
	}
}
