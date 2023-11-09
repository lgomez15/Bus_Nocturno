package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AgenteLineaB extends CyclicBehaviour{
	
	private String filePath = "C:\\Users\\luisg\\Desktop\\";
    String linea1Grafo = "1:0:2,2:2:3,3:3:4,4:2:5,5:7:6,6:1:7,7:4:8,8:3:9,9:2:10,10:1:0";
    String linea2Grafo = "11:0:4,4:3:12,12:4:9,9:3:13,13:2:0";
    private int filePathNumber;	
    private String lineaInfo = null;
    private String autobusesInfo = null;

    public AgenteLineaB(Agent a, String filePathNumber){
        super(a);
        this.filePath += "linea" + filePathNumber + "Data.txt";
        this.filePathNumber = Integer.parseInt(filePathNumber);
        try {
                File file = new File(filePath);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                autobusesInfo = bufferedReader.readLine();

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

	public void action(){

        ACLMessage msg = myAgent.receive();
        if(msg != null)
        {
            if(filePathNumber == 1)
            {
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                lineaInfo = linea1Grafo + "/" + autobusesInfo;
                reply.setContent(lineaInfo);
                reply.addReceiver(msg.getSender());
                reply.setInReplyTo("L1");
                myAgent.send(reply);
            }
            else if(filePathNumber == 2)
            {
                ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                lineaInfo = linea2Grafo + "/" + autobusesInfo;
                reply.setContent(lineaInfo);
                reply.addReceiver(msg.getSender());
                reply.setInReplyTo("L2");
                myAgent.send(reply);
            }
        }
        else
        {
            block();
        }
    }
           
}
