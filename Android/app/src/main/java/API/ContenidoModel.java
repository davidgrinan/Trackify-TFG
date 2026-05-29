package API;

public class ContenidoModel {

    private long id;
    private String titulo;
    private String tipo;
    private String genero;
    private String estado;
    private String descripcion;
    private Integer valoracion;
    private String imagenUrl;

    public ContenidoModel(long id, String titulo, String tipo, String genero,
                          String estado, String descripcion, Integer valoracion,
                          String imagenUrl) {
        this.id = id;
        this.titulo = titulo;
        this.tipo = tipo;
        this.genero = genero;
        this.estado = estado;
        this.descripcion = descripcion;
        this.valoracion = valoracion;
        this.imagenUrl = imagenUrl;
    }

    public long getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getTipo() { return tipo; }
    public String getGenero() { return genero; }
    public String getEstado() { return estado; }
    public String getDescripcion() { return descripcion; }
    public Integer getValoracion() { return valoracion; }
    public String getImagenUrl() { return imagenUrl; }
}