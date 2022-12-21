package it.unical.mat.progettoweb2022.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;

@ControllerAdvice
@Controller
@CrossOrigin("http://localhost:4200")
public class Auth {

    @GetMapping ("/login")
    public String login(){
        return "loginPage.html";
    }

    @PostMapping("/login")
    public void doLogin(HttpServletResponse httpResponse, @RequestParam String email){
        System.out.println(email);
        try {
            httpResponse.sendRedirect("http://localhost:4200"); //redirect to Angular
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @GetMapping ("/register")
    public String register() {
        return "registerPage.html";
    }

    @PostMapping ("/register")
    public void doRegister(@RequestParam String nickname){

        System.out.println(nickname );
    }
}
