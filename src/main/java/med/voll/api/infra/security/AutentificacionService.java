package med.voll.api.infra.security;

import med.voll.api.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Implementamos Intefaz "UserDetailsService". Viene  ya incorporado en SpringBoot Security.
// y definimos nuestra clase con la notacion Service.
@Service
public class AutentificacionService implements UserDetailsService {

    @Autowired //Inyectamos dependencia
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findByLogin(username);
    }
}
