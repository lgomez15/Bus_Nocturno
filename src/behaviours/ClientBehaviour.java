package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import utils.Utils;
import java.util.Scanner;

public class ClientBehaviour extends CyclicBehaviour{
 
    public String paradas[] = {"P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8", "P10", "P11", "P12", "P13"};
    public String respuestas[] = {"S", "N"}; // Respuestas válidas para el cliente.
    public int step = 0; // 0 -> enviar mensaje, 1-> recibir mensaje, 2-> borrar
    
    public ClientBehaviour(Agent a)
    {
        super(a);
    }

        // Tiene que pedir parada de origen, hora de salida y destino.
    public void action()
    {
        
        /*  VARIABLES */
        String content; // contenido del msg.

        switch(step)
        {
            case 0:
                content = pedirDatos(); // Pedimos los datos al usuario.        
                comunicarConServicio(content); // busca el servicio y envia el mensje.
                step = 1;
            break;

            case 1:
                esperaMensaje(); // imprime el mensaje.
                step = repetir(); // pide si se quiere realizar otra búsqueda. Devuevle 0 si se quiere repetir y 2 si no.
            break;
            
            case 2:
                myAgent.doDelete(); // borra el agente.
            
        }
    }



    public String pedirDatos()
    {
        String content;
        String pOrigen, pDestino, sHoraSalida;
        Double hSalida;
        Scanner scanner = new Scanner(System.in);

        do{
            System.out.println("Introduzca la parada de origen: " + paradas);
            pOrigen = System.console().readLine();
         }while(Utils.comparaCadenas(pOrigen, paradas) != 0); // Si la parada no existe, se vuelve a pedir.

        do{
            System.out.println("Introduzca la para de destino: " + paradas);
            pDestino = System.console().readLine();
        }while(Utils.comparaCadenas(pDestino, paradas) != 0); // Si la parada no existe, se vuelve a pedir.

        do{
            System.out.println("Introuduce la hora a la que quieres salir ");
        }while (!scanner.hasNextDouble()); // Si la hora no es un double, se vuelve a pedir.
            hSalida = scanner.nextDouble();
            scanner.close();
            sHoraSalida = Double.toString(hSalida); // Se convierte a String para poder enviarlo por ACLMessage.    

        content = pOrigen + ":" + pDestino + ":" + sHoraSalida; // Se crea el contenido del mensaje.

        return content;
    }

    public void comunicarConServicio(String content)
    {
        AID[] agents = utils.Utils.searchAgents(myAgent, "Servicio"); // creamos un array de AID con los agentes que ofrecen el servicio.
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST); // creamos el mensaje.
        msg.setContent(content); // le añadimos el contenido.
        for (int i = 0; i < agents.length; ++i) { 
            msg.addReceiver(agents[i]); // añadimos los receptores. Que será alguno agente de los que ofrecen el servicio.
        }
        myAgent.send(msg); // enviamos el mensaje.
    }

    public void esperaMensaje()
    {
        ACLMessage msg = myAgent.receive(); // recibimos el mensaje.
        if(msg != null)
        {
            if(msg.getPerformative() == ACLMessage.INFORM) // Si es un inform, es de un nodo inferior.
            {
               imprimeMensaje(msg.getContent()); // imprimimos el mensaje.
            }
            else
            {
                System.out.println("No se ha encontrado ninguna ruta");
            }
        }
        else
        {
            block();
        }

    }

    public void imprimeMensaje(String content)
    {
        System.out.println(""+content);
    }

    public int repetir()
    {
        String respuesta;
        do{
            System.out.println("¿Quiere realizar otra búsqueda? (S/N)");
            respuesta  = System.console().readLine();
        }while(utils.Utils.comparaCadenas(respuesta,respuestas) != 0);

        if(respuesta.equalsIgnoreCase("N"))
        {
            return 2; // borra el agente
        }
        else
        {
           return 0;
        }

    }

}
