package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class AgenteGrafoB extends CyclicBehaviour {

	private String linea1Info = null;
	private String linea2Info = null;
	private String request;
	private int infoCompleta = 0;
	private int step = 0;
	private int agentesRuta = 0;


	public AgenteGrafoB(Agent a) {
		super(a);
	}

	public void action(){

		switch (step) {
			case 0:
				ACLMessage msg = new ACLMessage(ACLMessage.REQUEST);
				msg.setContent("L1");
				msg.addReceiver(new AID("AgenteLinea1", AID.ISLOCALNAME));  
				myAgent.send(msg);
				
				ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
				msg2.setContent("L2");
				msg2.addReceiver(new AID("AgenteLinea2", AID.ISLOCALNAME));
				myAgent.send(msg2);
				
				step = 1;
				
			break;
			case 1:
			System.out.println("esperando infor de lineas, soy grafo");
				ACLMessage reply = myAgent.receive();
				if (reply != null) {
					if (reply.getPerformative() == ACLMessage.INFORM && reply.getInReplyTo().toString().equals("L1")) {
						linea1Info = reply.getContent();
						infoCompleta++;
						System.out.println("Info recibida de L1: " + linea1Info );
					}
					else if (reply.getPerformative() == ACLMessage.INFORM && reply.getInReplyTo().toString().equals("L2")) {
						linea2Info = reply.getContent();
						infoCompleta++;
						System.out.println("Info recibida de L2: " + linea2Info);
					}
					if (infoCompleta == 2) {
						step = 2;
					}
				}
				else
				{
					block();
				}
			break;	
			case 2:
				//recieve requests from service
				ACLMessage msg3 = myAgent.receive();
				if (msg3 != null) {
					if (msg3.getPerformative() == ACLMessage.REQUEST) 
					{
						System.out.println("Recibido REQUEST de Servicio");
						request = msg3.getContent() + "/n" + linea1Info + "/n" + linea2Info;
						String uuid = msg3.getReplyWith();
						String receiver = "AgenteRuta"+ agentesRuta;
						String exeFormatRutaAgent = "cmd /c start cmd.exe @cmd /k \"java jade.Boot -container AgenteRuta" + agentesRuta + ":agents.AgenteRuta(" +uuid +","+ 1 + ")";
						agentesRuta++;
						try {
							Runtime.getRuntime().exec(exeFormatRutaAgent);
						} catch (Exception e) {
							e.printStackTrace();
						}
						
						
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						//is copilot working? 
						ACLMessage info = new ACLMessage(ACLMessage.INFORM);
						info.addReceiver(new AID(receiver, AID.ISLOCALNAME));
						info.setReplyWith(uuid);
						info.setContent(request);
						myAgent.send(info);
						
					}
					else 
					{
						System.out.println("Recibido INFORM de agenteRuta");
						ACLMessage reply3 = new ACLMessage(ACLMessage.INFORM);
						reply3.addReceiver(new AID("Servicio", AID.ISLOCALNAME));
						reply3.setInReplyTo(msg3.getInReplyTo());
						reply3.setContent(msg3.getContent());
						myAgent.send(reply3);

					}
				}
				else {
					block();
				}

			break;

		}

	}
}
