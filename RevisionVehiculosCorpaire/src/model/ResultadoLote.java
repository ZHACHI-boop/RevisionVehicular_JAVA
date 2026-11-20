package model;

import java.util.ArrayList;
import java.util.List;

public class ResultadoLote {
    // Porcentajes maximos de tolerancia por parametro
    public static final double TOL_LAMBDA = 70.0;
    public static final double TOL_O2 = 30.0;
    public static final double TOL_N2O = 80.0;
    public static final double TOL_CO2 = 50.0;

    private Mecanico mecanico;
    private List<Vehiculo> vehiculos;

    public ResultadoLote(Mecanico mecanico, List<Vehiculo> vehiculos) {
        this.mecanico = mecanico;
        this.vehiculos = vehiculos;
    }

    public double calcularPorcentajeFallaLambda() {
        int fallas = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getLecturaGases().fallaLambda()) fallas++;
        }
        return (fallas * 100.0) / vehiculos.size();
    }

    public double calcularPorcentajeFallaO2() {
        int fallas = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getLecturaGases().fallaO2()) fallas++;
        }
        return (fallas * 100.0) / vehiculos.size();
    }

    public double calcularPorcentajeFallaN2O() {
        int fallas = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getLecturaGases().fallaN2O()) fallas++;
        }
        return (fallas * 100.0) / vehiculos.size();
    }

    public double calcularPorcentajeFallaCO2() {
        int fallas = 0;
        for (Vehiculo v : vehiculos) {
            if (v.getLecturaGases().fallaCO2()) fallas++;
        }
        return (fallas * 100.0) / vehiculos.size();
    }

    public double calcularPorcentajeErrorGeneral() {
        int totalFallas = 0;
        int totalMediciones = vehiculos.size() * 4;
        for (Vehiculo v : vehiculos) {
            totalFallas += v.getLecturaGases().contarFallasVehiculo();
        }
        return (totalFallas * 100.0) / totalMediciones;
    }

    public boolean apruebaPrimeraRevision() {
        return calcularPorcentajeFallaLambda() <= TOL_LAMBDA
            && calcularPorcentajeFallaO2() <= TOL_O2
            && calcularPorcentajeFallaN2O() <= TOL_N2O
            && calcularPorcentajeFallaCO2() <= TOL_CO2;
    }

    public String obtenerObservaciones() {
        List<String> excedidos = new ArrayList<>();
        if (calcularPorcentajeFallaLambda() > TOL_LAMBDA)
            excedidos.add(String.format("Lambda (%.0f%% > %.0f%%)", calcularPorcentajeFallaLambda(), TOL_LAMBDA));
        if (calcularPorcentajeFallaO2() > TOL_O2)
            excedidos.add(String.format("O2 (%.0f%% > %.0f%%)", calcularPorcentajeFallaO2(), TOL_O2));
        if (calcularPorcentajeFallaN2O() > TOL_N2O)
            excedidos.add(String.format("N2O (%.0f%% > %.0f%%)", calcularPorcentajeFallaN2O(), TOL_N2O));
        if (calcularPorcentajeFallaCO2() > TOL_CO2)
            excedidos.add(String.format("CO2 (%.0f%% > %.0f%%)", calcularPorcentajeFallaCO2(), TOL_CO2));

        return excedidos.isEmpty() ? "Todos los parametros dentro de tolerancia"
                                   : "Supera tolerancia en: " + String.join(", ", excedidos);
    }

    public Mecanico getMecanico() { return mecanico; }
    public List<Vehiculo> getVehiculos() { return vehiculos; }
}