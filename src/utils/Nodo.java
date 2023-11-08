package utils;
import java.util.ArrayList;

public class Nodo {
	    public int id;
        public int tiempo;
        public ArrayList<Nodo> vecinos;
        public ArrayList<Double> autobuses;

        public Nodo(int id, int tiempo, ArrayList<Nodo> vecinos ) {
            this.id = id;
            this.tiempo = tiempo;
            this.vecinos = new ArrayList<>();
            this.autobuses = new ArrayList<>();
        }

        public Double tiempoDeEspera(double horaActual)
        {
            double hora = 0;
            int i = 0;
            while (horaActual > this.autobuses.get(i))
            {
                i++;
            }
            hora = this.autobuses.get(i);
            if(hora == horaActual){
                return 0.0;
            }
            else
            {
                return (hora - horaActual);
            }

        }




        
}

