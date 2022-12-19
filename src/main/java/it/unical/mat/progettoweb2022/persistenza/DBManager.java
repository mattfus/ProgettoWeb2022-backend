package it.unical.mat.progettoweb2022.persistenza;

import it.unical.mat.progettoweb2022.persistenza.DAO.UserDAO;
import it.unical.mat.progettoweb2022.persistenza.DAO.postgresql.UserDAOpostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

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

    public Connection getConnection(){
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

}
