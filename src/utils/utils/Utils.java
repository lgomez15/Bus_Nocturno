package utils;

import jade.core.Agent;
import jade.core.AID;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class Utils {

    public static AID[] searchAgents(Agent a, String service) {

        AID[] agents = null;
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription sd = new ServiceDescription();
        sd.setType(service);
        template.addServices(sd);

        try {
            DFAgentDescription[] result = DFService.search(a, template);
            agents = new AID[result.length];
            for (int i = 0; i < result.length; ++i) {
                agents[i] = result[i].getName();
            }
        } catch (FIPAException fe) {
            fe.printStackTrace();
        }
        return agents;
    }

    public static int comparaCadenas(String cadena, String array[])
    {
        int i = 0;
        for ( i = 0; i < array.length; i++)
        {
            if(cadena.equalsIgnoreCase(array[i]))
            {
                return 0;
            }
        }
        return 1;
    }


}
