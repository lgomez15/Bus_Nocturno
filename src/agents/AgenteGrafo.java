package agents;

import jade.core.Agent;
import behaviours.AgenteGrafoB;

public class AgenteGrafo extends Agent{
	
	protected void setup() {
        
        System.out.println("Hello im the" + getAID().getLocalName());

        addBehaviour(new AgenteGrafoB(this));
    }

    protected void takeDown() {
        System.out.println("Goodbye!");
    }
}
