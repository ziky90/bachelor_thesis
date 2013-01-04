package bp.taxi.system.transporter.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageType;

/*
 * enum of all the message types from the side of the taxi driver
 */
public enum ETaxiMessageType implements MessageType{
	PLACE_BID,		
	REQUEST_TAXI,
	ACCEPTED_ORDER;
}
