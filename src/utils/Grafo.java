package utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Grafo {
		public Map<Integer, List<Nodo>> lineas;

        public Grafo() {
            lineas = new HashMap<>();
        }

        public void agregarLinea(int numero, List<Nodo> nodos) {
            lineas.put(numero, nodos);
        }

        public List<Nodo> obtenerLinea(int numero) {
            return lineas.get(numero);
        }

}
