package it.unical.mat.progettoweb2022.controller;

import com.google.api.services.drive.model.FileList;
import it.unical.mat.progettoweb2022.model.*;
import it.unical.mat.progettoweb2022.persistenza.DAO.AdDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.PropertyDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.ReviewDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.UserDAO;
import it.unical.mat.progettoweb2022.persistenza.DBManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:4200/")
public class Service {

    @GetMapping("/ads")
    public List<Ad> getAds(){
        System.out.println("PRENDO AD");
        AdDAO dao = DBManager.getInstance().getAdDao();
        return dao.findAll();
    }

    @GetMapping("/ad")
    public Ad getAd(@RequestParam String adId){
        AdDAO dao = DBManager.getInstance().getAdDao();
        return dao.findByPrimaryKey(Integer.parseInt(adId));
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
    public boolean addAd(HttpServletRequest req, @RequestParam String sessionId, @RequestParam String title,
                         @RequestParam String description, @RequestParam String type, @RequestParam String mq,
                         @RequestParam String latitude, @RequestParam String longitude, @RequestParam String price,
                         @RequestParam String status, @RequestBody ArrayList<LinkedHashMap> media){

        HttpSession session = (HttpSession) req.getServletContext().getAttribute(sessionId);
        User user = (User) session.getAttribute("user");

        try {
            /*
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
            if (status.equals("Affitto"))
                ad.setStatus("affittasi");
            else {
                ad.setStatus("vendesi");
            }

            DBManager.getInstance().getAdDao().saveOrUpdate(ad);

            property.setAd(ad.getId());
            DBManager.getInstance().getPropertyDao().saveOrUpdate(property);
            */


        }catch(Exception e){
            return false;
        }

        return true;
    }

    @GetMapping("/getImage")
    public List<byte[]> getImage(HttpServletRequest request, @RequestParam String adId){
        List<Image> imageList = DBManager.getInstance().getImageDao().findByAdId(Integer.parseInt(adId));
        List<byte[]> imgList = new ArrayList<>();
        for (Image image : imageList) {
            imgList.add(image.getData());
        }
        return imgList;
    }

    @GetMapping("/newReview")
    @CrossOrigin("http://localhost:4200")
    public boolean addReview(HttpServletRequest req, @RequestParam String title, @RequestParam String description,
                              @RequestParam Integer rating, @RequestParam String user, @RequestParam Integer ad){

        System.out.println(user);
        Review review = new Review();
        review.setTitle(title);
        review.setDescription(description);
        review.setVote(rating);
        try{
            review.setUser(DBManager.getInstance().getUserDao().findByPrimaryKey(user));
            review.setAd(DBManager.getInstance().getAdDao().findByPrimaryKey(ad));
            ReviewDAO dao = DBManager.getInstance().getReviewDao();
            dao.saveOrUpdate(review);
            return true;
        }catch(Exception e){
            return false;
        }
    }

    @GetMapping("/getReviews")
    public List<Review> getReviews(@RequestParam Integer adId){
        System.out.println("prendo reviews");
        ReviewDAO dao = DBManager.getInstance().getReviewDao();
        return dao.findByAdId(adId);
    }

    @GetMapping("/property")
    public Property getProperty(@RequestParam Integer adId){
        PropertyDAO dao = DBManager.getInstance().getPropertyDao();
        return dao.findByAdId(adId);
    }

    @GetMapping("/removeReview")
    public boolean removeReview(@RequestParam Integer reviewId){
        System.out.println("Rimuovo review");
        ReviewDAO dao = DBManager.getInstance().getReviewDao();
        return dao.delete(reviewId);
    }

    @GetMapping("/banUser")
    public boolean banUser(@RequestParam String nickname){
        UserDAO dao = DBManager.getInstance().getUserDao();
        User user = dao.findByPrimaryKey(nickname);
        user.setBanned(!user.getBanned());
        try {
            dao.saveOrUpdate(user);
        }catch (Exception e) {
            return false;
        }
        return true;
    }

    @GetMapping("/updateUser")
    public boolean updateUser(@RequestParam String nickname, @RequestParam String name,
        @RequestParam String lastname,@RequestParam String telephone, @RequestParam String email,
        @RequestParam String state, @RequestParam String country, @RequestParam String address,
        @RequestParam String postalCode, @RequestParam String password){

        try {
            UserDAO dao = DBManager.getInstance().getUserDao();
            User user = dao.findByPrimaryKey(nickname);
            user.setName(name);
            user.setLastname(lastname);
            user.setTelephone(telephone);
            user.setEmail(email);
            user.setState(state);
            user.setCountry(country);
            user.setAddress(address);
            user.setPostalCode(postalCode);
            if(password.length() > 8){
                user.setPassword(password);
            }

            dao.saveOrUpdate(user);
        }catch(Exception e){
            return false;
        }
        return true;
    }

    @PostMapping("/upload")
    @CrossOrigin("http://localhost:4200/addAd")
    public ResponseEntity<String> sendFileList(MultipartFile data) {
        // do something with the fileList here
        System.out.println(data);

        return new ResponseEntity<>("Success", HttpStatus.OK);
    }
}
