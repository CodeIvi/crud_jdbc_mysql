package org.iesvdm.crud_jdbc_project.Controller;


import jakarta.servlet.http.HttpSession;
import org.iesvdm.crud_jdbc_project.CrudJdbcProjectApplication;
import org.iesvdm.crud_jdbc_project.DAO.UserDAO;
import org.iesvdm.crud_jdbc_project.DTO.PiramideDTO;
import org.iesvdm.crud_jdbc_project.model.User;
import org.iesvdm.crud_jdbc_project.service.UserService;
import org.iesvdm.crud_jdbc_project.util.HashUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
public class UserController {
    private HashUtil hashUtil;

    private UserService userService;

    public UserController(UserService userService, HashUtil hashUtil) {
        this.userService = userService;
        this.hashUtil = hashUtil;
    }

    @GetMapping("/logIn")
    public String logInGet(Model model, @ModelAttribute User user) {

        model.addAttribute("user", user);

        return "logIn";
    }


    @PostMapping("/logIn")
    public String logInPost(Model model, @ModelAttribute User user, HttpSession httpSession) {
        List<User> listaClientes = userService.listUsers();
        String nombre = user.getUsername();
        String password = user.getPassword();
        String resultado = "";
        String pagina = null;

        try {
            String pasHash = hashUtil.hashPassword(password);

            var containNombre = listaClientes.stream().filter(u -> u.getUsername().equals(nombre)).toList();
            var containPassword = listaClientes.stream().filter(u -> u.getPassword().equals(pasHash)).toList();

            if (containNombre.isEmpty()) {
                resultado += "El usuario no está registrado";
                model.addAttribute("resultado", resultado);
                return "logIn";
            } else if (containPassword.isEmpty()) {
                resultado += "La contraseña es incorrecta";
                model.addAttribute("resultado", resultado);
                return "logIn";
            } else if (containNombre.get(0).isAdmin()) {
                resultado += "Acceso Permitido a la aplicación";
                model.addAttribute("resultado", resultado);
                httpSession.setAttribute("esAdmin", true);
                return "accesoPermitido";

            }

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return "piramide";
    }

    @GetMapping("/controlacceso")
    public String comprobar(Model model,HttpSession httpSession){
        Boolean isAdmin = (Boolean) httpSession.getAttribute("esAdmin");

        if(isAdmin != null && isAdmin){

            List<User>usuarios = userService.listUsers();

            model.addAttribute("usuarios",usuarios);
            model.addAttribute("usuario", new User() );

            return "pagadmin";
        }

        return "piramide";
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

        return "piramideGatos";
    }




    @PostMapping("/pagadmin")
    public String nuevoUserPost(Model model,@ModelAttribute User user) throws NoSuchAlgorithmException {
        String nombre = user.getUsername();
        String pass = user.getPassword();

        User nuevoUser = User.builder().username(nombre).password(hashUtil.hashPassword(pass)).isAdmin(false).build();
        userService.nuevoUsuario(nuevoUser);

        List<User>usuarios = userService.listUsers();

        model.addAttribute("usuarios",usuarios);
        model.addAttribute("usuario", new User());

        model.addAttribute("resultado","Usuario creado");

        return "pagadmin";
    }

}

