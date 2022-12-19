package it.unical.mat.progettoweb2022.persistenza;

import it.unical.mat.progettoweb2022.persistenza.DAO.AdDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.ImageDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.PropertyDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.UserDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.postgresql.ImageDAOpostgres;
import it.unical.mat.progettoweb2022.persistenza.DAO.postgresql.PropertyDAOpostgres;
import it.unical.mat.progettoweb2022.persistenza.DAO.postgresql.UserDAOpostgres;
import it.unical.mat.progettoweb2022.persistenza.DAO.postgresql.AdDAOpostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class DBManager {
    private static DBManager INSTANCE = null;
    Connection conn = null;

    private DBManager(){

    }

    public static DBManager getInstance(){
        if(INSTANCE == null) {
            INSTANCE = new DBManager();
        }
        return INSTANCE;
    }

    private Connection getConnection(){
        if (this.conn == null){
            try {
                this.conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/progettoWeb2022", "postgres", "fellowes");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return this.conn;
    }

    public UserDAO getUserDao(){
        return new UserDAOpostgres(getConnection());
    }

    public AdDAO getAdDao() {
        return new AdDAOpostgres(getConnection());
    }

    public PropertyDAO getPropertyDao(){
        return new PropertyDAOpostgres(getConnection());
    }

    public ImageDAO getImageDao() {
        return new ImageDAOpostgres(getConnection());
    }
}
