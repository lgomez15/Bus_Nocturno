package agents;
import behaviours.ClientBehaviour;
import jade.core.Agent;

public class Client extends Agent {
    protected void setup()
    {
        System.out.println("Hi, agent Client created");
        addBehaviour(new ClientBehaviour(this));

    }
    protected void takeDown()
    {
        System.out.println("Bye, agent Client destroyed");
    }
}
