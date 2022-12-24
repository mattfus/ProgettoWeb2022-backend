package it.unical.mat.progettoweb2022.controller;

import it.unical.mat.progettoweb2022.model.Ad;
import it.unical.mat.progettoweb2022.model.User;
import it.unical.mat.progettoweb2022.persistenza.DAO.AdDAO;
import it.unical.mat.progettoweb2022.persistenza.DBManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200")
public class Service {

    @GetMapping("/ads")
    public List<Ad> getAds(){
        System.out.println("DAMMI ANNUNCI ________________");
        AdDAO dao = DBManager.getInstance().getAdDao();
        return dao.findAll();
    }

    @GetMapping("/user")
    public User getNickname(HttpServletRequest req, @RequestParam String parameter, @RequestParam Boolean bySession){
        System.out.println("DAMMI USER __________________");
        User user = null;
        if(bySession) {
            HttpSession session = (HttpSession) req.getServletContext().getAttribute(parameter);
            user = (User) session.getAttribute("user");
        }else{
            user = DBManager.getInstance().getUserDao().findByPrimaryKey(parameter);
        }
        return user;
    }
}
