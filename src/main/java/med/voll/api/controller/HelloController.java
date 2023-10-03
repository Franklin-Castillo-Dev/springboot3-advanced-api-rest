package med.voll.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Usamos REST Controller ya que solamente retornamos JSON.
// en ningun momento retornaremos algun HTML
@RestController
@RequestMapping("/hello")
public class HelloController {
    @GetMapping
    public String helloWorld(){
        return "Hello World";
    }
}
