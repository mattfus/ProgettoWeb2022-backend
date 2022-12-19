package it.unical.mat.progettoweb2022.persistenza.DAO;

import it.unical.mat.progettoweb2022.model.Ad;
import it.unical.mat.progettoweb2022.model.User;

import java.util.List;

public interface AdDAO {
    public List<Ad> findAll();

    public Ad findByPrimaryKey(Integer id);

    public void saveOrUpdate(Ad ad);

    public void delete(Ad ad);
}
