package controller;

import java.util.List;
import java.util.Random;
import model.LecturaGases;
import model.Vehiculo;
import view.ConsolaVista;

public class CargaServidorRunnable implements Runnable {
    private List<Vehiculo> vehiculos;
    private ConsolaVista vista;
    private long tiempoDemoraMs;

    public CargaServidorRunnable(List<Vehiculo> vehiculos, ConsolaVista vista) {
        this.vehiculos = vehiculos;
        this.vista = vista;
    }

    @Override
    public void run() {
        Random random = new Random();
        this.tiempoDemoraMs = 500 + random.nextInt(2501); // Entre 500 ms y 3000 ms (3 seg)
        vista.mostrarMensajeServidor("Solicitando reporte de gases de " + vehiculos.size() + " vehiculos...");

        try {
            Thread.sleep(tiempoDemoraMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Vehiculo v : vehiculos) {
            double lambda = redondear(0.75 + (random.nextDouble() * 0.65)); // 0.75 a 1.40
            double o2     = redondear(0.40 + (random.nextDouble() * 2.60)); // 0.40 a 3.00
            double n2o    = redondear(15.0 + (random.nextDouble() * 45.0)); // 15.0 a 60.0
            double co2    = redondear(8.0  + (random.nextDouble() * 12.0)); // 8.0 a 20.0
            v.setLecturaGases(new LecturaGases(lambda, o2, n2o, co2));
        }

        vista.mostrarMensajeServidor("Datos recibidos correctamente en " + tiempoDemoraMs + " ms.");
    }

    private double redondear(double valor) {
        return Math.round(valor * 100.0) / 100.0;
    }
}