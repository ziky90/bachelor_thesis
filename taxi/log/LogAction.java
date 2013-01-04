package bp.taxi.log;

import java.util.HashMap;
import java.util.Map;
import cz.agents.agentpolis.entity.AgentPolisEntity;
import cz.agents.agentpolis.environment.AgentPolisEnvironment;
import cz.agents.agentpolis.environment.action.AgentPolisAction;
import cz.agents.agentpolis.event.IEnumEvent;
import cz.agents.agentpolis.event.IEnumEventKey;
import cz.agents.agentpolis.event.impl.CommonEvent;
import cz.agents.agentpolis.eventlogger.AgentPolisEventProtocol;


/*
 * Class that deals with all the logged events through the whole simulation
 */
public class LogAction extends AgentPolisAction<AgentPolisEnvironment, AgentPolisEntity>{
	
	
	private final AgentPolisEventProtocol eventProtocol;
	private final AgentPolisEntity relatedEntity;
	
	
	
	public LogAction(AgentPolisEnvironment environment, AgentPolisEntity relatedEntity) {
		super(environment, relatedEntity);		
		this.relatedEntity = relatedEntity;
		this.eventProtocol = environment.getEventProtocol();
		
		
	}

	/*
	 * method for logging of the universal Event
	 */
	private void logCommonEvent(IEnumEvent enumEvent, Map<IEnumEventKey,Object> eventData){		
		CommonEvent commonEvent = new CommonEvent(eventData,eventProcessor.getCurrentTime(), enumEvent, relatedEntity.getId());
		eventProtocol.sendEvent(commonEvent);
	}

	
	/*
	 * Method for logging of the specific passenger started live event
	 */
	public void logPassengerStartedLive(String passengerId, long departurePossition, long destinationPosition, int maximalPrice, byte necessity){
		Map<IEnumEventKey,Object> eventData = new HashMap<IEnumEventKey,Object>();
		eventData.put(ETaxiPassengerKey.ID, passengerId);
		eventData.put(ETaxiPassengerKey.DEPARTURE_PLACE, departurePossition);
		eventData.put(ETaxiPassengerKey.DESTINATION_PLACE, destinationPosition);
		eventData.put(ETaxiPassengerKey.MAXIMAL_PRICE, maximalPrice);
		eventData.put(ETaxiPassengerKey.NECESSITY, necessity);
		logCommonEvent(ETaxiPassengerState.PASSENGER_INITIALIZATION, eventData);		
	}
	
	/*
	 * Method for logging of the specific passenger found transporter event
	 */
	public void logPassengerFoundTransporter(String passengerId, int price, int journeyLength, int maximalPrice, byte necessity, int bidingPrice, int numberOfBids){		
		Map<IEnumEventKey,Object> eventData = new HashMap<IEnumEventKey,Object>();
		eventData.put(ETaxiPassengerKey.ID, passengerId);
		eventData.put(ETaxiPassengerKey.PRICE, price);
		eventData.put(ETaxiPassengerKey.JOURNEY_LENGTH, journeyLength);
		eventData.put(ETaxiPassengerKey.MAXIMAL_PRICE, maximalPrice);
		eventData.put(ETaxiPassengerKey.NECESSITY, necessity);
		eventData.put(ETaxiPassengerKey.BIDING_PRICE, bidingPrice);
		eventData.put(ETaxiPassengerKey.NUMBER_OF_BIDS, numberOfBids);
		logCommonEvent(ETaxiPassengerState.PASSENGER_BOOKING, eventData);
		
	}
	
	/*
	 * Method for logging of the specific passenger fail event
	 */
	public void logPassengerTripFail(String passengerId, int maximalPrice, byte necessity){		
		Map<IEnumEventKey,Object> eventData = new HashMap<IEnumEventKey,Object>();
		eventData.put(ETaxiPassengerKey.ID, passengerId);
		eventData.put(ETaxiPassengerKey.MAXIMAL_PRICE, maximalPrice);
		eventData.put(ETaxiPassengerKey.NECESSITY, necessity);
		//eventData.put(ETaxiPassengerKey.BIDING_PRICE, bidingPrice);
		logCommonEvent(ETaxiPassengerState.PASSENGER_TRIP_FAIL, eventData);
		
	}
	
	
	/*
	 * Method for logging of the specific transporter was booked event
	 */
	public void logTransporterBooked(String transporterId, int pricePerRide, int cumulativeIncome, int length, int milage){
		Map<IEnumEventKey,Object> eventData = new HashMap<IEnumEventKey,Object>();
		eventData.put(ETaxiDriverKey.ID, transporterId);
		eventData.put(ETaxiDriverKey.PRICE, pricePerRide);
		eventData.put(ETaxiDriverKey.CUMULATIVE_INCOME, cumulativeIncome);
		eventData.put(ETaxiDriverKey.TRIP_LENGTH, length);
		eventData.put(ETaxiDriverKey.MILAGE, milage);
		logCommonEvent(ETaxiDriverState.BOOKED, eventData);
	}
}
