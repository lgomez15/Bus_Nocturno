package agents;

import jade.core.Agent;
import behaviours.AgenteGrafoB;

public class AgenteGrafo extends Agent{
	
	protected void setup() {
        Object[] args = getArguments();

        System.out.println("Hello im the" + getAID().getLocalName());

        addBehaviour(new AgenteGrafoB(this, args[0].toString()));
    }

    protected void takeDown() {
        System.out.println("Goodbye!");
    }
}
