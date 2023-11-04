package behaviours;

import java.util.Map;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class AgenteGrafoB extends CyclicBehaviour {

	private String linea1Info = null;
	private String linea2Info = null;
	private String REQ = null;
	private int step = 0;


	public AgenteGrafoB(Agent a) {
		super(a);
	}

	public void action(){

		switch (step) {
			case 0:
				//send req msg to linea1 agent
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setContent("L1");
				msg.addReceiver(new AID("agenteLinea1", AID.ISLOCALNAME));  
				myAgent.send(msg);
				
				//wait for response
				ACLMessage reply = myAgent.receive();
				if (reply != null) {
					if (reply.getPerformative() == ACLMessage.INFORM) {
						linea1Info = reply.getContent();
					}
					step = 1;
				}
				else {
					System.out.println("waiting for response from linea 1...");
					block();
				}
			break;
			case 1:

				//send req msg to linea2 agent
				ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
				msg2.setContent("L2");
				msg2.addReceiver(new AID("agenteLinea2", AID.ISLOCALNAME));
				myAgent.send(msg2);

				//wait for response
				ACLMessage reply2 = myAgent.receive();
				if (reply2 != null) {
					if (reply2.getPerformative() == ACLMessage.INFORM) {
						linea2Info = reply2.getContent();
					}
					step = 2;
				}
				else {
					System.out.println("waiting for response from linea 2...");
					block();
				}
			break;	
			case 2:
				//recieve requests from service
				ACLMessage msg3 = myAgent.receive();
				if (msg3 != null) {
					if (msg3.getPerformative() == ACLMessage.REQUEST) 
					{
						REQ = msg3.getContent() + "/n" + linea1Info + "/n" + linea2Info;
						String uuid = msg3.getReplyWith();
						String exeFormatManager = "cmd /c start cmd.exe @cmd /k \"java jade.Boot -container agenteRuta" + uuid + ":agents.AgenteRuta(" + REQ + ")";
						
						try {
							Runtime.getRuntime().exec(exeFormatManager);
						} catch (Exception e) {
							e.printStackTrace();
						}					
					}
					else 
					{
						//recieve RESPONSE from rutaAgents and INFORM to Service
						ACLMessage reply3 = new ACLMessage(ACLMessage.INFORM);
						reply3.addReceiver(new AID("Servicio", AID.ISLOCALNAME));
						reply3.setInReplyTo(msg3.getInReplyTo());
						reply3.setContent(msg3.getContent());
						myAgent.send(reply3);

					}
				}
				else {
					System.out.println("waiting for request from service...");
					block();
				}

			break;

		}

	}
}
