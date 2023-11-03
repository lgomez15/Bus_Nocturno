package agents;

import behaviours.AgenteLinea1B;
import jade.core.Agent;

public class AgenteLinea1 extends Agent{

	protected void setup(){
		Object[] args = getArguments();
		System.out.println("Client agent "+getAID().getName()+" is ready.");
		addBehaviour(new AgenteLinea1B(this, args[0].toString()));
	}

	protected void takeDown(){
		System.out.println("Client agent "+getAID().getName()+" is terminating.");
	}
	
}
