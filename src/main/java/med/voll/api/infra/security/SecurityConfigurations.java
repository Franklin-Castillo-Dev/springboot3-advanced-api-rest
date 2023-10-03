package med.voll.api.infra.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Esta notacion hace que Spring sepa que deba escanear este archivo
// con prioridad en el orden de ejecucion. porque se entiende que es un pre requisito para los demas elementos de la app.
@Configuration
@EnableWebSecurity // Habilitar modulo de Web Security para esta configuracion. Para sobreescribir el comportamiento por defecto.
public class SecurityConfigurations {

    @Autowired
    private SecurityFilter securityFilter;

    @Bean //obligatorio definirlo como un Bean, para que Spring lo tenga en su contexto.
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        // Nuevo codigo para evitar los "Deprecated"
        return httpSecurity.csrf(csrf -> csrf.disable()) // desactivamos porque nuestra app es Stateless, muy util para las Statefull para evitar suplantacion identidad.
                .sessionManagement(sess -> sess
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Le indicamos a Spring el tipo de SESION STATELESS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.POST, "/login").permitAll() // Si vienen Request de tipo POST del URI /login, le permites todos.
                        .anyRequest().authenticated()) // Los demas REQUEST, SI PEDIRAN SER AUTENTIFICADOS. (Implementar esto, nos OBLIGA A FORZAR SESIONES Y LLAMAR PRIMERO NUESTRO FILTRO.)
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class) // Le decimos que primero ocuparemos el filtro de "securityFilter",
                                                                                            // y que solamente usuarios autentificados.
                                                                                            // (Pero nosotros tenemos un proyecto STATELESS, asi que vamos a configurar nuestro SecurityFilter para solucionarlo)
                .build();

        /* Codigo Deprectado.
        return httpSecurity.csrf().disable() // desactivamos porque nuestra app es Stateless, muy util para las Statefull para evitar suplantacion identidad.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)// Establecemos que nuestra Politica de Creacion de Session sea STATELESS
                .and().build();

        //Sale en rojo porque estan "Deprecated". Significa que para la proxima version ya no estaran disponibles. Tocara buscar los nuevos metodos.
         */

    }

    @Bean //obligatorio definirlo como un Bean, para que Spring lo tenga en su contexto.
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();

    }

    @Bean //obligatorio definirlo como un Bean, para que Spring lo tenga disponible en su contexto.
    public PasswordEncoder passwordEncoder (){
        // En el tipo de retorno ya establecemos que sera con Bcrypt nuestra encriptacion
        return new BCryptPasswordEncoder();
    }

}
