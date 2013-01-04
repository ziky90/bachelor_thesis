package bp.taxi.log;

import cz.agents.agentpolis.event.IEnumEventKey;


/*
 * Enum of all the logged elements on the passengers side
 */
public enum ETaxiPassengerKey implements IEnumEventKey{

	ID("ID"),
	DEPARTURE_PLACE("PLACE"),
	DESTINATION_PLACE("DESTINATION_PLACE"),
	MAXIMAL_PRICE("MAXIMAL_PRICE"),
	NECESSITY("NECESSITY"),
	PRICE("PRICE"),
	JOURNEY_LENGTH("JOURNEY_LENGTH"),
	BIDING_PRICE("BIDING_PRICE"),
	NUMBER_OF_BIDS("NUMBER_OF_BIDS");
	
	private final String keyName;
	
	private ETaxiPassengerKey(String keyName) {
		this.keyName = keyName;
	}

	public String keyName() {
		return keyName;
	}

}
