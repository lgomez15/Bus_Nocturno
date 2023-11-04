package agents;

import behaviours.AgenteLineaB;
import jade.core.Agent;

public class AgenteLinea extends Agent{

	protected void setup(){
		Object[] args = getArguments();
		System.out.println("Client agent "+getAID().getName()+" is ready.");
		addBehaviour(new AgenteLineaB(this, args[0].toString()));
	}

	protected void takeDown(){
		System.out.println("Client agent "+getAID().getName()+" is terminating.");
	}
	
}
