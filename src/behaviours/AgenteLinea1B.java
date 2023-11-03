package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AgenteLinea1B extends CyclicBehaviour{
	
	private String filePath = "C:\\Users\\Usuario\\Desktop\\";	
    private String line = null;
    private int step = 0;

    public AgenteLinea1B(Agent a, String filePathNumber){
        super(a);
        this.filePath += "linea" + filePathNumber + "Data.txt";

    }

	public void action(){

        switch (step) {
            case 0 :
            try {
                File file = new File(filePath);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                line = bufferedReader.readLine();

                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;

            case 1 :
                ACLMessage msg = myAgent.receive();
                if(msg != null){
                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.INFORM);
                    reply.setProtocol("Linea1");
                    reply.setContent(line);
                    myAgent.send(reply);
                }else{
                    System.out.println("AgenteLinea1B: waiting for message");
                    block();
                }
            break;

        }
    }       
}
