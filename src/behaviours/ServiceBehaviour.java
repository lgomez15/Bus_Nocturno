package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;


public class ServiceBehaviour extends CyclicBehaviour{


    private String comandoGrafo = "cmd /c start cmd.exe @cmd /k \"java jade.Boot -container agenteGrafo:agents.AgenteGrafo";
    Map <String, String> uidClientes; // mapa para almaacenar los uids de los clientes y el uid random que les voy a asingar.
    
    public ServiceBehaviour(Agent a)
    {
        super(a);
        this.uidClientes = new HashMap<String, String>();
        try
       {
            Runtime.getRuntime().exec(comandoGrafo);
       }catch(Exception e)
       {
            System.out.println(e);
       }
    }

    public void action()
    {



        ACLMessage msg = myAgent.receive(); 
        if(msg != null)
        {
            if(msg.getPerformative() == ACLMessage.REQUEST) // Si es un request, es de un nodo superior.
            {
                System.out.println("Peticion recibida del cliente: " + msg.getSender().toString());
                UUID uid = UUID.randomUUID();
                this.uidClientes.put(uid.toString(), msg.getSender().getLocalName()); // registramos el uid del cliente y el uid random que le hemos asignado este ultimo será el que pasemos al agente grafo.
                ACLMessage msgReq = new ACLMessage(ACLMessage.REQUEST);

                /*Le damos estructura al mensaje */
                msgReq.setReplyWith(uid.toString()); // le pasamos el uid random que le hemos asignado al cliente.
                String infoParaGrafo = msg.getContent();
                msgReq.setContent(infoParaGrafo);
                System.out.println("Enviando peticion al agente grafo");
                msgReq.addReceiver(buscarAID("agenteGrafo")); // se lo tenemos que enviar al agente grafo.
                myAgent.send(msgReq);
                System.out.println("Peticion enviada al agente grafo");
        
            }
            else  // Si es un inform, es de un nodo inferior.
            {
                System.out.println("Respuesta recibida del agente grafo");
                System.out.println("contenido: " + msg.getContent());
                String contenido = msg.getContent(); // obtenemos el contenido del mensaje.
                String uidCliente = (String) msg.getInReplyTo(); // obtenemos el uid del cliente.
                System.out.println("uidCliente: " + uidCliente);
                
                ACLMessage msgClienteFinal = new ACLMessage(ACLMessage.INFORM);
                msgClienteFinal.setContent(contenido);
                msgClienteFinal.addReceiver(new AID(this.uidClientes.get(uidCliente), jade.core.AID.ISLOCALNAME)); // le pasamos el uid del cliente para que se lo envie a el.
                myAgent.send(msgClienteFinal);
                System.out.println("Respuesta enviada al cliente: " + uidClientes.get(uidCliente));
                
            }
        }
        else
        {
           block();
        }


    }
    private  AID buscarAID(String nombreAgente)
    {
        AID aid = new AID(nombreAgente, jade.core.AID.ISLOCALNAME);
        return aid;
    }

}