package bp.taxi.system.passenger;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.commons.lang.NotImplementedException;
import org.joda.time.Duration;

import bp.taxi.agents.PTEntity;
import bp.taxi.system.SystemInformations;
import bp.taxi.system.passenger.comunication.PassengerMessengerSender;
import bp.taxi.system.passenger.inteligence.BidingPriceCalculator;
import bp.taxi.system.passenger.inteligence.HumanForTaxiDecisionSimulator;
import bp.taxi.system.transporter.comunication.ETransportPassengerMessageType;
import bp.taxi.system.transporter.comunication.TransportDriverMessengerData;
import cz.agents.agentpolis.activity.ILifeCycleActivity;
import cz.agents.agentpolis.description.Description;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.environment.sensor.TimeSensor;
import cz.agents.agentpolis.environment.sensor.agent.AgentPositionSensor;
import cz.agents.agentpolis.environment.sensor.callback.TimeCallback;
import cz.agents.agentpolis.publictransport.activity.movement.PassengerActivity;
import cz.agents.agentpolis.publictransport.activity.movement.callback.PassengerActivityCallback;
import bp.taxi.log.LogAction;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageReceiverCallback;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessegeReceiverProccesor;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageData;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageType;
import cz.agents.agentpolis.publictransport.environment.PublicTransportEnvironment;
import cz.agents.agentpolis.publictransport.trip.Trip;



/*
 * class that stands for the passenger life cycle
 */
public class PassengerLifeCycleActivity implements ILifeCycleActivity, TimeCallback, PassengerActivityCallback<Trip>, MessageReceiverCallback {

	private final TimeSensor timeSensor;	//time sensor for the starting of the activity
	private final AgentPositionSensor positionSensor;
	private final TimeSensor timeForReply;	//time sensor for the replying time
	private final Decider decider;
	private final Agent passenger;
	private long passengerWantedDestination;
	private final PublicTransportEnvironment enviroment;
	private final Duration startLife;
	private final PassengerMessengerSender taxiPassengerMessengerSender;
	private final Map<MessageType, MessegeReceiverProccesor<?>> messegeReceiverProccesors = new HashMap<MessageType, MessegeReceiverProccesor<?>>();
	private int bidingPrice=-1;	//passengers biding price that will be dynamically calculated
	private final int maximalPrice;  		
	private final byte necessity;
	private final SystemInformations systemInformations;
	private final Trip trip;	//trip that is wanted to travel by the passenger
	private SortedMap<Integer, Object[]> offers=new TreeMap<Integer, Object[]>();
	private final BidingPriceCalculator bidingPriceCalculator;
	private final HumanForTaxiDecisionSimulator humanDecisionSimulator;
	private int numberOfBids;
	//------------------ LOG -----------------------------
	private final LogAction logAction;
	//------------------ LOG -----------------------------
	/*
	 * constructor
	 */
	public PassengerLifeCycleActivity(Agent passenger, TimeSensor timeSensor, long passengerWantedDestination, final PassengerMessengerSender taxiPassengerMessengerSender, Duration startLife, PublicTransportEnvironment enviroment, TimeSensor timeFroReply, int maximalPrice, byte necessity, AgentPositionSensor positionSensor, Trip trip) {
		
		this.taxiPassengerMessengerSender = taxiPassengerMessengerSender;
		this.passengerWantedDestination = passengerWantedDestination;
		this.passenger = passenger;
		this.positionSensor=positionSensor;
		this.timeSensor = timeSensor;
		this.startLife = startLife;	
		this.enviroment=enviroment;
		this.timeForReply=timeFroReply; 
		this.decider= new Decider(this);
		this.systemInformations=SystemInformations.getInstance();
		this.maximalPrice=maximalPrice;
		this.necessity=necessity;
		this.trip=trip;
		//------------------ LOG -----------------------------
		this.logAction = passenger.addAction(LogAction.class);
		//------------------ LOG -----------------------------
		bidingPriceCalculator=new BidingPriceCalculator(maximalPrice, necessity);
		humanDecisionSimulator=new HumanForTaxiDecisionSimulator(necessity, maximalPrice);
		
		
	}

	
	/*
	 * method for description required by the environment
	 */
	public Description getDescription() {
		return new Description();
	}

	/*
	 * initial born method
	 */
	public void born() {

		/*
		 * can be uncommented but for my experiments here are just duplicate irrelevant data
		 */
		//------------------ LOG -----------------------------
		//logAction.logPassengerStartedLive(passenger.getId(), positionSensor.getCurrentPositionByNodeId(), passengerWantedDestination, maximalPrice, necessity);
		//------------------ LOG -----------------------------
		messegeReceiverProccesors.put(ETransportPassengerMessageType.TAXI_RESPONSE, new PassengerMessegeReceiverProccesor());
		timeSensor.setAlarm(this, startLife);				
	}
	
	/*
	 * search for a ride after the callback
	 * 
	 */
	public void timeCallback() {
		
		//simulating of the human made decision if to take the taxi or wait or give it up
		if(humanDecisionSimulator.stopSearching(numberOfBids) || numberOfBids>50){
			//------------------ LOG -----------------------------
			logAction.logPassengerTripFail(passenger.getId(), maximalPrice, necessity);
			//------------------ LOG -----------------------------
			this.doneFullTrip();
		}else{
			Collection<String> taxis=enviroment.getAgentStorage().getEntityIdsBaseOnType(PTEntity.TAXI_DRIVER);
			bidingPrice=bidingPriceCalculator.calculateBidingPrice(bidingPrice, numberOfBids);
			numberOfBids++;
			for(String taxi:taxis){			
				taxiPassengerMessengerSender.sendPassengerMessage(passenger.getId(), taxi, passengerWantedDestination, bidingPrice);			
			}
			timeForReply.setAlarm(this.decider, Duration.standardMinutes(5));
		}
	}
	
	/*
	 * method for after the trip action
	 */
	public void doneFullTrip() {
		
	}
	
	public void donePartTrip(Trip partNotDoneTrip) {
		throw new NotImplementedException();
	}

	public void tripFail(Trip failedTrip) {
		
	}

	/*
	 * method that handles all the received messages
	 */
	public void receivedMessage(MessageData<?> messageData) {		
		MessegeReceiverProccesor messegeReceiverProccesor = messegeReceiverProccesors.get(messageData.messageType);
		messegeReceiverProccesor.proccesMassege(messageData.messageSender, messageData.messengerData);		

	}
	
	
	/*
	 * making the decision which taxi to take based on the cheapest per unit price
	 */
	public void decideForTransport(){
		boolean transportFound=false;
		while(offers.entrySet().iterator().hasNext()){									
			String driverId=(String) offers.entrySet().iterator().next().getValue()[0];			
			if(systemInformations.checkAvailabilityId(driverId) && humanDecisionSimulator.decideForTransport(bidingPrice, offers.entrySet().iterator().next().getKey(), trip.numTrip())){
				systemInformations.removeTransporterFromTheSystem(driverId);
				String vehicleId=(String) offers.entrySet().iterator().next().getValue()[1];
				Trip pickupTrip= (Trip) offers.entrySet().iterator().next().getValue()[2];
				int finalPrice=offers.entrySet().iterator().next().getKey();
				PassengerActivity passengerActivity = new PassengerActivity(passenger, trip, this);		
				passengerActivity.usingVehicleAsPassenger(vehicleId);
				LinkedList<Long> path;
				if(pickupTrip!=null){
					Trip tripToPassenger=pickupTrip.clone();
					path = new LinkedList<Long>(copy(tripToPassenger));	
					Trip tripByTaxi=trip.clone();
					tripByTaxi.removeCurrentNode();
					path.addAll(copy(tripByTaxi));
				}else{
					Trip tripByTaxi=trip.clone();
					path = new LinkedList<Long>(copy(tripByTaxi));
				}				
				taxiPassengerMessengerSender.sendPassengerAcceptedOrderMessage(passenger.getId(), driverId, path, finalPrice);
				transportFound=true;
				
				//------------------ LOG -----------------------------
				if(pickupTrip!=null){
					logAction.logPassengerFoundTransporter(passenger.getId(), finalPrice, trip.numTrip()+pickupTrip.numTrip(), maximalPrice, necessity, bidingPrice, numberOfBids);					
				}else{
					logAction.logPassengerFoundTransporter(passenger.getId(), finalPrice, trip.numTrip(), maximalPrice, necessity, bidingPrice, numberOfBids);	
				}	
				//------------------ LOG -----------------------------
				
				break;			
			}
			offers.remove(offers.entrySet().iterator().next().getKey());
		}
		/*
		 * waiting cycle in case that the suitable taxi was not found
		 */
		if(!transportFound){
			timeSensor.setAlarm(this, Duration.standardMinutes(5));
		}	
		
	}
	
	/*
	 * method for creation of the trip
	 * @param Trip trip
	 */
	private LinkedList<Long> copy(Trip trip){
		LinkedList<Long> path = new LinkedList<Long>();
		while(trip.hasNextNode()){
			path.add(trip.getNextNodeAndRemove());
		}
		return path;
	}

	
	/*
	 * inner private class that cares of processing of the TransportDriverMessengerData
	 */
	private class PassengerMessegeReceiverProccesor implements	MessegeReceiverProccesor<TransportDriverMessengerData> {

		
		public PassengerMessegeReceiverProccesor() {
			super();			
		}

		public void proccesMassege(String taxiDriverId,	TransportDriverMessengerData messengerData) {
			
			Object[] dataArray= {taxiDriverId, messengerData.taxiVehicleId, messengerData.tripToPassenger};
			
			/*
			 * calculation of the price for the full trip, because of the efficiency and ecology support
			 */
			if(messengerData.actualPrice!=-1){
				if(messengerData.actualPrice<=maximalPrice){
					int completePrice;	
					if(messengerData.tripToPassenger!=null){
						completePrice=(messengerData.tripToPassenger.numTrip()+trip.numTrip())*messengerData.actualPrice;
					}else{
						completePrice=(trip.numTrip())*messengerData.actualPrice;
					}
					offers.put(completePrice, dataArray);
				}
												
			}

		}

	}

}
