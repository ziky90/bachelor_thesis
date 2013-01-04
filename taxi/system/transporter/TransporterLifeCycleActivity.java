package bp.taxi.system.transporter;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.Duration;

import bp.taxi.log.LogAction;
import bp.taxi.system.SystemInformations;
import bp.taxi.system.auction.Auction;
import bp.taxi.system.passenger.comunication.ETransportMessageType;
import bp.taxi.system.passenger.comunication.PassengerConfirmationMessengerData;
import bp.taxi.system.passenger.comunication.PassengerMessengerData;
import bp.taxi.system.transporter.comunication.TransportDriverMessengerSender;


import cz.agents.agentpolis.activity.ILifeCycleActivity;
import cz.agents.agentpolis.description.Description;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.entity.vehicle.Vehicle;
import cz.agents.agentpolis.environment.sensor.TimeSensor;
import cz.agents.agentpolis.environment.sensor.agent.AgentPositionSensor;
import cz.agents.agentpolis.environment.sensor.callback.TimeCallback;
import cz.agents.agentpolis.publictransport.activity.movement.DrivingActivity;
import cz.agents.agentpolis.publictransport.activity.movement.callback.DrivingActivityCallback;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageReceiverCallback;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessegeReceiverProccesor;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageData;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.data.MessageType;
import cz.agents.agentpolis.publictransport.planner.TripPlanner;
import cz.agents.agentpolis.publictransport.planner.TripPlannerException;
import cz.agents.agentpolis.publictransport.trip.Trip;
import cz.agents.agentpolis.publictransport.trip.Trips;


/*
 * class that stands for whole life cycle of the transporter
 */
public class TransporterLifeCycleActivity implements ILifeCycleActivity,  DrivingActivityCallback, MessageReceiverCallback, TimeCallback{

	//variable integer that sets the length of the given auction
	private final int auctionLength;
	
	private final TimeSensor timeSensor;
	/*
	 * agent initialization
	 */
	private final Agent transporter;
	
	/*
	 * vehicle initialization
	 */
	private final Vehicle vehicle;
	private final TripPlanner<Trip> tripPlanner;
	private final AgentPositionSensor positionSensor;
	private final TransportDriverMessengerSender taxiDriverMessengerSender;
	private final Map<MessageType, MessegeReceiverProccesor<?>> messegeReceiverProccesors = new HashMap<MessageType, MessegeReceiverProccesor<?>>();
	private Trip orderedTrip = null;
	private final DrivingActivity drivingActivity;
	private final SystemInformations systemInformations;
	private int winingPrice;
	private int cumulativeIncome;
	private int milage;
	//------------------ LOG -----------------------------
	private final LogAction logAction;
	//------------------ LOG -----------------------------
	
	/*
	 * creation of the auction logic class
	 */
	private final Auction auction; 

	/*
	 * constructor
	 */
	public TransporterLifeCycleActivity(Agent transporter,Vehicle vehicle, DrivingActivity drivingActivity, TripPlanner<Trip> tripPlanner,AgentPositionSensor positionSensor,
			TransportDriverMessengerSender taxiDriverMessengerSender, TimeSensor timeSensor, int minimalPrice, int maximalPrice, int auctionLength) {		
		this.transporter = transporter;
		this.vehicle = vehicle;
		this.tripPlanner = tripPlanner;
		this.positionSensor = positionSensor;
		this.taxiDriverMessengerSender = taxiDriverMessengerSender;
		this.drivingActivity = drivingActivity;
		this.systemInformations=SystemInformations.getInstance();
		this.timeSensor=timeSensor;
		this.auctionLength=auctionLength;
		auction= new Auction(minimalPrice, maximalPrice);
		//------------------ LOG -----------------------------
		this.logAction = transporter.addAction(LogAction.class);
		//------------------ LOG -----------------------------
	}
	
	
	/*
	 * 
	 * just description getter
	 */
	public Description getDescription() {
		return new Description();
	}

	
	/*
	 * 
	 * initial born method
	 * creation of message receivers for placed bid and aceptation of the order
	 */
	public void born() {
		systemInformations.addTransporterToTheSystem(transporter.getId());
		
		//set the alarm for the auction evaluation
		timeSensor.setAlarm(this, Duration.standardMinutes(auctionLength));
		
		//incoming messages enabling
		messegeReceiverProccesors.put(ETransportMessageType.PLACED_BID, new TransporterMessegeReceiverProccesor());				
		messegeReceiverProccesors.put(ETransportMessageType.ACCEPTED_ORDER, new AcceptedPathMessegeReceiverProccesor(this));				
	}
	
	/*
	 * 
	 * method dealing with received message event
	 * 
	 */ 
	public void receivedMessage(MessageData<?> messageData) {		
		MessegeReceiverProccesor messegeReceiverProccesor = messegeReceiverProccesors.get(messageData.messageType);		
		messegeReceiverProccesor.proccesMassege(messageData.messageSender, messageData.messengerData);		
	}

	/*
	 * 
	 * method that re-enables the taxi driver for another job
	 * 
	 */
	public void finishedDriving() {	
		systemInformations.addTransporterToTheSystem(transporter.getId());
	}
	
	/*
	 * 
	 * method that evaluates the auction associated to the transporter and sets the time when the auction should be evaluated again
	 */
	public void timeCallback() {
		auction.actualPrice=auction.evaluate();
		auction.refreshAuction();
		timeSensor.setAlarm(this, Duration.standardMinutes(auctionLength));
	}
	
	/*
	 * inner class for processing of the initial biding message
	 */
	private class TransporterMessegeReceiverProccesor implements MessegeReceiverProccesor<PassengerMessengerData> {

		
		public void proccesMassege(String passengerId, PassengerMessengerData messengerData) {
						
			Trips<Trip> tripsToPassenger = null;			
			int bidingPrice;			
			bidingPrice = messengerData.bidingPrice;
			auction.placeBid(bidingPrice);		
			boolean connected=true;
			
			//calculating the trip to the given passenger
			try {
								
				tripsToPassenger = tripPlanner.findTrip(transporter, positionSensor.getCurrentPositionByNodeId(), messengerData.currentPassengerPosition);
							
			} catch (TripPlannerException e) {				
					connected=false;
			}
			
			if(!connected){
				taxiDriverMessengerSender.sendPassengerMessage(transporter.getId(), passengerId, vehicle.getId(), null, -1);
			}else{
			
			Trip tripToPassenger = tripsToPassenger.getAndRemoveFirstTrip();
						
			winingPrice=auction.actualPrice;
				
			if (tripToPassenger==null){
				taxiDriverMessengerSender.sendPassengerMessage(transporter.getId(), passengerId, vehicle.getId(), null, winingPrice);								
				}
			else{	 
				taxiDriverMessengerSender.sendPassengerMessage(transporter.getId(), passengerId, vehicle.getId(), tripToPassenger.clone(), winingPrice);
				}
			}		
		}
	}

	/*
	 * class for the processing of the trip confirmation message
	 */
	private class AcceptedPathMessegeReceiverProccesor implements MessegeReceiverProccesor<PassengerConfirmationMessengerData>{

		private final DrivingActivityCallback drivingActivityCallback;
		
		public AcceptedPathMessegeReceiverProccesor(DrivingActivityCallback drivingActivityCallback) {
			super();
			this.drivingActivityCallback = drivingActivityCallback;
		}

		public void proccesMassege(String senderId,	PassengerConfirmationMessengerData messengerData) {
			cumulativeIncome=messengerData.income+cumulativeIncome;
			orderedTrip=new Trip(messengerData.path, vehicle.getGraphForMovingBaseOnType());
			milage=orderedTrip.numTrip()+milage;
			//------------------ LOG -----------------------------
			logAction.logTransporterBooked(transporter.getId(), messengerData.income, cumulativeIncome, orderedTrip.numTrip(), milage);
			//------------------ LOG -----------------------------			
			drivingActivity.drive(orderedTrip, drivingActivityCallback);			
		}		
	}

	
}
