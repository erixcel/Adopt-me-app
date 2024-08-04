package firebase.app.veterinaria_android.modelo;

public class Mascota {

    String edad;
    String especie;
    String id;
    String imagen;
    String nombre;
    String propietario;
    String raza;
    String sexo;

    public Mascota(){}

    public Mascota(String edad, String especie, String id, String imagen, String nombre, String propietario, String raza, String sexo) {
        this.edad = edad;
        this.especie = especie;
        this.id = id;
        this.imagen = imagen;
        this.nombre = nombre;
        this.propietario = propietario;
        this.raza = raza;
        this.sexo = sexo;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPropietario() {
        return propietario;
    }

    public void setPropietario(String propietario) {
        this.propietario = propietario;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }
}
