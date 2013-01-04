package bp.taxi.log;

import cz.agents.agentpolis.event.IEnumEvent;

/*
 * Enum of all the logged states on the passengers side
 */
public enum ETaxiPassengerState implements IEnumEvent {
	
	PASSENGER_INITIALIZATION("PASSENGER_INITIALIZATION"),
	PASSENGER_BOOKING("PASSENGER_BOOKING"),
	PASSENGER_TRIP_FAIL("PASSENGER_TRIP_FAIL");
	
		
	private final String eventName;
	
	private ETaxiPassengerState(String eventName) {		
		this.eventName = eventName;
	}

	
	public String getEventName() {
		return eventName;
	}

}
