package med.voll.api.domain.dto.medico;

import med.voll.api.domain.model.Medico;

// DTO para filtrar informacion que recibimos del a base
// y luego enviarla al usuario desde la API
public record DatosListadoMedico(
        Long id,
        String nombre,
        String especialidad,
        String documento,
        String email
) {

    // Constructor para Mapear Entidad Medico al formato del DTO DatosListadoMedico
    // Requerido para poder mapear nuestro listado del GET
    public DatosListadoMedico(Medico medico){
        this(medico.getId(), medico.getNombre(), medico.getEspecialidad().toString(), medico.getDocumento(), medico.getEmail());

    }
}
