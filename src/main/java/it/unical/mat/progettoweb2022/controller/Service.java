package it.unical.mat.progettoweb2022.controller;

import it.unical.mat.progettoweb2022.Protocol;
import it.unical.mat.progettoweb2022.model.Ad;
import it.unical.mat.progettoweb2022.model.Image;
import it.unical.mat.progettoweb2022.model.Property;
import it.unical.mat.progettoweb2022.model.User;
import it.unical.mat.progettoweb2022.persistenza.DAO.AdDAO;
import it.unical.mat.progettoweb2022.persistenza.DBManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.w3c.dom.html.HTMLFormElement;

import java.io.*;
import java.net.http.HttpRequest;
import java.text.Normalizer;
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

    @GetMapping("/searchAds")
    public List<Ad> getAdsWithFilter(@RequestParam String searchType, @RequestParam String parameter){
        List<Ad> adList = new ArrayList<Ad>();
        AdDAO dao = DBManager.getInstance().getAdDao();
        List<Ad> tempList = dao.findAll();
        if(searchType.equals("Città")) {
            for(Ad ad : tempList){
                if(ad.getCity().contains(parameter) || ad.getCity().equals(parameter))
                    adList.add(ad);
            }
        }else if(searchType.equals("Titolo")){
            for(Ad ad : tempList){
                if(ad.getTitle().contains(parameter))
                    adList.add(ad);
            }
        }else if(searchType.equals("Contenuto")){
            for(Ad ad : tempList){
                if(ad.getTitle().contains(parameter) || ad.getDescription().contains(parameter)){
                    adList.add(ad);
                }
            }
        }

        for(Ad ad : adList){
            System.out.println(ad.getTitle());
        }
        return adList;
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
        System.out.println(user.getBanned());
        return user;
    }

    @PostMapping("/addAd")
    @CrossOrigin("http://localhost:4200/addAd")
    public boolean addAd(HttpServletRequest req,
                        @RequestParam String sessionId, @RequestParam String title,
                        @RequestParam String description, @RequestParam String type,
                        @RequestParam String mq, @RequestParam String latitude,
                        @RequestParam String longitude,
                        @RequestParam String price,
                        @RequestParam String status,
                        @RequestParam @RequestBody byte[] media){

        HttpSession session = (HttpSession) req.getServletContext().getAttribute(sessionId);
        User user = (User) session.getAttribute("user");

        Property property = new Property();
        property.setType(type);
        property.setMq(Double.parseDouble(mq));
        property.setLatitude(latitude);
        property.setLongitude(longitude);
        property.setUser(user.getNickname());

        DBManager.getInstance().getPropertyDao().saveOrUpdate(property);

        Ad ad = new Ad();
        ad.setTitle(title);
        ad.setDescription(description);
        ad.setUser(user);
        ad.setProperty(property);
        ad.setPrice(Double.parseDouble(price));
        ad.setMq(Double.parseDouble(mq));

        DBManager.getInstance().getAdDao().saveOrUpdate(ad);

        Image image = new Image();
        image.setAd(ad.getId());
        image.setData(media);

        DBManager.getInstance().getImageDao().saveOrUpdate(image);

        return true;
    }
}
