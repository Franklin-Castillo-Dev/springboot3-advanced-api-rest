package med.voll.api.domain.dto.medico;

import jakarta.validation.constraints.NotNull;
import med.voll.api.domain.dto.direccion.DatosDireccion;

// Este DTO es el que nos definira que datos si se pueden actualizar de la Entidad.
public record DatosActualizarMedico(
        // Como es Integer, colocamos @NotNull, solo con Strings se coloca @NotBlank
        //Solamente el ID configuramos como dato OBLIGATORIO, ya que debemos saberlo para actualizar en la DB
        // los demas pueden ir vacios si asi se requiere.
        @NotNull
        Long id,
        String nombre,
        String documento,
        DatosDireccion direccion
) {
}
