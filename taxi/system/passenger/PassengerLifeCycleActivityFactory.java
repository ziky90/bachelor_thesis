package bp.taxi.system.passenger;

import org.joda.time.Duration;

import bp.taxi.system.passenger.comunication.PassengerMessengerSender;

import cz.agents.agentpolis.activity.ILifeCycleActivity;
import cz.agents.agentpolis.activity.ILifeCycleActivityFactory;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.environment.sensor.TimeSensor;
import cz.agents.agentpolis.environment.sensor.agent.AgentPositionSensor;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.environment.PublicTransportEnvironment;
import cz.agents.agentpolis.publictransport.trip.Trip;


/*
 * factory class for creating of the passenger
 */
public class PassengerLifeCycleActivityFactory implements ILifeCycleActivityFactory {

	private final Duration startLife;
	private final long passengerWantedDestination;
	private final PublicTransportEnvironment enviroment;
	private final MessageProtocol messageProtocol;
	private final int maximalPrice;
	private final byte necessity;
	public Trip trip;
	
	
	public PassengerLifeCycleActivityFactory(Duration startLife, long passengerWantedDestination, MessageProtocol messageProtocol, PublicTransportEnvironment enviroment, int maximalPrice, byte necessity) {
		super();
		this.startLife = startLife;
		this.passengerWantedDestination = passengerWantedDestination;
		this.messageProtocol = messageProtocol;
		this.enviroment=enviroment;		
		this.maximalPrice=maximalPrice;
		this.necessity=necessity;
	}


	public ILifeCycleActivity createLifeCycleActivity(Agent passenger) {

		TimeSensor timeSensor = passenger.addSensor(TimeSensor.class);
		TimeSensor deciderTime = passenger.addSensor(TimeSensor.class);
		AgentPositionSensor positionSensor = passenger.addSensor(AgentPositionSensor.class);
		PassengerMessengerSender taxiPassengerMessengerSender = new PassengerMessengerSender(messageProtocol, positionSensor);		
		PassengerLifeCycleActivity lifeCycleActivity = new PassengerLifeCycleActivity(passenger, timeSensor, passengerWantedDestination, taxiPassengerMessengerSender, startLife, enviroment, deciderTime, maximalPrice, necessity, positionSensor, trip);
		messageProtocol.addMessageReceiverCallback(passenger.getId(), lifeCycleActivity);
		return lifeCycleActivity;

	}
	
	

}
