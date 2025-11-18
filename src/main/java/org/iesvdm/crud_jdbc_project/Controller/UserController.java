package org.iesvdm.crud_jdbc_project.Controller;


import org.iesvdm.crud_jdbc_project.DTO.PiramideDTO;
import org.iesvdm.crud_jdbc_project.model.User;
import org.iesvdm.crud_jdbc_project.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    private UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/logIn")
    public String logInGet(Model model, @ModelAttribute User user){

        model.addAttribute("user",user);

        return "logIn";
    }



    @PostMapping("/logIn")
    public String logInPost(Model model, @ModelAttribute User user){
        List<User> listaClientes = userService.listUsers();
        String nombre = user.getUsername();
        String password = user.getPassword();
        String resultado = "";

        var containNombre = listaClientes.stream().filter(u->u.getUsername().equals(nombre)).toList();
        var containPassword = listaClientes.stream().filter(u->u.getPassword().equals(password)).toList();

        if(containNombre.isEmpty()){
            resultado+="El usuario no está registrado";
            model.addAttribute("resultado",resultado);
            return "logIn";
        }else if(containPassword.isEmpty()){
            resultado+="La contraseña es incorrecta";
            model.addAttribute("resultado",resultado);
            return "logIn";
        }else{
            resultado+="Acceso Permitido a la aplicación";
            model.addAttribute("resultado",resultado);
            return "accesoPermitido";
        }


    }


    @GetMapping("/accesoPermitido")
    public String accesoPermitidoGet(){

        return "/accesoPermitido";
    }

    @PostMapping("/accesoPermitido")
    public String accesoPermitidoPost(){

        return "/piramide";
    }




    @GetMapping("/piramide")
    public String piramideGet(Model model, @ModelAttribute PiramideDTO piramideDTO){

        model.addAttribute("piramideDTO",piramideDTO);

        return "/piramide";
    }

    @PostMapping("/piramide")
    public String piramidePost(Model model,@ModelAttribute PiramideDTO piramideDTO){
        int altura = piramideDTO.getAltura();
        List<Integer> repeticiones = IntStream.rangeClosed(1, altura)
                .boxed()
                .collect(Collectors.toList());

        model.addAttribute("altura",altura);
        model.addAttribute("repeticiones",repeticiones);

        return "/piramideGatos";
    }


}

