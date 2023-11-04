package behaviours;

import jade.core.AID;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;

import java.util.Map;
import java.util.UUID;
import java.util.HashMap;


public class ServiceBehaviour extends CyclicBehaviour{


    private String comandoGrafo = "cmd /c start cmd.exe @cmd /k \"java jade.Boot -container agenteGrafo:agents.AgenteGrafo";
    Map <String, String> uidClientes; // mapa para almaacenar los uids de los clientes y el uid random que les voy a asingar.
    
    public ServiceBehaviour()
    {
        super();
        this.uidClientes = new HashMap<String, String>();
    }

    public void action()
    {

        /*CREAMOS EL AGENTE GRAFO */
       try
       {
            Runtime.getRuntime().exec(comandoGrafo);
       }catch(Exception e)
       {
        System.out.println("" + e);
       }
       /* RECIBBIMOS LOS MENSAJES EN LA COLA
        * 1- Si es un inform es de un nodo inferior
        * 2- Si es un request es de un nodo superior
        */
        
        ACLMessage msg = myAgent.receive(); 
        // para encontrar el tipo de mensaje.
       if(msg.getPerformative() == ACLMessage.REQUEST) // Si es un request, es de un nodo superior.
       {
        UUID uid = UUID.randomUUID();
        this.uidClientes.put(uid.toString(), msg.getSender().toString()); // registramos el uid del cliente y el uid random que le hemos asignado este ultimo será el que pasemos al agente grafo.
        ACLMessage msgReq = new ACLMessage(ACLMessage.REQUEST);

        /*Le damos estructura al mensaje */
        msgReq.setReplyWith(uid.toString()); // le pasamos el uid random que le hemos asignado al cliente.
        String infoParaGrafo = msg.getContent();
        msgReq.setContent(infoParaGrafo);
        msgReq.addReceiver(buscarAID("agenteGrafo")); // se lo tenemos que enviar al agente grafo.
        myAgent.send(msgReq);
      
    }
       else if(msg.getPerformative() == ACLMessage.INFORM) // Si es un inform, es de un nodo inferior.
       {
        String contenido = msg.getContent(); // obtenemos el contenido del mensaje.
        String uidCliente = msg.getInReplyTo(); // obtenemos el uid del cliente.

        ACLMessage msgClienteFinal = new ACLMessage(ACLMessage.INFORM);
        msgClienteFinal.setContent(contenido);
        msgClienteFinal.addReceiver(buscarAID(uidClientes.get(uidCliente))); // le pasamos el uid del cliente para que se lo envie a el.
        myAgent.send(msgClienteFinal);
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