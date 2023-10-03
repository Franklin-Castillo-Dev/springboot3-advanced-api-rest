package med.voll.api.domain.dto.medico;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import med.voll.api.domain.dto.direccion.DatosDireccion;

public record DatosRegistroMedico(
        @NotBlank
        String nombre,
        @NotBlank
        @Email
        String email,
        @NotBlank
        //significa numero de 4-6 digitos
        @Pattern(regexp = "\\d{4,6}")
        String documento,
        @NotBlank
        String telefono,
        @NotNull
        Especialidad especialidad,
        @NotNull
        @Valid // Para que nos valide que el DTO de DatosDireccion tambien sea valido
        DatosDireccion direccion)
{

}
