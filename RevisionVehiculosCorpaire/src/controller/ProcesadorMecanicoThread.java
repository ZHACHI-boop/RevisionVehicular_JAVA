package controller;

import java.util.List;
import java.util.Random;
import model.Mecanico;
import model.ResultadoLote;
import model.Vehiculo;
import view.ConsolaVista;

public class ProcesadorMecanicoThread extends Thread {
    private Mecanico mecanico;
    private List<Vehiculo> loteVehiculos;
    private ConsolaVista vista;

    public ProcesadorMecanicoThread(Mecanico mecanico, List<Vehiculo> loteVehiculos, ConsolaVista vista) {
        super("Hilo-" + mecanico.getCodigo());
        this.mecanico = mecanico;
        this.loteVehiculos = loteVehiculos;
        this.vista = vista;
    }

    @Override
    public void run() {
        try {
            // Simula latencia individual de despliegue en pantalla (hasta 3 segundos)
            Thread.sleep(200 + new Random().nextInt(2801));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        ResultadoLote resultado = new ResultadoLote(mecanico, loteVehiculos);
        vista.imprimirTablaLote(resultado);
    }
}