package bp.taxi.system.transporter.comunication;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageType;

/*
 * enum for the types of messages that are sent from transporter to the passenger
 */
public enum ETransportPassengerMessageType implements MessageType{
	TAXI_RESPONSE;
}
