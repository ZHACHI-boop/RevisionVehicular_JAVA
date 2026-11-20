package util;

import java.io.*;
import java.util.*;
import model.Mecanico;
import model.Vehiculo;

public class ArchivoUtil {

    public static void inicializarArchivosBase(String rutaMecanicos, String rutaVehiculos) {
        File fMec = new File(rutaMecanicos);
        File fVeh = new File(rutaVehiculos);

        String[] marcas = {"Toyota", "Chevrolet", "Hyundai", "Kia", "Nissan",
                           "Mazda", "Ford", "Volkswagen", "Renault", "Suzuki"};
        String[] nombres = {"Carlos Andrade", "Luis Morales", "Jorge Paredes", "Andres Vega", "Pedro Salazar",
                            "Diego Castro", "Mario Torres", "Fernando Ruiz", "Javier Mendoza", "Ricardo Leon"};

        if (!fMec.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(fMec))) {
                for (int i = 0; i < 10; i++) {
                    pw.printf("MEC-%02d;%s;%s%n", (i + 1), nombres[i], marcas[i]);
                }
            } catch (IOException e) {
                System.err.println("Error generando mecanicos.txt: " + e.getMessage());
            }
        }

        if (!fVeh.exists()) {
            Random rnd = new Random();
            try (PrintWriter pw = new PrintWriter(new FileWriter(fVeh))) {
                for (String marca : marcas) {
                    for (int j = 0; j < 5; j++) {
                        String letras = "" + (char)('A' + rnd.nextInt(26))
                                           + (char)('A' + rnd.nextInt(26))
                                           + (char)('A' + rnd.nextInt(26));
                        int num = 1000 + rnd.nextInt(9000);
                        pw.printf("%s-%d;%s%n", letras, num, marca);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error generando vehiculos.txt: " + e.getMessage());
            }
        }
    }

    public static List<Mecanico> leerMecanicos(String ruta) {
        List<Mecanico> lista = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(ruta))) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(";");
                    lista.add(new Mecanico(partes[0], partes[1], partes[2]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encontro el archivo: " + ruta);
        }
        return lista;
    }

    public static List<Vehiculo> leerVehiculos(String ruta) {
        List<Vehiculo> lista = new ArrayList<>();
        try (Scanner sc = new Scanner(new File(ruta))) {
            while (sc.hasNextLine()) {
                String linea = sc.nextLine().trim();
                if (!linea.isEmpty()) {
                    String[] partes = linea.split(";");
                    lista.add(new Vehiculo(partes[0], partes[1]));
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("No se encontro el archivo: " + ruta);
        }
        return lista;
    }
}