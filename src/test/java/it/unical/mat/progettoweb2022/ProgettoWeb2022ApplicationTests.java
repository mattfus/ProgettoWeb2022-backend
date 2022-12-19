package it.unical.mat.progettoweb2022;

import it.unical.mat.progettoweb2022.model.User;
import it.unical.mat.progettoweb2022.persistenza.DBManager;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class ProgettoWeb2022ApplicationTests {

    @Test
    void userDAOworks() {
        //FINDALL
        List<User> lista = DBManager.getInstance().getUserDao().findAll();
        for(User u : lista){
            System.out.println(u.toString());
        }

        //FINDBYPRIMARYKEY
        User user = DBManager.getInstance().getUserDao().findByPrimaryKey("calzino22");
        System.out.println(user.toString());

        //UPDATEorSAVE
        User user2 = new User();
        user2.setName("Matteo");
        user2.setLastname("Fusaro");
        user2.setAge(23);
        user2.setNickname("matt320");
        user2.setPassword("password");
        user2.setEmail("email");
        user2.setRole("admin");
        user2.setBanned(false);

        DBManager.getInstance().getUserDao().saveOrUpdate(user2);
        System.out.println(user2.toString());
    }

}
