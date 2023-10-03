package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.dto.usuario.DatosAutentificacionUsuario;
import med.voll.api.domain.model.Usuario;
import med.voll.api.infra.security.DatosJWTToken;
import med.voll.api.infra.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager; // Clase para Iniciar Proceso de Autentificacion de SpringBootSecurity

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity authUsuario(@RequestBody @Valid DatosAutentificacionUsuario datosAutentificacionUsuario){

        //Variable de Token de Autentificacion
        Authentication authToken = new UsernamePasswordAuthenticationToken(
                datosAutentificacionUsuario.login(),
                datosAutentificacionUsuario.clave()
        );

        //Autentifica nuestra sesion
        var usuarioAutenticado = authenticationManager.authenticate(authToken); // este metodo retorna el Usuario Autentificado

        //generamos token
        var JWTtoken = tokenService.generarToken((Usuario) usuarioAutenticado.getPrincipal()); // .getPrincipal() nos devuelve el usuario ya validado. y lo casteamos.

        //para fines didactivos vamos a devolver un HTTP 200 OK.
        // Utilizamos un DTO llamado "DatosJWTToken"
        // que sera el Usado para regresarle los Datos al Usuario.
        return ResponseEntity.ok(new DatosJWTToken(JWTtoken));
    }
}
