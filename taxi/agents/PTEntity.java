package bp.taxi.agents;

import cz.agents.agentpolis.entity.IEnumEntityType;


/*
 * Enum for diversification of agents
 */
public enum PTEntity implements IEnumEntityType {
	
	CAR_PASSENGER("PASSENGER"), 
	TAXI_DRIVER("TAXI_DRIVER");
	

	private final String entityType;

	private PTEntity(String entityType) {
		this.entityType = entityType;
	}

	public String getDescriptionEntityType() {
		return entityType;
	}
}
