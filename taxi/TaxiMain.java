package bp.taxi;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import bp.taxi.agents.PTEntity;
import bp.taxi.log.ETaxiDriverKey;
import bp.taxi.log.ETaxiDriverState;
import bp.taxi.log.ETaxiPassengerKey;
import bp.taxi.log.ETaxiPassengerState;
import bp.taxi.system.environment.TaxiEnvironment;
import bp.taxi.system.environment.TaxiEnvironmentFactory;
import bp.taxi.system.passenger.PassengerInit;
import bp.taxi.system.transporter.TransporterInit;
import cz.agents.agentpolis.creator.ScenarioCreator;
import cz.agents.agentpolis.event.IEnumEvent;
import cz.agents.agentpolis.event.IEnumEventKey;
import cz.agents.agentpolis.publictransport.demo.googleearth.vehicle.log.EVehicleEnumEvent;
import cz.agents.agentpolis.publictransport.demo.googleearth.vehicle.log.VehicleEventProcessor;
import cz.agents.agentpolis.publictransport.queue.resolver.factory.QueueFactory;

/**
 * simulation running main class
 *
 * @author Jan Zikes
 */
public class TaxiMain 
{
    
	public static void main( String[] args )
    {
    	
    		/*
    		 * naming the file that should be created for the experiment
    		 */
    		File experiment = new File("experiments/exp-1");
    		ScenarioCreator<TaxiEnvironment> creator = new ScenarioCreator<TaxiEnvironment>(new TaxiEnvironmentFactory(), experiment, 0);		
    	
    		//log to CSV
    	    	
    		List<IEnumEvent> allowEvents = new ArrayList<IEnumEvent>();
    			
    		allowEvents.addAll(Arrays.asList(ETaxiPassengerState.values()));
    		allowEvents.addAll(Arrays.asList(ETaxiDriverState.values()));
    	
    		List<IEnumEventKey> keysToData = new ArrayList<IEnumEventKey>();
    		keysToData.addAll(Arrays.asList(ETaxiPassengerKey.values()));
    		keysToData.addAll(Arrays.asList(ETaxiDriverKey.values()));
		
    		creator.addAllowEventsAndKeysForCSV(allowEvents, keysToData);
		
    		List<IEnumEvent> allowVehicleEvents = new ArrayList<IEnumEvent>();
    		allowVehicleEvents.addAll(Arrays.asList(EVehicleEnumEvent.values()));
		
    		VehicleEventProcessor vehicleEventProcessor = new VehicleEventProcessor(allowVehicleEvents); 
		
    		creator.addEventProcessorToEventProtocl(vehicleEventProcessor);
    	
    		//!log to CSV
		
		
    		//adding the elements to the scenario
    		creator.addInitFactory(new QueueFactory());		
			creator.addAgentInit(new TransporterInit());
    		creator.addAgentInit(new PassengerInit());
    	
    		
    		//entity color setting
    		creator.addEntityStyleVis(PTEntity.TAXI_DRIVER, Color.RED, 9);
    		creator.addEntityStyleVis(PTEntity.CAR_PASSENGER, Color.GREEN, 8);
    	
    		        
    		//creation of the scenario
    		creator.create();
    		creator=null;
    	
        
    }
}
