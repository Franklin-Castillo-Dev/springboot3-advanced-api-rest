package med.voll.api.domain.model;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.dto.direccion.DatosDireccion;

//Embeddable sirve para reutilizar estos campos y atributos para varias Entidades
@Embeddable
@Getter //Genera Getters Lombok
@NoArgsConstructor //Genera Constructor Vacio Lombok
@AllArgsConstructor //Genera Construtor con Parametros Lombok
public class Direccion {

    private String calle;
    private String numero;
    private String complemento;
    private String distrito;
    private String ciudad;

    // Constructor que acepta el Record DatosDireccion
    // y lo transforma a tipo Direccion
    public Direccion(DatosDireccion datosDireccion) {
        this.calle = datosDireccion.calle();
        this.numero = datosDireccion.numero();
        this.complemento = datosDireccion.complemento();
        this.distrito = datosDireccion.distrito();
        this.ciudad = datosDireccion.ciudad();

    }

    // Metodo para Actualizar los datos de Direccion a partir del DTO recibido.
    public Direccion ActualizarDatos(DatosDireccion datosDireccion) {
        this.calle = datosDireccion.calle();
        this.numero = datosDireccion.numero();
        this.complemento = datosDireccion.complemento();
        this.distrito = datosDireccion.distrito();
        this.ciudad = datosDireccion.ciudad();

        return this;
    }
}
