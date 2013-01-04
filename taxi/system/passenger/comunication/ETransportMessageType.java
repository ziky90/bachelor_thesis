package bp.taxi.system.passenger.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageType;

/*
 * enum of messages types from the passenger
 */
public enum ETransportMessageType implements MessageType{
	PLACED_BID,	
	ACCEPTED_ORDER;
}
