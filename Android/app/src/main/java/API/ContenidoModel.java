package API;

public class ContenidoModel {

    private long id;
    private String titulo;
    private String tipo;
    private String genero;
    private String estado;
    private Integer valoracion;

    public ContenidoModel(long id, String titulo, String tipo, String genero, String estado, Integer valoracion) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.genero = genero;
        this.estado = estado;
        this.valoracion = valoracion;
    }

    public long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getTipo() {
        return tipo;
    }

    public String getGenero() {
        return genero;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }
}