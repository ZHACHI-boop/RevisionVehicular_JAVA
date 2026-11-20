package controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import model.Mecanico;
import model.Vehiculo;
import util.ArchivoUtil;
import view.ConsolaVista;

public class ControladorRevision {
    private static final String RUTA_MECANICOS = "mecanicos.txt";
    private static final String RUTA_VEHICULOS = "vehiculos.txt";
    private ConsolaVista vista;

    public ControladorRevision(ConsolaVista vista) {
        this.vista = vista;
        ArchivoUtil.inicializarArchivosBase(RUTA_MECANICOS, RUTA_VEHICULOS);
    }

    public void iniciar() {
        int opcion;
        do {
            opcion = vista.mostrarMenuPrincipal();
            switch (opcion) {
                case 1:
                    new File(RUTA_MECANICOS).delete();
                    new File(RUTA_VEHICULOS).delete();
                    ArchivoUtil.inicializarArchivosBase(RUTA_MECANICOS, RUTA_VEHICULOS);
                    System.out.println("Archivos mecanicos.txt y vehiculos.txt regenerados con exito.");
                    break;
                case 2:
                    ejecutarProcesoConcurrente();
                    break;
                case 3:
                    System.out.println("Saliendo del sistema Corpaire...");
                    break;
                default:
                    System.out.println("Opcion invalida. Intente nuevamente.");
            }
        } while (opcion != 3);
    }

    private void ejecutarProcesoConcurrente() {
        List<Mecanico> mecanicos = ArchivoUtil.leerMecanicos(RUTA_MECANICOS);
        List<Vehiculo> vehiculos = ArchivoUtil.leerVehiculos(RUTA_VEHICULOS);

        // 1. Hilo con Runnable para carga desde servidor (demora hasta 3s)
        CargaServidorRunnable tareaCarga = new CargaServidorRunnable(vehiculos, vista);
        Thread hiloCarga = new Thread(tareaCarga, "Hilo-ServidorDB");
        hiloCarga.start();

        try {
            // Esperamos a que el servidor entregue las lecturas antes de repartir lotes
            hiloCarga.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 2. Creacion y arranque de 10 hilos (Thread) por cada mecanico
        List<ProcesadorMecanicoThread> hilosMecanicos = new ArrayList<>();
        for (Mecanico mec : mecanicos) {
            List<Vehiculo> lote = filtrarVehiculosPorMarca(vehiculos, mec.getMarcaEspecialidad(), 5);
            ProcesadorMecanicoThread hiloMec = new ProcesadorMecanicoThread(mec, lote, vista);
            hilosMecanicos.add(hiloMec);
            hiloMec.start();
        }

        // Esperar a que los 10 hilos terminen de imprimir antes de volver al menu
        for (ProcesadorMecanicoThread hilo : hilosMecanicos) {
            try {
                hilo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private List<Vehiculo> filtrarVehiculosPorMarca(List<Vehiculo> todos, String marca, int limite) {
        List<Vehiculo> lote = new ArrayList<>();
        for (Vehiculo v : todos) {
            if (v.getMarca().equalsIgnoreCase(marca) && lote.size() < limite) {
                lote.add(v);
            }
        }
        return lote;
    }
}