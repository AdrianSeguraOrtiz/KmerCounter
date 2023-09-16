package kmers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Ejecutable {

	private static void guardar (String s, String nombreFicheroSalida) {
		File file = new File (nombreFicheroSalida);
		try {
			FileWriter fw = new FileWriter (file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		String fichero = args[0];
		int k1 = Integer.valueOf(args[1]);
		ADN adn;
		String ficheroSalida;
		try {
			int k2 = Integer.valueOf(args[2]);
			adn = new ADN(fichero, k1, k2);
			ficheroSalida = "Kmers" + fichero + "ParaK(" + k1 + "," + k2 + ").txt";
		}
		catch (Exception e) {
			adn = new ADN(fichero, k1, k1);
			ficheroSalida = "Kmers" + fichero + "ParaK" + k1 + ".txt";
		}
		String resultado = adn.toString();
		guardar(resultado, ficheroSalida);
		System.out.println(resultado);
		System.out.println("El resultado ha sido guardado en el fichero " + ficheroSalida);
	}

}
