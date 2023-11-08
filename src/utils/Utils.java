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
        int iguales = 1;
        for ( i = 0; i < array.length; i++)
        {
            if(cadena.equalsIgnoreCase(array[i]))
            {
                iguales = 0;
                break;
            }
        }
        return iguales;
    }

    
    public static Ruta rutaOptimRuta(Grafo g, Nodo origen, Nodo destino, double horaSalida)
    {
        Ruta ruta = new Ruta();
        Ruta r1 = new Ruta();
        Ruta r2 = new Ruta();

        horaSalida += origen.tiempoDeEspera(horaSalida);

        ruta.tiempo = horaSalida;
        ruta.nodos.add(origen);
        
        while(ruta.nodos.get(ruta.nodos.size() - 1).id != destino.id)
        {
            Nodo actual = ruta.nodos.get(ruta.nodos.size() - 1);
            if(actual.vecinos.size() > 1 && actual.vecinos.size()!=0)
            {
                if(actual.id == 4)
                {
                    Ruta masRapida;
                    r1 = recorrerNodosOptimo(actual.vecinos.get(0), r1);
                    r1.tiempo += actual.tiempoDeEspera(ruta.tiempo);
                    r2 = recorrerNodosOptimo(actual.vecinos.get(1), r2);
                    r2.tiempo += actual.tiempoDeEspera(ruta.tiempo);

                    if(r1.tengoDestino(destino) && r2.tengoDestino(destino))
                    {
                        masRapida = elegirRuta(r1, r2);
                        ruta.nodos.addAll(masRapida.nodos);
                        ruta.tiempo += masRapida.tiempo;
                    }
                    else if(r1.tengoDestino(destino))
                    {
                        r1.marchaAtras(destino);
                        ruta.nodos.addAll(r1.nodos);
                        ruta.tiempo += r1.tiempo;
                    }
                    else if(r2.tengoDestino(destino))
                    {
                        r2.marchaAtras(destino);
                        ruta.nodos.addAll(r2.nodos);
                        ruta.tiempo += r2.tiempo;
                    }else
                    {
                        masRapida = elegirRuta(r1, r2);
                        ruta.nodos.addAll(masRapida.nodos);
                        ruta.tiempo += masRapida.tiempo;
                    }
                }
                if (actual.id == 9) {
                    ruta.tiempo += destino.tiempo;
                    ruta.nodos.add(destino);                 
                }
            }
            else if(actual.vecinos.size() == 1)
            {
                ruta.tiempo += actual.vecinos.get(0).tiempo;
                ruta.nodos.add(actual.vecinos.get(0));
            }
        }

        return ruta;
    }

    public static Ruta recorrerNodosOptimo(Nodo nodo, Ruta ruta)
    {
        if(nodo.vecinos.size() != 1)
        {
            if(nodo.id == 9)
            {   
                ruta.tiempo += nodo.tiempo;
                ruta.nodos.add(nodo);
                return ruta;
            }
            return ruta;
        }
        else
        {    
            ruta.nodos.add(nodo);
            ruta.tiempo += nodo.tiempo;
            return recorrerNodosOptimo(nodo.vecinos.get(0), ruta);
        }
        
    }

    public static int recorrerNodos(Nodo origen, Nodo destino, int tiempo)
    {
        if(origen.id == destino.id)
        {
            return tiempo + destino.tiempo;
        }
        else
        {    
            tiempo += origen.tiempo;
            return recorrerNodos(origen.vecinos.get(0), destino, tiempo);
        }
        
    }

    public static void calcularTiempos(Grafo g, Double[] b1, Double[] b2) 
    {

        for (int i =0; i< g.lineas.size(); i++)
        {
            for(int j=0; j < g.lineas.get(i+1).size();j++)
            {
                for(int k=0; k< b1.length; k++)
                {
                    if(i==0)
                        g.lineas.get(i+1).get(j).autobuses.add(b1[k] +recorrerNodos(g.lineas.get(i+1).get(0),g.lineas.get(i+1).get(j), 0));
                    else if (i==1)
                        g.lineas.get(i+1).get(j).autobuses.add(b2[k] +recorrerNodos(g.lineas.get(i+1).get(0),g.lineas.get(i+1).get(j), 0));
                }
            }
        }
    }

    public static Ruta elegirRuta(Ruta r1, Ruta r2)
    {   
        return r1.tiempo < r2.tiempo ? r1 : r2;    
    }
}