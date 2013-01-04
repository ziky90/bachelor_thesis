package bp.taxi.log;

import cz.agents.agentpolis.event.IEnumEvent;


/*
 * Enum of all the loged states on the side of the taxi driver
 */
public enum ETaxiDriverState implements IEnumEvent {

	BOOKED("BOOKED");

	private final String eventName;
	
	
	private ETaxiDriverState(String eventName) {
		this.eventName = eventName;
	}


	public String getEventName() {
		return eventName;
	}
}
