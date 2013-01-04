package bp.taxi.system.transporter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bp.taxi.agents.PTEntity;
import bp.taxi.system.environment.TaxiEnvironment;

import cz.agents.agentpolis.creator.inits.IAgentInit;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.entity.vehicle.Vehicle;
import cz.agents.agentpolis.entity.vehicle.VehicleType;
import cz.agents.agentpolis.graph.EGraphType;
import cz.agents.agentpolis.graph.IGraphType;
import cz.agents.agentpolis.publictransport.planner.TripPlanner;
import cz.agents.agentpolis.publictransport.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.publictransport.trip.Trip;
import cz.agents.agentpolis.utils.convertor.VelocityConvertor;

/*
 * class that cares for initialization of the passenger
 */
public class TransporterInit implements IAgentInit<TaxiEnvironment> {

	/*
	 * block of the variables that can be set
	 */
	public static final int auctionLength=3;	//taxi drivers auction length
	public static final int minimalPrice=35;	//minimal taxi driver allowed price
	public static final int maximalPrice=45;	//for loop for generating of the taxis
	
	
	public List<Agent> initAllAgentLifeCycles(TaxiEnvironment environment) {
		
		List<Agent> agents = new ArrayList<Agent>();
    	
		double velocityOfVehicle = VelocityConvertor.kmph2mps(30);
		
		/*
         * Dijkstra path planning algorithm call
         */
        Set<IGraphType> allowedGraphType = new HashSet<IGraphType>();
        allowedGraphType.add(EGraphType.STREET);
        TripPlanner<Trip> tripPlanner = ShortestPathPlanner.createShortestPathPlanner(environment, allowedGraphType);		
		
                
        //for loop for generating of the taxis
		for(int i=0;i<50;i++){
			
			//vehicle creation
			Vehicle vehicle=new Vehicle("Taxi"+i , environment.handler(),  VehicleType.CAR, 5.0, 2, EGraphType.STREET);
			long statringTaxiDriverAndVehiclePlace=(long) environment.getStreetStorage().getNodeList().get((int)(Math.random()*environment.getStreetStorage().getNodeList().size())).getId();
			environment.getVehicleStorage().addEntity(vehicle);
	        environment.getVehiclePositionStorage().setNewEntityPosition(vehicle.getId(), statringTaxiDriverAndVehiclePlace);
	        environment.getMaxEntityVelocityStorage().addEntityMaxVelocity(vehicle.getId(), velocityOfVehicle);
	        
	       	       	        
	        //factory creation
	        TransporterLifeCycleActivityFactory factory = new TransporterLifeCycleActivityFactory(vehicle, tripPlanner, environment.getMessageProtocol(), minimalPrice, maximalPrice, auctionLength); 

	        // create the Agent
			Agent driver = new Agent("TaxiDriver"+i, environment.handler(),PTEntity.TAXI_DRIVER, factory);

			// Add to agent container.
			environment.getAgentStorage().addEntity(driver);

			// Chose home node.
			environment.getAgentPositionStorage().setNewEntityPosition(driver.getId(), statringTaxiDriverAndVehiclePlace);

			agents.add(driver);
		}
        
		return agents;
	}

	
}


