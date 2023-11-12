package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.*;
import jade.lang.acl.ACLMessage;
import utils.Utils;
import java.util.Scanner;

public class ClientBehaviour extends CyclicBehaviour{
 
    public String paradasO[] = {"P1", "P2", "P3", "P4", "P5", "P6", "P7", "P8","P9","P11", "P12"};
    public String paradasD[] = {"P2", "P3", "P4", "P5", "P6", "P7", "P8","P9", "P10", "P12", "P13"};
    public String respuestas[] = {"S", "N"}; // Respuestas válidas para el cliente.
    public int step = 0; // 0 -> enviar mensaje, 1-> recibir mensaje, 2-> borrar
    public Scanner scanner = new Scanner(System.in);
    
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
            break;
            
            case 2:
                myAgent.doDelete();
                scanner.close();
        }
    }



    public String pedirDatos()
    {
        String content;
        String pOrigen, pDestino, sHoraSalida;
        Double hSalida;

        int paradaOrigen, paradaDestino;

        do{
            System.out.println("Introduzca la parada de origen: " + imprimirArray(paradasO));
            pOrigen = System.console().readLine();
         }while(Utils.comparaCadenas(pOrigen, paradasO) != 0); // Si la parada no existe, se vuelve a pedir.
         paradaOrigen = Integer.parseInt(pOrigen.substring(1));// Se quita la P del String.
        do{
            System.out.println("Introduzca la para de destino: " + imprimirArray(paradasD));
            pDestino = System.console().readLine();
            paradaDestino = Integer.parseInt(pDestino.substring(1));// Se quita la P del String.
        }while(Utils.comparaCadenas(pDestino, paradasD) != 0 || paradaOrigen > paradaDestino ); // Si la parada no existe, se vuelve a pedir.

        do{
            System.out.println("Introuduce la hora a la que quieres salir ");
        }while (!scanner.hasNextDouble()); // Si la hora no es un double, se vuelve a pedir.
            hSalida = scanner.nextDouble();
            sHoraSalida = Double.toString(hSalida); // Se convierte a String para poder enviarlo por ACLMessage.    
        
        pOrigen = pOrigen.substring(1);
        pDestino = pDestino.substring(1);
        content = pOrigen + ":" + pDestino + ":" + sHoraSalida; // Se crea el contenido del mensaje.

        return content;
    }
    public String imprimirArray(String array[])
    {
        String opciones = "[ ";
        for (int i = 0; i < array.length; i ++)
        {
            if( i == array.length - 1)
            {
                opciones = opciones + array[i];
                break;
            }
            opciones = opciones + array[i] + ",";
        }
        opciones = opciones + " ]";
        return opciones;
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
                System.out.println("======================================");
                System.out.println("RUTA CALCULADA");
                System.out.println("======================================");
                System.out.println(""+msg.getContent());
                System.out.println("======================================");
            }
            else
            {
                System.out.println("No se ha encontrado ninguna ruta");
            }
            step = 2;
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

    

}
