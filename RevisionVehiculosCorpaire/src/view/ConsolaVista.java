package view;

import java.util.Scanner;
import model.LecturaGases;
import model.Mecanico;
import model.ResultadoLote;
import model.Vehiculo;

public class ConsolaVista {
    private final Scanner scanner = new Scanner(System.in);

    public int mostrarMenuPrincipal() {
        System.out.println("\n=======================================================================================");
        System.out.println("            SISTEMA DE REVISION VEHICULAR CORPAIRE - QUITO (GESTION CONCURRENTE)");
        System.out.println("=======================================================================================");
        System.out.println("1. Regenerar archivos base (mecanicos.txt y vehiculos.txt)");
        System.out.println("2. Ejecutar simulacion concurrente de revision de lotes (Hilos)");
        System.out.println("3. Salir");
        System.out.print("Seleccione una opcion: ");
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public synchronized void mostrarMensajeServidor(String mensaje) {
        System.out.println("[SERVIDOR DB] " + mensaje);
    }

    public synchronized void imprimirTablaLote(ResultadoLote resultado) {
        Mecanico m = resultado.getMecanico();
        System.out.println("\n+---------------------------------------------------------------------------------------+");
        System.out.printf("| MECANICO: %-25s | CODIGO: %-8s | MARCA ASIGNADA: %-15s |%n",
                m.getNombre(), m.getCodigo(), m.getMarcaEspecialidad());
        System.out.println("+------------+---------------+------------+------------+------------+------------+--------+");
        System.out.println("| PLACA      | MARCA         | LAMBDA     | O2         | N2O        | CO2        | FALLAS |");
        System.out.println("+------------+---------------+------------+------------+------------+------------+--------+");

        for (Vehiculo v : resultado.getVehiculos()) {
            LecturaGases g = v.getLecturaGases();
            String sLam = String.format("%.2f%s", g.getLambda(), g.fallaLambda() ? " (!)" : "");
            String sO2  = String.format("%.2f%s", g.getO2(), g.fallaO2() ? " (!)" : "");
            String sN2O = String.format("%.2f%s", g.getN2O(), g.fallaN2O() ? " (!)" : "");
            String sCO2 = String.format("%.2f%s", g.getCO2(), g.fallaCO2() ? " (!)" : "");

            System.out.printf("| %-10s | %-13s | %-10s | %-10s | %-10s | %-10s | %-6d |%n",
                    v.getPlaca(), v.getMarca(), sLam, sO2, sN2O, sCO2, g.contarFallasVehiculo());
        }

        System.out.println("+------------+---------------+------------+------------+------------+------------+--------+");
        System.out.printf("| %% ERROR POR PARAMETRO -> Lambda: %3.0f%% (Tol: 70%%) | O2: %3.0f%% (Tol: 30%%)                 |%n",
                resultado.calcularPorcentajeFallaLambda(), resultado.calcularPorcentajeFallaO2());
        System.out.printf("|                          N2O:    %3.0f%% (Tol: 80%%) | CO2: %3.0f%% (Tol: 50%%)                |%n",
                resultado.calcularPorcentajeFallaN2O(), resultado.calcularPorcentajeFallaCO2());
        System.out.printf("| %% ERROR GENERAL DEL LOTE (5 autos / 20 mediciones): %.2f%%                            |%n",
                resultado.calcularPorcentajeErrorGeneral());
        System.out.printf("| RESULTADO DEL LOTE: %-65s |%n",
                resultado.apruebaPrimeraRevision() ? "PASA EN PRIMERA REVISION" : "SEGUNDA REVISION ASIGNADA");
        System.out.printf("| OBSERVACION:        %-65s |%n", resultado.obtenerObservaciones());
        System.out.println("+---------------------------------------------------------------------------------------+");
    }
}