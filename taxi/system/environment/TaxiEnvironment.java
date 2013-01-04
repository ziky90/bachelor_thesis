package bp.taxi.system.environment;

import java.util.HashMap;
import org.openstreetmap.osm.data.coordinates.Bounds;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageProtocol;
import cz.agents.agentpolis.publictransport.enviroment.communication.protocol.MessageReceiverCallback;
import cz.agents.agentpolis.publictransport.environment.PublicTransportEnvironment;
import cz.agents.alite.communication.DefaultCommunicator;
import cz.agents.alite.communication.channel.CommunicationChannelException;
import cz.agents.alite.communication.eventbased.EventBasedCommunicationChannel;
import cz.agents.alite.simulation.Simulation;


/*
 * TaxiEnvironment which is classical PTE with few changes
 */
public class TaxiEnvironment extends PublicTransportEnvironment {

	private static int counter = 0;						//value that enables multiple simulations run
	private final MessageProtocol messageProtocol;		//message protocol that hides the protocol in the parent class
	
	

	public TaxiEnvironment(Simulation simulation, Bounds bounds,  long seed, boolean noStats) {
		super(simulation, bounds,  seed, noStats);

		
		
		// ----- communication -----------------------
		/*
		 * Hiding of the quiet similar construct in the parent class
		 */
				        
		DefaultCommunicator communicator = new DefaultCommunicator(TaxiEnvironment.class.getClass().getSimpleName() + "@" + MessageProtocol.class.hashCode()+counter++);

				EventBasedCommunicationChannel eventBasedCommunicationChannel;
				try {
				eventBasedCommunicationChannel = new EventBasedCommunicationChannel(communicator, getEventProcessor());
				communicator.addChannel(eventBasedCommunicationChannel);
				} catch (CommunicationChannelException e) {
				e.printStackTrace();
				}

				this.messageProtocol = new MessageProtocol(communicator, "Demo message protocol",new HashMap<String,MessageReceiverCallback>());

     // ----- communication -----------------------
        
	}

	
		@Override
	public MessageProtocol getMessageProtocol() {
		return messageProtocol;
	}
}
