package med.voll.api.controller;

import jakarta.validation.Valid;
import med.voll.api.domain.dto.direccion.DatosDireccion;
import med.voll.api.domain.dto.medico.DatosActualizarMedico;
import med.voll.api.domain.dto.medico.DatosListadoMedico;
import med.voll.api.domain.dto.medico.DatosRegistroMedico;
import med.voll.api.domain.dto.medico.DatosRespuestaMedico;
import med.voll.api.domain.model.Medico;
import med.voll.api.domain.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/medicos")
public class MedicoController {

    //Por testing no es muy recomendable utilizar @AutoWired por complicaciones. Pref Utilizar otra forma.
    //por fines didacticos y simplificar proceso, utilizaremos ese de momento.
    @Autowired //Inyecta el objeto.
    private MedicoRepository medicoRepository;

    /***
     *
      * @param datosRegistroMedico Obtener JSON utilizando el DTO como Contenedor.
     */
    // Aplicamos Generics, UriComponentsBuilder y DTO de Respuesta.
    @PostMapping
    public ResponseEntity<DatosRespuestaMedico> registrarMedico(@RequestBody @Valid DatosRegistroMedico datosRegistroMedico,
                                          UriComponentsBuilder uriComponentsBuilder){

        // Utilizamos DatosRegistroMedico, para crear un Objeto de Tipo Medico
        // Y ese Objeto Tipo Medico, pasarlo al Repository
        // Ya que nuestro Generics del Repository, solo acepta tipo Medico.
        Medico medico = medicoRepository.save(new Medico(datosRegistroMedico));

        // Creamos DTO y le Pasamos la Entidad.
        DatosRespuestaMedico datosRespuestaMedico = new DatosRespuestaMedico(
                medico.getId(),
                medico.getNombre(),
                medico.getEmail(),
                medico.getTelefono(),
                medico.getEspecialidad().toString(),
                new DatosDireccion(
                        medico.getDireccion().getCalle(),
                        medico.getDireccion().getDistrito(),
                        medico.getDireccion().getCiudad(),
                        medico.getDireccion().getNumero(),
                        medico.getDireccion().getComplemento()
                )
        );

        // REST por convencion internacional debe hacer 2 retornos.
        // Primero: HTTP Status 201 - Created
        // Segundo: en el Header, la URL donde encontrar el registro creado (en este caso el medico).
        // Ej. GET http://localhost:8080/medicos/{id}
        // URI url = "http://localhost:8080/medicos/" + medico.getId(); // Podria Funcionar, pero no es buena practica.
        URI url = uriComponentsBuilder.path("/medicos/{id}").buildAndExpand(medico.getId()).toUri(); // Creamos URI dinamicamente.
        return ResponseEntity.created(url).body(datosRespuestaMedico); // HTTP Status 201 - Created With URI
    }

    // Ocuparemos el DTO DatosActualizarMedico para limitar y definir que campos si se pueden actualizar.
    @PutMapping
    @Transactional // Notacion para que guarde automaticamente en DB despues de completado el metodo siempre que no haya ningun error. sino hace rollback automatico.
    public ResponseEntity actualizarMedico(@RequestBody @Valid DatosActualizarMedico datosActualizarMedico){

        // primero obtenemos instancia de Medico
        Medico medico = medicoRepository.getReferenceById(datosActualizarMedico.id());

        // Actualizamos los Datos del objeto medico con los datos del DTO recibido.
        medico.actualizarDatos(datosActualizarMedico);

        //Vamos a retornar un OK con los datos del Medico como Parametro,
        // pero usaremos un DTO para ello.
        // porque no enviar el objeto mismo? porque es mala practica.
        return ResponseEntity.ok(
                new DatosRespuestaMedico(
                        medico.getId(),
                        medico.getNombre(),
                        medico.getEmail(),
                        medico.getTelefono(),
                        medico.getEspecialidad().toString(),
                        new DatosDireccion(
                                medico.getDireccion().getCalle(),
                                medico.getDireccion().getDistrito(),
                                medico.getDireccion().getCiudad(),
                                medico.getDireccion().getNumero(),
                                medico.getDireccion().getComplemento()
                        )
                )
        ); // Return HTTP Status 200, means ok
    }

    // Get by ID
    // Metodo para Obtener Datos por ID
    @GetMapping("/{id}")
    public ResponseEntity<DatosRespuestaMedico> retornaDatosMedico(@PathVariable Long id){

        // primero obtenemos instancia de Medico
        Medico medico = medicoRepository.getReferenceById(id);

        var datosMedicos =
                new DatosRespuestaMedico(
                        medico.getId(),
                        medico.getNombre(),
                        medico.getEmail(),
                        medico.getTelefono(),
                        medico.getEspecialidad().toString(),
                        new DatosDireccion(
                                medico.getDireccion().getCalle(),
                                medico.getDireccion().getDistrito(),
                                medico.getDireccion().getCiudad(),
                                medico.getDireccion().getNumero(),
                                medico.getDireccion().getComplemento()
                        )
                );

        return ResponseEntity.ok(datosMedicos); // return HTTP Status 204, Means No Content.
    }

    // Delete Logico
    //Metodo para Eliminacion Logica de la base (Setear el Registro como Inactivo, pero no eliminarlo.)
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity eliminarMedico(@PathVariable Long id){
        // primero obtenemos instancia de Medico
        Medico medico = medicoRepository.getReferenceById(id);

        // Desactivamos Medico
        medico.desactivarMedico();

        return ResponseEntity.noContent().build(); // return HTTP Status 204, Means No Content.
    }


    /*
    //Metodo para Eliminar de la DB
    @DeleteMapping("/{id}")
    @Transactional
    public void eliminarMedico(@PathVariable Long id){
        // primero obtenemos instancia de Medico
        Medico medico = medicoRepository.getReferenceById(id);

        //Eliminamos Medico
        medicoRepository.delete(medico);
    }
     */


    // Siempre utilizamos el DTO para filtrar
    // Pero ahora agregamos Paginacion a los resultados.
    @GetMapping
    public ResponseEntity<Page<DatosListadoMedico>> listadoMedicos(@PageableDefault(size = 2) Pageable paginacion){
        // Traemos el Listado de Medicos
        // Luego lo mapeamos utilizando el DTO DatosListadoMedico (Requiere constructor en el DTO para Mapearlo)
        // y lo hacemos una lista
        //return medicoRepository.findAll(paginacion).map(DatosListadoMedico::new);

        //Consulta Personalizada
        //findBy (Es la implementacion de Spring DATA)
        //ActivoTrue (Es el Where que yo quiero que JPA Realice.)
        //Unido ya queda findByActivoTrue();
        return ResponseEntity.ok(medicoRepository.findByActivoTrue(paginacion).map(DatosListadoMedico::new));
    }

    //Utilizaremos el DTO de tipo Record, llamado DatosListadomedico
    //para filtrar que Informacion vamos a Retornar desde la DB a la API
    /*
    @GetMapping
    public List<DatosListadoMedico> listadoMedicos(){
        // Traemos el Listado de Medicos
        // Luego lo mapeamos utilizando el DTO DatosListadoMedico (Requiere constructor en el DTO para Mapearlo)
        // y lo hacemos una lista
        return medicoRepository.findAll().stream().map(DatosListadoMedico::new).toList();
    }
     */

    /*
    //Metodo para Listar toda la informacion de la Base
    @GetMapping
    public List<Medico> ListarTodoMedicos(){
        return medicoRepository.findAll();
    }
     */
}
