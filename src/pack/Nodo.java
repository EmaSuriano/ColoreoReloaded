package pack;

import java.util.Comparator;

public class Nodo{
	private int color;
	private String idNodo;
	private int cantAristas;

	public Nodo(String idNodo) {
		this.color = 0;
		this.idNodo = idNodo;
		this.cantAristas=0;
	}
	
	public Nodo(String idNodo, Character color) {
		this.color = color;
		this.idNodo = idNodo;
	}
	
	public int getCanAristas(){
		return cantAristas;
	}

	public void aumentarCantAristas(){
		cantAristas ++;
	}
	
	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getIdNodo() {
		return idNodo;
	}
	
	public static final Comparator <Nodo> welshPowellSorter = new Comparator<Nodo>() {
		@Override
		public int compare(Nodo arg0, Nodo arg1){
			if(arg0.cantAristas < arg1.cantAristas)
				return 1;
			return -1;
		}
	};
	
	public static final Comparator <Nodo> matulaSorter = new Comparator<Nodo>() {
		@Override
		public int compare(Nodo arg0, Nodo arg1){
			if(arg0.cantAristas >= arg1.cantAristas)
				return 1;
			return -1;
		}
	};
}