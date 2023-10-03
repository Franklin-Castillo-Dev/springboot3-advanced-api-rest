package med.voll.api.domain.repository;

import med.voll.api.domain.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

// extends JpaRepository<param1, param2>
// Estamos utilizando Generics
// Param1 = Tipo Objeto que voy a guardar
// Param2 = Tipo de Dato que Utiliza el ID del objeto.
// extends JpaRepository<Medico, Long>
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    UserDetails findByLogin(String username);
}
