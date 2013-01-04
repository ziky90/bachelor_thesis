package bp.taxi.system.passenger;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.joda.time.Duration;

import bp.taxi.agents.PTEntity;
import bp.taxi.system.environment.TaxiEnvironment;

import cz.agents.agentpolis.creator.inits.IAgentInit;
import cz.agents.agentpolis.entity.agent.Agent;
import cz.agents.agentpolis.graph.EGraphType;
import cz.agents.agentpolis.graph.IGraphType;
import cz.agents.agentpolis.publictransport.planner.TripPlanner;
import cz.agents.agentpolis.publictransport.planner.TripPlannerException;
import cz.agents.agentpolis.publictransport.planner.path.ShortestPathPlanner;
import cz.agents.agentpolis.publictransport.trip.Trip;
import cz.agents.agentpolis.publictransport.trip.Trips;

/*
 * class for initialization of the passenger
 */
public class PassengerInit implements IAgentInit<TaxiEnvironment> {

	//private static final Logger logger = Logger.getLogger(PassengerInit.class);
	private static int lastId=0;
	
	/*
	 * simulation parameters to set
	 */
	private static final int peek1Passengers=250;	//passengers
	private static final int peek1offset=420;		//offset
	private static final int peek1duration=120;		//duration
	private static final int offPeekPassengers=50;	//passengers
	private static final int offpeekoffset=540;		//offset
	private static final int offpeekduration=420;	//duration
	private static final int peek2Passengers=250;	//passengers
	private static final int peek2offset=960;		//offset
	private static final int peek2duration=180;		//duration
	private static final int maximalPrice=100;		
	
	public PassengerInit() {
		super();

	}

	
	/*
	 * life cycles creating method
	 * 
	 */
	public List<Agent> initAllAgentLifeCycles(TaxiEnvironment environment) {
		

			List<Agent> agents = new ArrayList<Agent>();

			/*
			 * Dijkstra path planning algorithm call
			 */
			Set<IGraphType> allowedGraphType = new HashSet<IGraphType>();
			allowedGraphType.add(EGraphType.STREET);
			TripPlanner<Trip> tripPlanner = ShortestPathPlanner.createShortestPathPlanner(environment, allowedGraphType);
			
			/*
			 * generating of bunches of passengers
			 */
			agents.addAll(generateAgents(peek1Passengers, peek1offset, peek1duration, tripPlanner, environment));
			agents.addAll(generateAgents(offPeekPassengers, offpeekoffset, offpeekduration, tripPlanner, environment));
			agents.addAll(generateAgents(peek2Passengers, peek2offset, peek2duration, tripPlanner, environment));
			
			return agents;
	}
	
	
	
	
	/*
	 * method for the generating of the given number of passengers that are distributed uniformly through a given time
	 */
	public List<Agent> generateAgents(int number, int begining, int duration, TripPlanner<Trip> tripPlanner, TaxiEnvironment environment){
		
		List<Agent> agents = new ArrayList<Agent>();
		
		//for that iterates over all the passengers that should be generated
		for (int i = 0; i < number; i++) {

			//passenger important points generation
			long startingPassengerPoint = (long) environment.getStreetStorage().getNodeList().get((int) (Math.random() * environment.getStreetStorage().getNodeList().size())).getId();
			long passengerWantedDestination = (long) environment.getStreetStorage().getNodeList().get((int) (Math.random() * environment.getStreetStorage().getNodeList().size())).getId();

			Agent passenger = null;
			boolean exception = true;
			Duration startLife = Duration.standardMinutes(begining+(int)(Math.random()*duration)); 
			
			//generating of the necessity and prices in the normal distribution
			byte necessity;
			int maximalParticularPrice=maximalPrice;
			if ((int) ((Math.random() * 10) + 1)<3){
				necessity=1;
				maximalParticularPrice = ((int) (Math.random() * maximalPrice-50)) + 10;
			}else if((int) ((Math.random() * 10) + 1)<9){
				necessity=2;
				maximalParticularPrice = ((int) (Math.random() * maximalPrice-50)) + 30;
			}else{
				necessity=3;
				maximalParticularPrice = ((int) (Math.random() * maximalPrice-50)) + 50;
			}
			
			
			
			String passengerId = "Passenger" + (i+lastId);
			Trips<Trip> myTrips = null;
			PassengerLifeCycleActivityFactory factory = new PassengerLifeCycleActivityFactory(startLife, passengerWantedDestination,environment.getMessageProtocol(), environment,maximalParticularPrice, necessity);
			
			//loop that is checking if the given journey is connected
			while (exception) {

				//loop that is checking if the given starting point is not equal to the destination point
				while (startingPassengerPoint == passengerWantedDestination) {
					startingPassengerPoint = (long) environment.getStreetStorage().getNodeList().get((int) (Math.random() * environment.getStreetStorage().getNodeList().size())).getId();
				}

				factory = new PassengerLifeCycleActivityFactory(startLife, passengerWantedDestination, environment.getMessageProtocol(), environment, maximalParticularPrice, necessity);
				passenger = new Agent(passengerId, environment.handler(),	PTEntity.CAR_PASSENGER, factory);

				//checking for non connected journey
				try{
					myTrips=tripPlanner.findTrip(passenger, startingPassengerPoint,passengerWantedDestination);
					exception = false;
				}catch(TripPlannerException e){
					startingPassengerPoint = (long) environment.getStreetStorage().getNodeList().get((int) (Math.random() * environment.getStreetStorage().getNodeList().size())).getId();
					passengerWantedDestination = (long) environment.getStreetStorage().getNodeList().get((int) (Math.random() * environment.getStreetStorage().getNodeList().size())).getId();
					exception = true;
				}
				
			}
		
			factory.trip=myTrips.getAndRemoveFirstTrip();
			
			environment.getAgentStorage().addEntity(passenger);

			environment.getAgentPositionStorage().setNewEntityPosition(	passenger.getId(), startingPassengerPoint);

			agents.add(passenger);
			
		}
		//increasing the id offset
		lastId=lastId+number;
		return agents;
	}

	
}
