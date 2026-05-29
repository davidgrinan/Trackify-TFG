package API;

public class GeneroModel {

    private long id;
    private String nombre;

    public GeneroModel(long id, String nombre) {
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