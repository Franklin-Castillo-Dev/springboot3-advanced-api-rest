package med.voll.api.infra.security;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import med.voll.api.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // Es el Estereotipo mas generico de Spring para definir un Componente que Spring Escanea para incluirlo en su contexto.
public class SecurityFilter extends OncePerRequestFilter { // Interfaz Propia de Spring para realizacion del Filtro Requerido.

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // Obtenemos Token del Header
        // Por estandar el Token llega en un header en especifico, llamado "Authorization". Es obligatorio obtenerlo asi.
        var authHeader = request.getHeader("Authorization"); //

        // Solamente si viene token hacemos validacion
        if(authHeader != null){
            // Eliminamos el PREFIX y el espacio del token.
            var token = authHeader.replace("Bearer ", "");

            // guardamos subject.
            var nombreUsuario = tokenService.getSubject(token); // Extract Username

            // significa que el token es valido
            if(nombreUsuario != null){

                // token valido
                var usuario = usuarioRepository.findByLogin(nombreUsuario);

                //a partir de aca le decimos a Spring, que para mi, este login es valido.

                // Creamos el Authentication
                var authentication = new UsernamePasswordAuthenticationToken(
                        usuario,
                        null,
                        usuario.getAuthorities()
                ); //Forzamos Inicio Sesion.

                //Lo seteamos manualmente en el Contexto del SecurityContextHolder.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Realizamos Filtro, y mandamos Request y Response.
        // Esta es la Unica Forma de Hacer el Filtro
        filterChain.doFilter(request, response);
    }

}
