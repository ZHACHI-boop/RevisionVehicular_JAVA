package model;

public class Mecanico {
    private String codigo;
    private String nombre;
    private String marcaEspecialidad;

    public Mecanico(String codigo, String nombre, String marcaEspecialidad) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marcaEspecialidad = marcaEspecialidad;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getMarcaEspecialidad() { return marcaEspecialidad; }
}