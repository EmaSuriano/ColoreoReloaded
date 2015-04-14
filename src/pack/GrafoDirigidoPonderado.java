package pack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class GrafoDirigidoPonderado {
	protected static final String PATH_OUT = "src/pack/coloreado.out";
	protected static final int MAX_NUMBER = 100;
	protected List<Nodo> nodos;
	protected int cantAristas;
	protected int porcentajeAdyacencia;
	protected int gradoMayor, gradoMenor;
	protected MatrizAsimetrica matrizAdyacencia;

	public void print() {
		matrizAdyacencia.showMatrix();
	}

	public boolean isConnectedTo(Nodo n1, Nodo n2) {
		if (matrizAdyacencia.getValueAt(Integer.valueOf(n1.getIdNodo()) - 1, Integer.valueOf(n2.getIdNodo()) - 1) == Integer.MAX_VALUE
				&& matrizAdyacencia.getValueAt(Integer.valueOf(n1.getIdNodo()) - 1, Integer.valueOf(n2.getIdNodo()) - 1) == Integer.MAX_VALUE)
			return false;
		return true;
	}
	
	public int valueOfConnection(Nodo n1, Nodo n2){
		if(matrizAdyacencia.getValueAt(Integer.valueOf(n1.getIdNodo()) - 1, Integer.valueOf(n2.getIdNodo()) - 1) != Integer.MAX_VALUE)
			return matrizAdyacencia.getValueAt(Integer.valueOf(n1.getIdNodo()) - 1, Integer.valueOf(n2.getIdNodo()) - 1);
		else 
			return matrizAdyacencia.getValueAt(Integer.valueOf(n2.getIdNodo()) - 1, Integer.valueOf(n1.getIdNodo()) - 1);
	}

	public GrafoDirigidoPonderado(File file) {
		FileReader fr = null;
		BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			String line = br.readLine();

			String[] split = line.split(" ");

			nodos = new ArrayList<Nodo>();
			for (int i = 0; i < Integer.valueOf(split[0]); i++)
				nodos.add(new Nodo(Integer.toString(i + 1)));
			matrizAdyacencia = new MatrizAsimetrica(nodos.size());
//			cantAristas = Integer.valueOf(split[1]);
//			porcentajeAdyacencia = Integer.valueOf(split[2]);
//			gradoMayor = Integer.valueOf(split[3]);
//			gradoMayor = Integer.valueOf(split[4]);

			while ((line = br.readLine()) != null) {
				split = line.split(" ");
				addConnection(nodos.get(Integer.valueOf(split[0])), nodos.get(Integer.valueOf(split[1])), Integer.valueOf(split[2]));
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public void addConnection(Nodo n1, Nodo n2, int val) {
		n1.aumentarCantAristas();
		n2.aumentarCantAristas();
		matrizAdyacencia.setValueAt(nodos.indexOf(n1), nodos.indexOf(n2), val);
	}

	public GrafoDirigidoPonderado(int cantNodos) {
		nodos = new ArrayList<Nodo>();
		matrizAdyacencia = new MatrizAsimetrica(cantNodos);
		cantAristas = 0;
		porcentajeAdyacencia = 0;
		gradoMayor = 0;
		gradoMenor = 0;
	}

	public static void generadorProbabilidad(int cantNodos, int probabilidad, File file) {
		GrafoDirigidoPonderado grafoAux = new GrafoDirigidoPonderado(cantNodos);
		for (int i = 0; i < cantNodos; i++)
			grafoAux.nodos.add(new Nodo(Integer.toString(i + 1)));
		for (int i = 0; i < cantNodos; i++) {
			for (int j = 0; j < cantNodos; j++) {
				if(i!=j){
					if (randomConnection(probabilidad) == true)
						grafoAux.matrizAdyacencia.setValueAt(i, j, (int) (Math.random()*100 % MAX_NUMBER)+1);
					if (grafoAux.matrizAdyacencia.getValueAt(i, j) != Integer.MAX_VALUE)
						grafoAux.cantAristas++;					
				}
			}
		}
		grafoAux.cantAristas += grafoAux.checkNoConnections();
		grafoAux.fillGraph();
		grafoAux.print();
		grafoAux.saveGraph(file);
	}

	public static void generadorPorcentaje(int cantNodos, int porcentaje, File file) {
		GrafoDirigidoPonderado grafoAux = new GrafoDirigidoPonderado(cantNodos);
		for (int i = 0; i < cantNodos; i++)
			grafoAux.nodos.add(new Nodo(Integer.toString(i + 1)));
		int cantConnections = (int) Math.ceil((double) grafoAux.matrizAdyacencia.getMaxConnection() * porcentaje / 100); // round
		int i, j;
		while (grafoAux.cantAristas != cantConnections) {
			i = (int) (Math.random() * (cantNodos - 1));
			j = (int) (Math.random() * (cantNodos - i - 1)) + i + 1;
			if (grafoAux.matrizAdyacencia.getValueAt(i, j) != Integer.MAX_VALUE) {
				grafoAux.matrizAdyacencia.setValueAt(i, j, (int) (Math.random()*100 % MAX_NUMBER)+1);
				grafoAux.cantAristas++;
			}
		}
		grafoAux.print();
		grafoAux.cantAristas += grafoAux.checkNoConnections();
		grafoAux.fillGraph();
		grafoAux.print();
		grafoAux.saveGraph(file);
	}

	public static void generadorRegularesGrado(int cantNodos, int grado, File file) throws Exception {
		if (grado < 2 && grado > cantNodos) // validacion 1
			throw new Exception("Grado incorrecto");
		if (cantNodos % 2 == 1 && grado % 2 == 1) // validacion 2
			throw new Exception("Grado imposible!");
		GrafoDirigidoPonderado grafoAux = new GrafoDirigidoPonderado(cantNodos);
		for (int i = 0; i < cantNodos; i++)
			grafoAux.nodos.add(new Nodo(Integer.toString(i + 1)));

		if (grado % 2 == 1) { // lo hago grado par
			int j;
			for (int i = 0; i < (cantNodos / 2); i++) {
				j = i + (cantNodos / 2);
				grafoAux.matrizAdyacencia.setValueAt(i, j, (int) (Math.random() % MAX_NUMBER));
				grafoAux.cantAristas++;
			}
			grado--;
		}

		int cont = 1;
		for (int g = 2; g <= grado; g += 2) {
			for (int i = 0; i < cantNodos; i++) {
				int j;
				if (i + cont >= cantNodos)
					j = (cont + i) - cantNodos;
				else
					j = i + cont;
				grafoAux.matrizAdyacencia.setValueAt(i, j, (int) (Math.random() % MAX_NUMBER));
				grafoAux.cantAristas++;
			}
			cont++;
		}
		grafoAux.cantAristas += grafoAux.checkNoConnections();
		grafoAux.fillGraph();
		grafoAux.print();
		grafoAux.saveGraph(file);
	}

	public static void generadorRegularesProbabilidad(int cantNodos, int probabilidad, File file) throws Exception {
		int grado = (int) Math.ceil((double) probabilidad * (cantNodos - 1) / 100);
		generadorRegularesGrado(cantNodos, grado, file);
	}

	public static void generadorNPartitos(int cantNodos, int n, File file) {
		int nodosXGrupo = cantNodos / n;
		GrafoDirigidoPonderado grafoAux = new GrafoDirigidoPonderado(cantNodos);
		for (int i = 0; i < cantNodos; i++)
			grafoAux.nodos.add(new Nodo(Integer.toString(i + 1)));
		int[][] grupos = new int[n][cantNodos];
		int i = 0, j = 0, cont;

		while (i < n && j < cantNodos) {
			cont = 0;
			while (j < cantNodos && cont < nodosXGrupo) {
				grupos[i][j] = 1;
				cont++;
				j++;
			}
			i++;
		}
		i = 0;
		j = 0;

		if (nodosXGrupo * n < cantNodos)
			for (int k = (nodosXGrupo * n); k < cantNodos; k++)
				grupos[0][k] = 1;

		while (i < n) {
			j = 0;
			while (j < cantNodos) {
				if (grupos[i][j] == 1) {
					for (int k = 0; k < cantNodos; k++)
						if (grupos[i][k] == 0) {
							grafoAux.matrizAdyacencia.setValueAt(j, k, (int) (Math.random() % MAX_NUMBER));
							grafoAux.cantAristas++;
						}
				}
				j++;
			}
			i++;
		}
		grafoAux.cantAristas += grafoAux.checkNoConnections();
		grafoAux.fillGraph();
		grafoAux.print();
		grafoAux.saveGraph(file);
	}

	private void saveGraph(File file) {
		FileWriter fr = null;
		BufferedWriter br = null;
		try {
			fr = new FileWriter(file);
			br = new BufferedWriter(fr);
			String line = nodos.size() + " " + cantAristas + " " + porcentajeAdyacencia + " " + gradoMayor + " " + gradoMenor;
			fr.write(line);
			for (int i = 0; i < nodos.size() ; i++) {
				for (int j = 0; j < nodos.size(); j++) {
					if (i != j)
						if (matrizAdyacencia.getValueAt(i, j) != Integer.MAX_VALUE)
							fr.write("\n" + i + " " + j + " " + matrizAdyacencia.getValueAt(i, j));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	private void fillGraph() {
		porcentajeAdyacencia = cantAristas * 100 / matrizAdyacencia.getMaxConnection();
		gradoMenor = matrizAdyacencia.getMaxConnection();
		gradoMayor = 1;
		for (int i = 0; i < nodos.size(); i++) {
			int connections = 0;
			for (int j = 0; j < nodos.size(); j++) {
				if (i != j)
					if (matrizAdyacencia.getValueAt(i, j) != Integer.MAX_VALUE)
						connections++;
			}
			if (connections > gradoMayor)
				gradoMayor = connections;
			if (connections < gradoMenor)
				gradoMenor = connections;
		}
	}

	private int checkNoConnections() {
		int cantidadCambios = 0;
		for (int i = 0; i < nodos.size() - 1; i++) {
			boolean tienePareja = false;
			for (int j = i + 1; j < nodos.size(); j++) {
				if (matrizAdyacencia.getValueAt(i, j) != Integer.MAX_VALUE) {
					tienePareja = true;
					break;
				}
			}
			if (tienePareja == false) {
				cantidadCambios++;
				if (nodos.get(0) != nodos.get(i))
					matrizAdyacencia.setValueAt(i, 0, (int) (Math.random() % MAX_NUMBER));
				else
					matrizAdyacencia.setValueAt(i, 1, (int) (Math.random() % MAX_NUMBER));
			}
		}
		return cantidadCambios;
	}

	private static boolean randomConnection(int probabilidad) {
		if (Math.random() < ((float) probabilidad / 100))
			return true;
		return false;
	}

	public void coloreo(Comparator<Nodo> sorter) {
		Collections.shuffle(nodos);
		if (sorter != null) // en caso de que el sorter sea aleatorio
			Collections.sort(nodos, sorter); // sort OP
		List<Nodo> nodosPintados = new ArrayList<Nodo>();
		List<Nodo> nodosSinPintar = new ArrayList<Nodo>(nodos);
		int cont = 1;
		boolean flagSinConexion;

		while (!nodosSinPintar.isEmpty()) {
			nodosSinPintar.get(0).setColor(cont);
			nodosPintados.add(nodosSinPintar.remove(0));

			for (Iterator<Nodo> iteratorNoPintados = nodosSinPintar.iterator(); iteratorNoPintados.hasNext();) {
				Nodo nodoSinPintar = iteratorNoPintados.next();
				flagSinConexion = true;
				for (Iterator<Nodo> iteratorPintados = nodosPintados.iterator(); iteratorPintados.hasNext();) {
					Nodo nodoPintado = iteratorPintados.next();

					if (isConnectedTo(nodoSinPintar, nodoPintado)) {
						flagSinConexion = false;
						break;
					}
				}
				if (flagSinConexion == true) {
					nodoSinPintar.setColor(cont);
					nodosPintados.add(nodoSinPintar);
					iteratorNoPintados.remove();
				}
			}
			cont++;
			nodosPintados.clear();
		}
		guardarResultadoColoreo(cont);
	}

	private void guardarResultadoColoreo(int cantidadColores) {
		FileWriter fr = null;
		BufferedWriter br = null;
		try {
			fr = new FileWriter(PATH_OUT);
			br = new BufferedWriter(fr);
			String line = nodos.size() + " " + cantidadColores + " " + cantAristas + " " + porcentajeAdyacencia + " " + gradoMayor + " " + gradoMenor;
			fr.write(line);
			for (Nodo nodo : nodos) {
				fr.write("\n" + (Integer.valueOf(nodo.getIdNodo()) - 1) + " " + nodo.getColor());
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	public static boolean programaProbador(File fileIn, File fileOut) {
		FileReader fr = null;
		BufferedReader br = null;
		FileReader fr2 = null;
		BufferedReader br2 = null;
		try {
			fr = new FileReader(fileIn);
			br = new BufferedReader(fr);
			fr2 = new FileReader(fileOut);
			br2 = new BufferedReader(fr2);
			String line = br.readLine();
			String[] split = line.split(" ");
			int cantNodos = Integer.valueOf(split[0]);
			GrafoDirigidoPonderado grafoAux = new GrafoDirigidoPonderado(cantNodos);
			for (int i = 0; i < cantNodos; i++)
				grafoAux.nodos.add(new Nodo(Integer.toString(i + 1)));

			line = br2.readLine(); // salteo la linea de los encabezados
			int cont = 0;
			while ((line = br2.readLine()) != null) {
				split = line.split(" ");
				int nodo = Integer.valueOf(split[0]);
				int color = Integer.valueOf(split[1]);
				if (grafoAux.nodos.get(nodo).getColor() != 0) // el nodo ya
																// habia sido
																// pintado
																// antes!
					return false;
				grafoAux.nodos.get(nodo).setColor(color);
				cont++;
			}
			if (cont != cantNodos) // la cantidad de nodos pintados no era la
									// requerida
				return false;

			while ((line = br.readLine()) != null) {
				split = line.split(" ");
				if (checkColor(grafoAux.nodos.get(Integer.valueOf(split[0])), grafoAux.nodos.get(Integer.valueOf(split[1]))) == false)
					return false;
				// ((MatrizSimetrica)
				// matrizAdyacencia).setValueAt(Integer.valueOf(split[0]),
				// Integer.valueOf(split[1]), true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
				br2.close();
				fr2.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return true;
	}

	public static boolean checkColor(Nodo n1, Nodo n2) {
		if (n1.getColor() == n2.getColor())
			return false;
		return true;
	}

	public static void main(String[] args) throws Exception {
//		GrafoDirigidoPonderado.generadorProbabilidad(6, 40, new File("src/pack/grafo.in"));
//		// GrafoNoDirigidoNoPonderado grafo = new GrafoNoDirigidoNoPonderado(new
//		// File("src/pack/grafo.in"));
//		// grafo.print();
//		// grafo.coloreo(Nodo.welshPowellSorter);
//		System.out.println(programaProbador(new File("src/pack/grafo.in"), new File(PATH_OUT)));
	}
}