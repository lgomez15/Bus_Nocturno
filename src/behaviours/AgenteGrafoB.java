package behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;


public class AgenteGrafoB extends CyclicBehaviour {

	private String ruta;
	private int origen;
	private int destino;
	private int tiempo;

	public AgenteGrafoB(Agent a, String ruta) {
		super(a);
		this.ruta = ruta;
	}

	public void action(){

		//transform string of ruta (A:B:C) to int origen, int destino, int tiempo
		String[] parts = this.ruta.split(":");
		this.origen = Integer.parseInt(parts[0]);
		this.destino = Integer.parseInt(parts[1]);
		this.tiempo = Integer.parseInt(parts[2]);



	}
}
