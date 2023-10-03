package med.voll.api.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import med.voll.api.domain.dto.medico.DatosActualizarMedico;
import med.voll.api.domain.dto.medico.DatosRegistroMedico;
import med.voll.api.domain.dto.medico.Especialidad;

@Table(name = "medicos")
@Entity(name = "Medico")
@Getter //Genera Getters Lombok
@NoArgsConstructor //Genera Constructor Vacio Lombok
@AllArgsConstructor //Genera Construtor con Parametros Lombok
@EqualsAndHashCode(of = "id") // Para comparaciones entre medicos Lombok
public class Medico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private String email;
    private String documento;

    private String telefono;

    private Boolean activo;

    @Enumerated(EnumType.STRING)
    private Especialidad especialidad;

    @Embedded
    private Direccion direccion;

    // Constructor que acepta el Record DatosRegistroMedico
    // y lo transforma a tipo Medico
    public Medico(DatosRegistroMedico datosRegistroMedico) {
        this.nombre = datosRegistroMedico.nombre();
        this.email = datosRegistroMedico.email();
        this.documento = datosRegistroMedico.documento();
        this.telefono = datosRegistroMedico.telefono();
        this.activo = true;
        this.especialidad = datosRegistroMedico.especialidad();
        this.direccion = new Direccion(datosRegistroMedico.direccion());

    }

    // Metodo que se encarga de actualizar la Entidad Medico a partir del DTO recibido.
    public void actualizarDatos(DatosActualizarMedico datosActualizarMedico) {
        if(datosActualizarMedico.nombre() != null){
            this.nombre = datosActualizarMedico.nombre();
        }
        if(datosActualizarMedico.documento() != null){
            this.documento = datosActualizarMedico.documento();
        }
        if(datosActualizarMedico.direccion() != null){
            this.direccion = direccion.ActualizarDatos(datosActualizarMedico.direccion());
        }
    }

    // Metodo para Desactivar Medico.
    public void desactivarMedico() {
        this.activo = false;
    }
}
