package agents;

import jade.core.Agent;
import behaviours.AgenteRutaB;

public class AgenteRuta extends Agent {
	
		protected void setup(){
		Object[] args = getArguments();
		System.out.println("Client agent "+getAID().getName()+" is ready.");
		addBehaviour(new AgenteRutaB(this, args[0].toString(), args[1].toString()));
	}

	protected void takeDown(){
		System.out.println("Client agent "+getAID().getName()+" is terminating.");
	}
	
}
