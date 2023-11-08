package utils;
import java.util.ArrayList;

public class Ruta {
	
	public ArrayList<Nodo> nodos;
	public double tiempo;

	public Ruta() {
		nodos = new ArrayList<Nodo>();
		tiempo = 0;
	}

	public boolean tengoDestino(Nodo destino)
	{
		for(Nodo nodo : this.nodos)
		{
			if(nodo.id == destino.id)
				return true;
		}
		return false;
	}

	public void marchaAtras(Nodo destino)
	{
		for(int i = (this.nodos.size()-1); i>=0 ;i --)
		{
			if(this.nodos.get(i).id != destino.id)
			{
				tiempo = tiempo - this.nodos.get(i).tiempo;
				this.nodos.remove(i);
			}else
				break;
		}
	}

}
