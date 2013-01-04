package bp.taxi.system.transporter;

import bp.taxi.system.transporter.comunication.TransportDriverMessengerSender;
import cz.agents.agentpolis.activity.ILifeCycleActivity;
import cz.agents.agentpolis.activity.ILifeCycleActivityFactory;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.entity.vehicle.Vehicle;
import cz.agents.agentpolis.environment.sensor.TimeSensor;
import cz.agents.agentpolis.environment.sensor.agent.AgentPositionSensor;
import cz.agents.agentpolis.publictransport.activity.movement.DrivingActivity;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.planner.TripPlanner;
import cz.agents.agentpolis.publictransport.trip.Trip;

/*
 * factory class for the creation of transporters including their vehicles
 */
public class TransporterLifeCycleActivityFactory implements ILifeCycleActivityFactory{

	private final Vehicle vehicle;
	private final TripPlanner<Trip> tripPlanner;	
	private final MessageProtocol messageProtocol;
	private final int minimalPrice;
	private final int maximalPrice;
	private final int auctionLength;
	
	
	public TransporterLifeCycleActivityFactory(Vehicle vehicle, TripPlanner<Trip> tripPlanner, MessageProtocol messageProtocol, int minimalPrice, int maximalPrice, int auctionLength) {
		super();
		this.vehicle = vehicle;
		this.tripPlanner = tripPlanner;		
		this.messageProtocol = messageProtocol;		
		this.minimalPrice=minimalPrice;
		this.maximalPrice=maximalPrice;
		this.auctionLength=auctionLength;
	}
	
	public ILifeCycleActivity createLifeCycleActivity(Agent driver) {

		TimeSensor timeSensor = driver.addSensor(TimeSensor.class);
		TransportDriverMessengerSender taxiDriverMessengerSender = new TransportDriverMessengerSender(messageProtocol);
		AgentPositionSensor positionSensor = driver.addSensor(AgentPositionSensor.class);		
		DrivingActivity drivingActivity = new DrivingActivity(driver, vehicle);		
		TransporterLifeCycleActivity lifeCycleActivity = new  TransporterLifeCycleActivity(driver, vehicle,drivingActivity, tripPlanner, positionSensor, taxiDriverMessengerSender, timeSensor, minimalPrice, maximalPrice, auctionLength);
		messageProtocol.addMessageReceiverCallback(driver.getId(), lifeCycleActivity);
			
		return lifeCycleActivity;
	}

}
