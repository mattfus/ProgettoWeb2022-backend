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
        AdDAO dao = DBManager.getInstance().getAdDao();
        return dao.findAll();
    }

    @GetMapping("/nickname")
    public List<String> getNickname(HttpServletRequest req, @RequestParam String sessionId){
        HttpSession session = (HttpSession) req.getServletContext().getAttribute(sessionId);
        System.out.println("HO CERCATO L'UTENTE " + session.getId());
        User user = (User) session.getAttribute("user");
        List<String> list = new ArrayList<String>();
        list.add(user.getNickname());
        return list;
    }
}
