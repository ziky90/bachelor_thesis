package bp.taxi.system;

import java.util.HashSet;

/*
 * Local environment class for the subproject dynamically price setting in transport
 */
public class SystemInformations{
	
	private static volatile SystemInformations systemInformations;
	private final HashSet<String> availableTransporters=new HashSet<String>();
	
	/*
	 * constructor
	 */
	private SystemInformations(){}
	
	/*
	 * Check if there is given transporter on the list of available transporters
	 * @param String id
	 */
	public boolean checkAvailabilityId(String id){
		return availableTransporters.contains(id);
	}
	
	/*
	 * Method for adding the transporter on the available list
	 */
	public void addTransporterToTheSystem(String transporterId){
		availableTransporters.add(transporterId);		
	}
	
	/*
	 * method from removing of the transporter from available list
	 */
	public void removeTransporterFromTheSystem(String id){
		availableTransporters.remove(id);
	}
	
	/*
	 * Thread safe singleton get instance
	 */
	public static SystemInformations getInstance(){
		if (systemInformations == null) {
			synchronized (SystemInformations.class) {
				if (systemInformations == null) {
					systemInformations = new SystemInformations();
				}

			}
		}
		return systemInformations;
	}
	}

