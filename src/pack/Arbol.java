  package pack;

import java.util.ArrayList;
import java.util.List;

public class Arbol{
	private int costo;
	private List<Arista> aristas;
	private List<Nodo> nodosUsados;

	public Arbol(Nodo nodo) {
		aristas= new ArrayList<Arista>();
		nodosUsados= new ArrayList<Nodo>();
		nodosUsados.add(nodo);
	}
	
	public void showConnections(){
		for (int i = 0; i < aristas.size(); i++) {
			System.out.println(aristas.toString());
		}
	}

	public int getCosto() {
		return costo;
	}

	public void setCosto(int costo) {
		this.costo = costo;
	}

	public List<Arista> getAristas() {
		return aristas;
	}

	public void setAristas(List<Arista> aristas) {
		this.aristas = aristas;
	}

	public List<Nodo> getNodosUsados() {
		return nodosUsados;
	}

	public void setNodosUsados(List<Nodo> nodosUsados) {
		this.nodosUsados = nodosUsados;
	}
	
	
}
