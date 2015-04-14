package pack;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;


public class UniqueQueue {
	
	private Queue<Nodo> queue;
	private int[] distances;
	
	@SuppressWarnings("unused")
	private UniqueQueue(){}
	public UniqueQueue(Nodo nodoInicial, int size) {
		this.distances = new int[size];
		this.queue = new LinkedList<Nodo>();
		this.initialize();
		this.distances[getElementId(nodoInicial)] = 0;
	}
	
	/**
	 * Solo agrega un elemento si este no se encontraba previamente agregado
	 * @param element
	 * @param distance
	 */
	public boolean add(Nodo element, int distance) {
		if(this.distances[getElementId(element)] == Integer.MAX_VALUE) {
			this.queue.add(element);
			this.distances[getElementId(element)] = distance;
			return true;
		}
		return false;
	}
	
	public void addAll(List<Nodo> nodosAdyacentes, int distanciaAlInicial) {
		for(Nodo nodo : nodosAdyacentes)
			this.add(nodo, distanciaAlInicial);
	}
	
	/**
	 * Se inicializa el vector en INFINITO
	 */
	private void initialize() {
		for (int i = 0; i < this.distances.length; i++) {
			distances[i] = Integer.MAX_VALUE;
		}
	}
	
	public Nodo next() {
		if(this.queue.isEmpty())
			return null;
		return this.queue.poll();
	}

	private int getElementId(Nodo element) {
		return Integer.parseInt(element.getIdNodo()) - 1;
	}
	
	public int getDistance(Nodo element) {
		return this.distances[getElementId(element)];
	}
	
	public int[] getCurrentDistances() {
		return this.distances;
	}
	public boolean hasNext() {
		return !this.queue.isEmpty();
	}
	public void addAll(List<Nodo> nodosAdyacentesAlActual, Nodo currentNode) {
		for(Nodo nodo : nodosAdyacentesAlActual)
			this.add(nodo, currentNode);
	}
	private void add(Nodo nodo, Nodo currentNode) {
		if(this.distances[getElementId(nodo)] == Integer.MAX_VALUE) {
			this.queue.add(nodo);
			this.distances[getElementId(nodo)] = this.distances[getElementId(currentNode)]+1;
		}
	}

}
