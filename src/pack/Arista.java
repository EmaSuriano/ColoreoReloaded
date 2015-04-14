package pack;

import java.util.Comparator;

public class Arista implements Comparable<Arista>{
	private Nodo nodo1;
	private Nodo nodo2;
	private int valor;

	public Arista(Nodo nodo1, Nodo nodo2, int valor) {
		this.nodo1 = nodo1;
		this.nodo2 = nodo2;
		this.valor = valor;
	}
	
	public void copyArista(Arista arista){
		this.nodo1= arista.nodo1;
		this.nodo2= arista.nodo2;
		this.valor= arista.valor;
	}

	public Nodo getNodo1() {
		return nodo1;
	}

	public void setNodo1(Nodo nodo1) {
		this.nodo1 = nodo1;
	}

	public Nodo getNodo2() {
		return nodo2;
	}

	public void setNodo2(Nodo nodo2) {
		this.nodo2 = nodo2;
	}

	public int getValor() {
		return valor;
	}

	public void setValor(int valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Arista [" + nodo1.getIdNodo() + ", " + nodo2.getIdNodo() + ", valor=" + valor + "]";
	}



	public static final Comparator<Arista> sorter = new Comparator<Arista>() {
		@Override
		public int compare(Arista arg0, Arista arg1) {
			if (arg0.valor > arg1.valor)
				return 1;
			return -1;
		}
	};

	@Override
	public int compareTo(Arista o) {
		return this.valor - o.valor;
	}
}
