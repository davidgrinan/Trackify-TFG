package API;

public class EstadoModel {

    private long id;
    private String nombre;

    public EstadoModel(long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }
}