package firebase.app.veterinaria_android.modelo;

public class Usuario {
    private String id;
    private String nombre;
    private String pais;
    private String apellido;
    private String celular;

    public Usuario() {

    }

    public Usuario(String id, String nombre, String pais, String apellido, String celular) {
        this.id = id;
        this.nombre = nombre;
        this.pais = pais;
        this.apellido = apellido;
        this.celular = celular;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }
}
