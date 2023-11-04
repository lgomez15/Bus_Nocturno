package utils;
import java.util.ArrayList;
import java.util.List;

public class Grafo {
    private int numNodos;
    private List<List<Parada>> listaAdyacencia;

    public Grafo(int numNodos) {
        this.numNodos = numNodos;
        listaAdyacencia = new ArrayList<>(numNodos);

        for (int i = 0; i < numNodos; i++) {
            listaAdyacencia.add(new ArrayList<>());
        }
    }

    public void agregarArista(Parada origen, Parada destino) {
        listaAdyacencia.get(origen.getNumero()).add(destino);
        listaAdyacencia.get(destino.getNumero()).add(origen);
    }

    public void imprimirGrafo() {
        for (int i = 0; i < numNodos; i++) {
            System.out.print("Parada " + i + " está conectada a: ");
            for (Parada parada : listaAdyacencia.get(i)) {
                System.out.print(parada.getNumero() + " (Peso: " + parada.getPeso() + ") ");
            }
            System.out.println();
        }
    }
}