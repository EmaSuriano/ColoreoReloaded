package pack;

import java.util.ArrayList;
import java.util.List;

public class MatrizAsimetrica {
	private int matriz[][];

	public MatrizAsimetrica(int cantNodos) {
		matriz = new int[cantNodos][cantNodos];
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				matriz[i][j]= Integer.MAX_VALUE;
			}
		}
	}
	
	public int getLength(){
		return matriz.length;
	}
	
	public MatrizAsimetrica borrarDiagonalPrincipal(){
		MatrizAsimetrica matrizAux= new MatrizAsimetrica(this.matriz.length); 
		for (int i = 0; i < this.matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(i==j)
					matrizAux.matriz[i][j]=0;
				else
					matrizAux.matriz[i][j]= this.matriz[i][j];
			}
		}
		return matrizAux;
	}
	
	public MatrizAsimetrica convertirMatrizBoolean(){
		MatrizAsimetrica matrizAux= new MatrizAsimetrica(this.matriz.length); 
		for (int i = 0; i < this.matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(this.matriz[i][j]== Integer.MAX_VALUE)
					matrizAux.matriz[i][j]= 0;
				else
					matrizAux.matriz[i][j]= 1;
			}
		}
		return matrizAux;
	}
	
	public int getMaxConnection(){
		return matriz.length* matriz.length;
	}

	public void setValueAt(int x, int y, int val) {
		matriz[x][y] = val;
	}

	public int getValueAt(int x, int y) {
		return matriz[x][y];
	}
	
	public void showMatrix(){
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz.length; j++) {
				if(matriz[i][j]== Integer.MAX_VALUE)
					System.out.print("oo ");
				else
					System.out.print(matriz[i][j] + " ");
			}
			System.out.println(" ");
		}
	}
}
