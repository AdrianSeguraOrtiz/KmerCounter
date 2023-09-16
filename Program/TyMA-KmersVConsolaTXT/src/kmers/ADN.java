package kmers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Scanner;

public class ADN {

	private String fichero;
	private String disco;
	private List <Integer[]> frecuenciasAbsolutas;
	private List <Double[]> frecuenciasRelativas;
	private int k1;
	private int k2;
	
	public ADN (String fichero, int k1, int k2) {
		this.fichero = fichero;
		this.disco = fichero + ".txt";
		this.leeFichero();
		frecuenciasAbsolutas = new ArrayList <Integer[]> ();
		frecuenciasRelativas = new ArrayList <Double[]> ();
		this.k1 = k1;
		this.k2 = k2;
	}
	public String toString() {
		String res = k1 == k2 ? "Solución para " + fichero + " para k = " + k1 + ": \n\n" : "Solución para " + fichero + " para k de " + k1 + " a " + k2 + ": \n\n";
		for (int i = k1; i <= k2; i++) {
			res += kmers(i);
		}
		return res;
	}
	
	private void leeFichero() {
		try {
			File file = new File (disco);
			FileWriter fw = new FileWriter (file);
			BufferedWriter bw = new BufferedWriter(fw);
			Scanner sc = new Scanner(new File(fichero));
			sc.nextLine();
			while (sc.hasNextLine()) {
				String linea = sc.nextLine();
				if (linea.startsWith(">")) {
					bw.newLine();
				}
				else {
					bw.write(linea);
				}
			}
			bw.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String kmers (int k) {
		String res = "Frecuencia K-Mers para k = " + k + ": \n\n";
		int cnt = 1;
		try {
			Scanner sc = new Scanner (new File(disco));
			while (sc.hasNextLine()){
				res += " - Secuencia " + cnt + ": \n" + kmers(k, sc.nextLine()) + "\n\n";
				cnt += 1;
			}
			if (cnt > 2) {
				res += calculaGlobal(k) + "\n\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String calculaGlobal (int k) {
		String res = " - Recuento global del archivo para k = " + k + ": \n\n";
		Integer [] frecuenciaAbs = new Integer [(int)Math.pow(4, k)];
		double frecuenciaRel_i = 0;
		int numKmersTotal = 0;
		try {
			Scanner sc = new Scanner (new File(disco));
			int numLineas = 0;
			while (sc.hasNextLine()){
				numKmersTotal += sc.nextLine().length() - k + 1;
				numLineas += 1;
			}
			for (int i = 0; i < (int)Math.pow(4, k); i++) {
				frecuenciaAbs[i] = 0;
				frecuenciaRel_i = 0;
				for (int j = numLineas*(k-k1); j <  numLineas*(k-k1) 
						+ numLineas; j++) {
					frecuenciaAbs[i] += frecuenciasAbsolutas.get(j)[i];
				}
			}
			for (int i = 0; i < (int)Math.pow(4, k); i++) {
				frecuenciaRel_i = (double) frecuenciaAbs[i]/numKmersTotal;
				Formatter f = new Formatter();
				res += IndexToString(i, k) + ": " + f.format("%0" + String.valueOf(
						calculaMaximo(frecuenciaAbs)).length() + "d", frecuenciaAbs[i])
						+ " (" + (float) frecuenciaRel_i +"%) \n";
				f.close();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return res;
	}
	
	private String kmers(int k, String sec) {
		Integer [] frecuenciasAbs = new Integer [(int)Math.pow(4, k)] ;
		Double [] frecuenciasRel = new Double [(int)Math.pow(4, k)];
		String [] kmers = new String [(int)Math.pow(4, k)];
		for (int i = 0; i < kmers.length; i++) {
			kmers[i] = IndexToString(i, k);
			frecuenciasAbs[i] = 0;
		}
		for (int i = 0; i < sec.length() - k+1; i++) {
			String kmer = sec.substring(i, i+k);
			int numKmer = StringToIndex(kmer);
			frecuenciasAbs[numKmer] += 1;
		}
		frecuenciasAbsolutas.add(frecuenciasAbs);
		for (int i = 0; i < frecuenciasAbs.length; i++) {
			frecuenciasRel[i] = (double) frecuenciasAbs[i]/(sec.length() - k +1);
		}
		frecuenciasRelativas.add(frecuenciasRel);
		String res = "";
		for (int i = 0; i < kmers.length; i++) {
			Formatter f = new Formatter();
			res = res + "\n" +  kmers[i] + ": " + f.format("%0" + String.valueOf(
					calculaMaximo(frecuenciasAbs)).length() + "d", frecuenciasAbs[i])
					+ " (" + frecuenciasRel[i].floatValue() + "%)";
			f.close();
		}
		return res;
	}
	
	private int calculaMaximo(Integer [] numeros) {
		int max = 0;
		for (int i : numeros) {
			if(i > max) {
				max = i;
			}
		}
		return max;
	}
	private int StringToIndex(String kmer) {
		int res = 0;
		char [] arrayKmer = kmer.toCharArray();
		for (int i = 0; i < arrayKmer.length; i++) {
			if (arrayKmer[i] == 'A') {
				res = res + 0*(int)Math.pow(4, arrayKmer.length - i -1);
			}
			else if (arrayKmer[i] == 'C') {
				res = res + 1*(int)Math.pow(4, arrayKmer.length - i -1);
			}
			else if (arrayKmer[i] == 'G') {
				res = res + 2*(int)Math.pow(4, arrayKmer.length - i -1);
			}
			else if (arrayKmer[i] == 'T') {
				res = res + 3*(int)Math.pow(4, arrayKmer.length - i -1);
			}
		}
		return res;
	}
	
	private String IndexToString(int pos, int k) {
		char[] res = new char[k];
		int resto;
		int cociente = pos;
		char alph[] = { 'A', 'C', 'G', 'T' };
		int i = 0;
		for (i = 0; i < k; i++)
			res[i] = 'A';
		i = 0;
		while (cociente >= 4) { 
			resto = cociente % 4;
			cociente = cociente / 4;
			res[k - i - 1] = alph[resto];
			i++;
		}
		res[k - i - 1] = alph[cociente];
		return String.valueOf(res);
	}
}
