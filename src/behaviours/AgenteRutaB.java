package behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import utils.Grafo;
import utils.Nodo;
import utils.Ruta;

import java.util.*;

public class AgenteRutaB extends OneShotBehaviour {

	public String request;
	public String uuid;
	public int step = 0;
	public Grafo grafo;
	public Ruta rutaOptima;
	public String response;

	public AgenteRutaB(Agent a,String uuid) {
		super(a);
		this.uuid = uuid;
	}


	public void action(){
		
		while(step != 3)
		{
			switch (step) {
				case 0:
					ACLMessage msg = myAgent.receive();
					if(msg != null)
					{
						System.out.println("Mensaje recibido de:" + msg.getSender());
						request = msg.getContent();
						uuid = msg.getReplyWith();
						step = 1;
					}
					else{
						block();
					}
				break;
				case 1:
					System.out.println("Imprimiendo datos:");
					String partes[] = request.split("/n");
					String ruta[] = partes[0].split(":");
					int o = Integer.parseInt(ruta[0]);
					int d = Integer.parseInt(ruta[1]);
					Double tiempo = Double.parseDouble(ruta[2]);



					String[] l1Info = partes[1].split("/");
					String[] l2Info = partes[2].split("/");
					
					Double[] autobusesL1 = cargarBuses(l1Info[1]);
					Double[] autobusesL2 = cargarBuses(l2Info[1]);

					grafo = construirGrafo(l1Info[0], l2Info[0]);
					calcularTiempos(grafo, autobusesL1, autobusesL2);

					Nodo origen = new Nodo(d, o, null);
					Nodo destino = new Nodo(d,o,null);

					for(Nodo n : grafo.lineas.get(1))
					{
						if(n.id == o)
							origen = n;
						if(n.id == d) 
							destino=n;
					}
					for(Nodo n : grafo.lineas.get(2))
					{
						if(n.id == o)
							origen = n;
						if(n.id == d) 
							destino=n;
					}

					response = "Origen:" + origen.id + "\n";
					response += "Destino:" + destino.id + "\n";;

					rutaOptima = rutaOptimRuta(grafo, origen, destino, tiempo);
					response += "Ruta:" + "\n";
					response += "Hora de salida:"+(tiempo+origen.tiempoDeEspera(tiempo))+ "\n";
					for(Nodo nodo : rutaOptima.nodos)
					{
						response += "Parada:"+nodo.id + "\n";
					}
					double horaLlegada = rutaOptima.tiempo;
					response += "Hora de llegada:" + horaLlegada;
					step = 2;

									
				break;
			
				case 2:
					ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
					reply.setContent(response);
					reply.setInReplyTo(uuid);
					reply.addReceiver(new jade.core.AID("agenteGrafo", jade.core.AID.ISLOCALNAME));
					myAgent.send(reply);
					step=3;
				break;
			}

		}
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

	public static Ruta elegirRuta(Ruta r1, Ruta r2)
    {   
        return r1.tiempo < r2.tiempo ? r1 : r2;    
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


	public Grafo construirGrafo(String l1, String l2)
	{
		Grafo g = new Grafo();
		String paradasL1[] = l1.split(",");
		String paradasL2[] = l2.split(",");
		String nodoInfo[];
		List<Nodo> linea1 = new ArrayList<>();
		List<Nodo> linea2 = new ArrayList<>();
		int i;

		for(String parada : paradasL1)
		{
			nodoInfo = parada.split(":");
			linea1.add(new Nodo(Integer.parseInt(nodoInfo[0]), Integer.parseInt(nodoInfo[1]), null));
		}
		
		i = 0;
		for(Nodo n: linea1)
		{
			i++;
			if(i < linea1.size())
			{
				n.vecinos.add(linea1.get(i));
			}
		}

		g.agregarLinea(1, linea1);
		
		for(String parada : paradasL2)
		{
			nodoInfo = parada.split(":");
			linea2.add(new Nodo(Integer.parseInt(nodoInfo[0]), Integer.parseInt(nodoInfo[1]), null));
		}
		
		i = 0;
		for(Nodo n: linea2)
		{
			i++;
			if(i < linea2.size())
			{
				n.vecinos.add(linea2.get(i));
			}
		}

		g.agregarLinea(2, linea2);

		g.lineas.get(1).get(3).vecinos.add(g.lineas.get(2).get(2));
        g.lineas.get(1).get(8).vecinos.add(g.lineas.get(2).get(4));

        g.lineas.get(2).get(1).vecinos.add(g.lineas.get(1).get(4));
        g.lineas.get(2).get(3).vecinos.add(g.lineas.get(1).get(9));
		
		return g;

		
}

	public Double[] cargarBuses(String info)
	{
		String[] aux = info.split(",");
		Double[] autobuses = new Double[aux.length];
		for(int i=0; i < aux.length;i++ )
		{
			autobuses[i] = ( Double.parseDouble(aux[i]));
		}
		return autobuses;
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


						// System.out.println("||||||||||||||||||||||||||||||||");
					// System.out.println("Linea 1");
					// for(Nodo parada : grafo.lineas.get(1))
					// {
					// 	System.out.println("----------------------------");
					// 	System.out.println("Parada:" + parada.id + "Peso:" + parada.tiempo);
					// 	System.out.println("Vecino:" + parada.vecinos.get(0).id);

					// 	for(Double b: parada.autobuses)
					// 	{
					// 		System.out.println(b);
					// 	}

					// 	System.out.println("----------------------------");
					// }
					// System.out.println("||||||||||||||||||||||||||||||||");
					// System.out.println("Linea 2");
					// for(Nodo parada : grafo.lineas.get(2))
					// {
					// 	System.out.println("----------------------------");
					// 	System.out.println("Parada:" + parada.id + "Peso:" + parada.tiempo);
					// 	System.out.println("Vecino:" + parada.vecinos.get(0).id);

					// 	for(Double b: parada.autobuses)
					// 	{
					// 		System.out.println(b);
					// 	}

					// 	System.out.println("----------------------------");
					// }
					// System.out.println("||||||||||||||||||||||||||||||||");
}
