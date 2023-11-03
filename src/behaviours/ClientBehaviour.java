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

    public ClientBehaviour(Agent a)
    {
        super(a);
    }

        // Tiene que pedir parada de origen, hora de salida y destino.
    public void action()
    {
        
        /*  VARIABLES */

        String pOrigen, pDestino, sHoraSalida;
        Double hSalida;
        Boolean seguir = true; // controla si el agente debe morirse, o permanecer en el bucle.
        String respuesta;
        Scanner scanner = new Scanner(System.in);
        String content; // contenido del msg.
        

        /* PETICIÓN DE VARIABLES */
        
            do{
                System.out.println("Introduzca la parada de origen: " + paradas);
                pOrigen = System.console().readLine();
            }while(Utils.comparaCadenas(pOrigen, paradas) == 0); // Si la parada no existe, se vuelve a pedir.

            do{
                System.out.println("Introduzca la para de destino: " + paradas);
                 pDestino = System.console().readLine();
            }while(Utils.comparaCadenas(pDestino, paradas) == 0); // Si la parada no existe, se vuelve a pedir.

            do{
                System.out.println("Introuduce la hora a la que quieres salir ");
            }while (!scanner.hasNextDouble()); // Si la hora no es un double, se vuelve a pedir.
                hSalida = scanner.nextDouble();
                scanner.close();
                sHoraSalida = Double.toString(hSalida); // Se convierte a String para poder enviarlo por ACLMessage.
                

            content = pOrigen + ":" + pDestino + ":" + sHoraSalida; // Se crea el contenido del mensaje.
                /*COMUNICACIÓN CON EL SERVICIO */
            buscarServicio(content);
            


           
           /*Cmproabmos si se quiere seguir usando el agente, sino se borra.*/
            do{
              System.out.println("¿Quiere realizar otra búsqueda? (S/N)");
              respuesta  = System.console().readLine();
            }while(utils.Utils.comparaCadenas(respuesta,respuestas) == 0);
            if(!seguir){
                myAgent.doDelete();
            }
        

    }

    public void buscarServicio(String content)
    {
        AID[] agents = utils.Utils.searchAgents(myAgent, "Servicio"); // creamos un array de AID con los agentes que ofrecen el servicio.
        ACLMessage msg = new ACLMessage(ACLMessage.REQUEST); // creamos el mensaje.
        msg.setContent(content); // le añadimos el contenido.
        for (int i = 0; i < agents.length; ++i) { 
            msg.addReceiver(agents[i]); // añadimos los receptores. Que será alguno agente de los que ofrecen el servicio.
        }
        myAgent.send(msg); // enviamos el mensaje.
    }



}
