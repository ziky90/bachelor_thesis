package bp.taxi.system.passenger;

import cz.agents.agentpolis.environment.sensor.callback.TimeCallback;

/*
 * class for detecting certain time callbacks
 */
public class Decider implements TimeCallback {

	PassengerLifeCycleActivity passengerLifeCycleActivity;
	
	/*
	 * constructor
	 */
	public Decider (PassengerLifeCycleActivity passengerLifeCycleActivity){
		this.passengerLifeCycleActivity=passengerLifeCycleActivity;
	}
	/*
	 * time callback method that is activated after the certain time
	 */
	public void timeCallback() {
		passengerLifeCycleActivity.decideForTransport();
	}

}
