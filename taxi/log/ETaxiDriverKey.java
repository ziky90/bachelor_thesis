package bp.taxi.log;

import cz.agents.agentpolis.event.IEnumEventKey;

/*
 * Enum of all the logged elements on the taxi drivers side
 */
public enum ETaxiDriverKey implements IEnumEventKey {
	ID("ID"),
	PRICE("PRICE"),
	CUMULATIVE_INCOME("CUMULATIVE_INCOME"),
	TRIP_LENGTH("TRIP_LENGTH"),
	MILAGE("MILAGE");
	
	

	private final String keyName;

	private ETaxiDriverKey(String keyName) {

		this.keyName = keyName;
	}

	public String keyName() {
		return keyName;
	}
}
