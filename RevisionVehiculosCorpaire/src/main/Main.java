package main;

import controller.ControladorRevision;
import view.ConsolaVista;

public class Main {
    public static void main(String[] args) {
        ConsolaVista vista = new ConsolaVista();
        ControladorRevision controlador = new ControladorRevision(vista);
        controlador.iniciar();
    }
}