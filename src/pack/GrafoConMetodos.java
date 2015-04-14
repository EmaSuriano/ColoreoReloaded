package pack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrafoConMetodos extends GrafoDirigidoPonderado {

	public GrafoConMetodos(File file) {
		super(file);
	}

	/**
	 * Devuelve una matriz de adyacencia que nos dice si se puede llegar de un nodo a otro, DE TODOS! PD: Tiene la misma complejidad computaciones y da menos informacion que floyd, DESCARTADO!!
	 * 
	 * @return
	 */
	public MatrizAsimetrica warshall() {
		MatrizAsimetrica matrizWarshall = matrizAdyacencia.convertirMatrizBoolean();
		for (int i = 0; i < matrizAdyacencia.getLength(); i++)
			for (int j = 0; j < matrizAdyacencia.getLength(); j++)
				for (int j2 = 0; j2 < matrizAdyacencia.getLength(); j2++)
					if (j != j2 && j != i && j2 != i)
						((MatrizAsimetrica) matrizWarshall).setValueAt(j, j2, verificar(i, j, j2, (MatrizAsimetrica) matrizWarshall));
		return matrizWarshall;
	}

	private int verificar(int i, int j, int j2, MatrizAsimetrica matrizWarshall) { // usado en warshall
		if ((matrizWarshall.getValueAt(j, j2) + (matrizWarshall.getValueAt(i, j2) * matrizWarshall.getValueAt(j, i))) > 0)
			return 1;
		return 0;
	}

	/**
	 * Devuelve una matriz de adyacencia que nos dice a que distancia esta de un nodo de otro, DE TODOS!
	 */
	public MatrizAsimetrica floyd() {
		MatrizAsimetrica matrizFloyd = matrizAdyacencia.borrarDiagonalPrincipal();
		int size = matrizFloyd.getLength();
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				for (int j2 = 0; j2 < size; j2++)
					((MatrizAsimetrica) matrizFloyd).setValueAt(j, j2, calcularMinimo(matrizFloyd, i, j, j2));
		return matrizFloyd;
	}

	private int calcularMinimo(MatrizAsimetrica matriz, int pibote, int x, int y) { // usado en floyd
		int calculo1 = ((MatrizAsimetrica) matriz).getValueAt(x, y);
		if (((MatrizAsimetrica) matriz).getValueAt(pibote, y) == Integer.MAX_VALUE || ((MatrizAsimetrica) matriz).getValueAt(x, pibote) == Integer.MAX_VALUE)
			return calculo1;
		int calculo2 = ((MatrizAsimetrica) matriz).getValueAt(x, pibote) + ((MatrizAsimetrica) matriz).getValueAt(pibote, y);
		if (calculo2 < calculo1)
			return calculo2;
		return calculo1;
	}

	/**
	 * Devuelve el vector de costes minimos, o el vector de camino (como llegar a tener esos costos) Testeado y anda de PUTA MADRE!
	 * 
	 * @param nodo
	 * @return
	 */
	public int[] dijkstra(Nodo nodo) {
		List<Integer> usados = new ArrayList<Integer>();
		List<Integer> pendientes = new ArrayList<Integer>();
		int[] vectorResultado = new int[nodos.size()];
		int[] vectorCamino = new int[nodos.size()];

		usados.add(nodos.indexOf(nodo)); // agrego el nodo existente a la lista de usados
		for (int i = 0; i < nodos.size(); i++)
			if (nodos.get(i) != nodo) { // lleno el vector y la lista de pendientes
				vectorResultado[i] = ((MatrizAsimetrica) matrizAdyacencia).getValueAt(nodos.indexOf(nodo), i);
				pendientes.add(nodos.indexOf(nodos.get(i)));
			}

		while (!pendientes.isEmpty()) {
			int pibote = buscarMin(vectorResultado, pendientes); // busco en el vector el minimo, en relacion con la lista
			usados.add(pibote); // agrego el nodo que salio nominado a los usados
			for (Integer numero : pendientes)
				vectorResultado[numero] = reemplazarMinimo(vectorResultado, vectorCamino, pibote, numero);
		}
		return vectorResultado;
	}

	private int reemplazarMinimo(int[] vectorResultado, int[] vectorCamino, int pibote, int numero) { // usado en dijstra
		int calculo1 = vectorResultado[numero];
		int calculo2 = ((MatrizAsimetrica) matrizAdyacencia).getValueAt(pibote, numero);
		if (calculo2 == Integer.MAX_VALUE)
			return calculo1;
		calculo2 += vectorResultado[pibote];
		if (calculo2 < calculo1) { // si el camino dio menor
			vectorCamino[numero] = pibote;
			return calculo2;
		}
		return calculo1;
	}

	private int buscarMin(int[] vector, List<Integer> lista) { // usado en dijkstra
		int aux = lista.get(0); // contenido de un elemento de la lista
		int cont = 0;
		for (int i = 1; i < lista.size(); i++) {
			if (vector[lista.get(i)] < vector[aux]) {
				cont = i; // obtengo la posicion dentro de la lista
				aux = lista.get(i); // obtengo id dentro de la lista, para
									// buscarlo dentro del vector
			}
		}
		lista.remove(cont);
		return aux;
	}

	public Arbol prim() {
		Arbol arbol = new Arbol(nodos.get(0));
		List<Nodo> nodosSinUsar = new ArrayList<Nodo>(nodos);
		nodosSinUsar.remove(nodos.get(0));
		List<Nodo> nodosAdyacentes = new ArrayList<Nodo>();
		List<Nodo> nodosNoAdyacentes = new ArrayList<Nodo>();
		nodosNoAdyacentes = getNodosNoAdyacentes(arbol.getNodosUsados());
		nodosAdyacentes = getNodosAdyacentes(arbol.getNodosUsados(), nodosSinUsar);
		while (arbol.getNodosUsados().size() != nodos.size() || (nodosAdyacentes.size() != 0 && nodosNoAdyacentes.size() != 0)) {
			Arista arista = obtenerMenorArista(arbol.getNodosUsados(), nodosAdyacentes);
			arbol.getNodosUsados().add(arista.getNodo2());
			nodosSinUsar.remove(arista.getNodo2());
			arbol.getAristas().add(arista);
			nodosNoAdyacentes = getNodosNoAdyacentes(arbol.getNodosUsados());
			nodosAdyacentes = getNodosAdyacentes(arbol.getNodosUsados(), nodosSinUsar);
		}
		return arbol;
	}

	public Arista obtenerMenorArista(List<Nodo> nodosUsados, List<Nodo> nodosAdyacentes) {
		Arista arista = new Arista(nodos.get(0), nodos.get(0), Integer.MAX_VALUE);
		for (Nodo nodoOrigen : nodosUsados) {
			for (Nodo nodoDestino : nodosAdyacentes) {
				if (isConnectedTo(nodoOrigen, nodoDestino)) {
					Arista aux = new Arista(nodoOrigen, nodoDestino, valueOfConnection(nodoOrigen, nodoDestino));
					if (arista.compareTo(aux) > 0)
						arista.copyArista(aux);
				}
			}
		}
		return arista;
	}
	
	public List<Nodo> getNodosAdyacentes(Nodo nodo) {
		List<Nodo> adyacentes = new ArrayList<Nodo>();
		for (Nodo nodoAux : nodos) {
			if(nodoAux != nodo && isConnectedTo(nodo, nodoAux))
				adyacentes.add(nodoAux);
		}
		return adyacentes;
	}


	public List<Nodo> getNodosAdyacentes(List<Nodo> nodosUsados, List<Nodo> nodosSinUsar) {
		List<Nodo> resultado = new ArrayList<Nodo>();
		for (Nodo nodo : nodosUsados) {
			for (Nodo nodoSinUso : nodosSinUsar) {
				if(isConnectedTo(nodo, nodoSinUso))
						resultado.add(nodoSinUso);
				}
		}
		return resultado;
	}
	
	public List<Nodo> getNodosNoAdyacentes(List<Nodo> nodosUsados){
		List<Nodo> resultado = new ArrayList<Nodo>();
		for (Nodo nodo : nodosUsados) {
			for (Nodo nodoTotal : nodos) {
				if(!isConnectedTo(nodo, nodoTotal) && nodo != nodoTotal)
						resultado.add(nodoTotal);
				}
		}
		return resultado;
	}

	public int[] BFS(Nodo nodo) {
		UniqueQueue nodosAdyacentesPendientes = new UniqueQueue(nodo, this.nodos.size());
		Nodo currentNode = nodo;
		while(currentNode != null) {
			List<Nodo> nodosAdyacentesAlActual = getNodosAdyacentes(currentNode);
			nodosAdyacentesPendientes.addAll(nodosAdyacentesAlActual, currentNode);
			System.out.println(currentNode.getIdNodo().toString());
			currentNode = nodosAdyacentesPendientes.next();
		}
		return nodosAdyacentesPendientes.getCurrentDistances();
	}


	public static void main(String[] args) throws Exception {
		// GrafoConMetodos.generadorProbabilidad(5, 50, new File("src/pack/grafo.in"));
		GrafoConMetodos grafo = new GrafoConMetodos(new File("src/pack/grafo.in"));
		grafo.print();
//		 System.out.println("DIJKSTRA-------");
//		 int aux[] = grafo.dijkstra(grafo.nodos.get(0));
//		 for (int i = 0; i < aux.length; i++)
//		 System.out.print(aux[i] + " ");
		 
		 int aux[] =grafo.BFS(grafo.nodos.get(0));
		 for (int i = 0; i < aux.length; i++)
			 System.out.print(aux[i] + " ");
		// System.out.println("\nFLOYD--------");
		// MatrizAsimetrica matriz = grafo.floyd();
		// matriz.showMatrix();
		// System.out.println("\nWARSHALL--------");
		// matriz = grafo.warshall();
		// matriz.showMatrix();
		// System.out.println("PRIM-----------");
		// Arbol arbol= grafo.prim();
		// for (Arista arista: arbol.getAristas()) {
		// System.out.println(arista.toString());
		// }
	}

}
