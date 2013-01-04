package bp.taxi.system.passenger.comunication;

import java.util.LinkedList;

import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessengerData;

/*
 * Passenger confirmation message data structure
 */
public class PassengerConfirmationMessengerData implements MessengerData{
	
	public final LinkedList<Long> path;
	public final int income;

	public PassengerConfirmationMessengerData(LinkedList<Long> path, int income){
		this.path=path;
		this.income=income;
	}

	
}
