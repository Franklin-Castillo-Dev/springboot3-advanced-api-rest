package med.voll.api.infra.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import med.voll.api.domain.model.Usuario;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    //Se ocupa esta libreria: import org.springframework.beans.factory.annotation.Value;
    @Value("${api.security.secret}") // Consumir Valor de Application.Properties.
    private String apiSecret;
    public String generarToken(Usuario usuario){
        try {
            // el algoritmo RSA256 es mas complejo ya que requiere dos llaves, una publica y una privada
            // por lo cual, de momento usaremos uno mas sencillo, el algoritmo HMAC256
            // Nunca dejar los Secrets / Llaves Criptograficas / Contras / etc, en codigo. ES MALA PRACTICA.
            // en esta ocasion por fines didacticos va asi, pero hay otros metodos para obtenerlas de mejor manera.
            Algorithm algorithm = Algorithm.HMAC256(apiSecret);
            return JWT.create()
                    .withIssuer("vollmed-api") // el nombre de quien emite el token. (deberia ir dinamico, no quemado.)
                    .withSubject(usuario.getLogin()) // hacia quien va dirijido.
                    .withClaim("id", usuario.getId()) // para enviar datos personalizados al cliente.
                    .withExpiresAt(generarFechaExpiracion()) // Vencimiento del token.
                    .sign(algorithm);
        } catch (JWTCreationException exception){
            throw new RuntimeException();
        }
    }

    public String getSubject(String token){

        // Verificar que token no llegue nulo
        if(token == null){
            throw new RuntimeException("Token Nulo en Validacion Token.");
        }

        DecodedJWT verifier = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(apiSecret); // Validamos Firma
            verifier = JWT.require(algorithm)
                    // specify an specific claim validations
                    .withIssuer("vollmed-api")
                    // reusable verifier instance
                    .build()
                    .verify(token);

        } catch (JWTVerificationException exception){
            System.out.printf(exception.toString());
        }
        // validacion null - Metodo 1
        //assert verifier != null;

        //validacion null - Metodo If
        if(verifier.getSubject() == null){
            throw new RuntimeException("verifier Invalido.");
        }

        return verifier.getSubject();
    }

    private Instant generarFechaExpiracion(){
        // -06-00 indica GMT-0600 HORA ESTANDAR CENTRAL (la de centroamerica).
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-06:00"));
    }
}
