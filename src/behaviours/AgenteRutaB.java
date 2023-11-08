package behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class AgenteRutaB extends OneShotBehaviour {

	public String request;
	public String uuid;
	public AgenteRutaB(Agent a,String uuid, String request) {
		super(a);
		this.request = request;
		this.uuid = uuid;
	}


	public void action(){
		System.out.println("Ruta: " + request);
		
		//response to agenteGrafo
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setContent(request);
		msg.setInReplyTo(uuid);
		msg.addReceiver(new jade.core.AID("agenteGrafo", jade.core.AID.ISLOCALNAME));
		myAgent.send(msg);
	}

	
}
