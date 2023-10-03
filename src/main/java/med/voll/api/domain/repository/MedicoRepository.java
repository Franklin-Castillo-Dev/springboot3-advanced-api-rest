package med.voll.api.domain.repository;

import med.voll.api.domain.model.Medico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// extends JpaRepository<param1, param2>
// Estamos utilizando Generics
// Param1 = Tipo Objeto que voy a guardar
// Param2 = Tipo de Dato que Utiliza el ID del objeto.
// extends JpaRepository<Medico, Long>
public interface MedicoRepository extends JpaRepository<Medico, Long> {

    //No requiere ninguna linea extra de codigo, porque mi JpaRepository ya hace todo por mi.

    // Consulta Personalizada Adicional Creada por Mi con la nomenclatura de Spring DATA.
    // El tipo debe ser igual al Tipo de Retorno que necesitamos que haga.
    // En este caso como queremos una Paginacion de Medicos para nuestro GET, asi lo configuramos.
    Page<Medico> findByActivoTrue(Pageable paginacion);
}
