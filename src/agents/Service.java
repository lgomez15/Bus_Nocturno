package agents;

import behaviours.ServiceBehaviour;
import jade.core.Agent; 
import jade.domain.DFService; 
import jade.domain.FIPAAgentManagement.DFAgentDescription; 
import jade.domain.FIPAAgentManagement.ServiceDescription; 
import jade.domain.*;

public class Service extends Agent{
    
    protected void setup()
    {
        /*REGISTRO SERVICIO */
        /*1- Creamos el agente */
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(getAID());
        /*2- Descripción del servicio */
        ServiceDescription sd = new ServiceDescription();
        sd.setType("Servicio"); // Tipo de servicio.
        sd.setName("Servicio"); // Nombre del servicio.
        dfd.addServices(sd); // Añade el servicio a la descripción del agente.
        /*Registramos elagente servicio */
        try{
            DFService.register(this, dfd);
        }catch(FIPAException fe){
            System.out.println("Error al registrar el servicio " + fe);
        }


        this.addBehaviour(new ServiceBehaviour());
    }

    protected void takeDown()
    {
        try{
            DFService.deregister(this);
        }catch(FIPAException fe){
            System.out.println("Error al desregistrar el servicio " + fe);
        }
    }



}
