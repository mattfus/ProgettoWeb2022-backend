package it.unical.mat.progettoweb2022.controller;

import it.unical.mat.progettoweb2022.Protocol;
import it.unical.mat.progettoweb2022.model.User;
import it.unical.mat.progettoweb2022.persistenza.DBManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@CrossOrigin("http://localhost:4200")
public class Auth {

    @PostMapping("/doLogin")
    @ResponseBody
    public Object doLogin(HttpServletRequest req,HttpServletResponse resp,@RequestParam String nickname, @RequestParam String password){
        System.out.println(nickname);
        System.out.println(password);

        User user = DBManager.getInstance().getUserDao().findByPrimaryKey(nickname);
        System.out.println(user.toString());
        if(user.getId() != null){
            if(user.getPassword().equals(password)){
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
                session.setAttribute("sessionId", session.getId());
                req.getServletContext().setAttribute(session.getId(), user);
                return req.getSession().getId();
            }else{
                return Protocol.INCORRECT_PASSWORD;
            }
        }else{
            return Protocol.USER_NOT_EXISTS;
        }
    }

    @PostMapping("/doRegister")
    @ResponseBody
    public Object doRegister(HttpServletRequest req,HttpServletResponse resp,
                             @RequestParam String nickname,
                             @RequestParam String password){

        System.out.println(nickname + " " + password);
        return Protocol.OK;

    }

}
