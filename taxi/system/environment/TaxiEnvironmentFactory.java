package bp.taxi.system.environment;

import org.openstreetmap.osm.data.coordinates.Bounds;

import cz.agents.agentpolis.environment.IEnvironmentFactory;
import cz.agents.alite.simulation.Simulation;

/*
 * Taxi environment factory which is made with respect to Agent polis platform
 */
public class TaxiEnvironmentFactory implements IEnvironmentFactory<TaxiEnvironment> {

	public TaxiEnvironmentFactory(){
		super();
	}
	
	public TaxiEnvironment createEnvironment(Simulation simulation,	Bounds bounds, long seed, boolean showVisio) {
		
			return new  TaxiEnvironment(simulation, bounds,  seed,  showVisio);
	}
		

}
