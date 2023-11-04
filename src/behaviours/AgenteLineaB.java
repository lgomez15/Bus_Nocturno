package behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AgenteLineaB extends CyclicBehaviour{
	
	private String filePath = "C:\\Users\\Usuario\\Desktop\\";
    String linea1Grafo = "1-2:2,2-3:2,3-4:2,4-5:7,5-6:1,6-7:4,7-8:3,8-9:2,9-10:1";
    String linea2Grafo = "11-4:3,4-12:4,12-9:3,9-13:2";
    private int filePathNumber;	
    private String lineaInfo = null;
    private String autobusesInfo = null;
    private int step = 0;

    public AgenteLineaB(Agent a, String filePathNumber){
        super(a);
        this.filePath += "linea" + filePathNumber + "Data.txt";
        this.filePathNumber = Integer.parseInt(filePathNumber);
    }

	public void action(){

        switch (step) {
            case 0 :
            try {
                File file = new File(filePath);
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);

                autobusesInfo = bufferedReader.readLine();

                bufferedReader.close();
                step = 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            break;

            case 1 :
                ACLMessage msg = myAgent.receive();
                if(msg != null)
                {
                    if(filePathNumber == 1)
                    {
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        lineaInfo = linea1Grafo + "/" + autobusesInfo;
                        reply.setContent(lineaInfo);
                        reply.addReceiver(msg.getSender());
                        myAgent.send(reply);
                    }
                    else if(filePathNumber == 2)
                    {
                        ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
                        lineaInfo = linea2Grafo + "/" + autobusesInfo;
                        reply.setContent(lineaInfo);
                        reply.addReceiver(msg.getSender());
                        myAgent.send(reply);
                    }
                }
                else
                {
                    block();
                }
            break;
        }
    }       
}
