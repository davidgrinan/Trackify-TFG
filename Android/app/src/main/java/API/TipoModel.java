package API;

public class TipoModel {

    private long id;
    private String nombre;

    public TipoModel(long id, String nombre) {
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