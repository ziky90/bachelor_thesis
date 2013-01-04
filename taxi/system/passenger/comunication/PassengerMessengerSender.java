package bp.taxi.system.passenger.comunication;

import java.util.LinkedList;

import cz.agents.agentpolis.environment.sensor.APositionSensor;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageData;

/*
 * class that cares of message sending from passenger to the driver
 */
public class PassengerMessengerSender {

	private final MessageProtocol messageProtocol;
	private final APositionSensor positionSensor;
		
	public PassengerMessengerSender(MessageProtocol messageProtocol, APositionSensor positionSensor) {
		super();
		this.messageProtocol = messageProtocol;
		this.positionSensor = positionSensor;
	}
	
	/**
	 * Sends message to taxi driver, with bid to the taxi auction.
	 *  
	 * 
	 * @param passengerId
	 * @param taxiDriverId
	 * @param passengerWantedDestination
	 * @param bidingPrice
	 */
	public void sendPassengerMessage(final String passengerId, final String taxiDriverId, final long passengerWantedDestination, final int bidingPrice){
		
		long currentPassengerPosition = positionSensor.getCurrentPositionByNodeId();
		
		//sending of the bid
		PassengerMessengerData taxiDriverPassengerMessengerData = new PassengerMessengerData(currentPassengerPosition, passengerWantedDestination, bidingPrice);				
		MessageData<PassengerMessengerData> messageData = new MessageData<PassengerMessengerData>(passengerId, ETransportMessageType.PLACED_BID, taxiDriverPassengerMessengerData);
		messageProtocol.sendMessage(taxiDriverId, messageData);
	}

	
	/**
	 * Sends message to taxi driver in which is that the passenger accepted taxi offer 
	 * 
	 * @param passengerId
	 * @param taxiDriverId
	 */
	public void sendPassengerAcceptedOrderMessage(final String passengerId, final String taxiDriverId, final LinkedList<Long> path, final int income){		
		MessageData<PassengerConfirmationMessengerData> messageData = new MessageData<PassengerConfirmationMessengerData>(passengerId, ETransportMessageType.ACCEPTED_ORDER, new PassengerConfirmationMessengerData(path, income));
		messageProtocol.sendMessage(taxiDriverId, messageData);		
	}	
	
}
