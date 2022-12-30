package it.unical.mat.progettoweb2022.persistenza.DAO.postgresql;

import it.unical.mat.progettoweb2022.model.Ad;
import it.unical.mat.progettoweb2022.persistenza.DAO.AdDAO;
import it.unical.mat.progettoweb2022.persistenza.DBManager;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdDAOpostgres implements AdDAO {

    private Connection conn;

    public AdDAOpostgres(Connection conn){
        this.conn = conn;
    }

    @Override
    public List<Ad> findAll() {
        List<Ad> adList =  null;
        String query = "SELECT * FROM ads";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            adList = new ArrayList<Ad>();
            while(rs.next()){
                Ad ad = new Ad();
                ad.setId(rs.getInt("id"));
                ad.setTitle(rs.getString("title"));
                ad.setDescription(rs.getString("description"));
                ad.setUser(DBManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("user")));
                ad.setProperty(DBManager.getInstance().getPropertyDao().findByPrimaryKey(rs.getInt("property")));
                ad.setPrice(rs.getDouble("price"));
                ad.setMq(rs.getDouble("mq"));
                ad.setStatus(rs.getString("status"));
                ad.setCity(rs.getString("city"));

                adList.add(ad);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return adList;
    }

    @Override
    public Ad findByPrimaryKey(Integer id) {
        Ad ad = null;
        String query = "SELECT * FROM ads WHERE id = ?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                ad = new Ad();
                ad.setId(rs.getInt("id"));
                ad.setTitle(rs.getString("title"));
                ad.setDescription(rs.getString("description"));
                ad.setUser(DBManager.getInstance().getUserDao().findByPrimaryKey(rs.getString("user")));
                ad.setProperty(DBManager.getInstance().getPropertyDao().findByPrimaryKey(rs.getInt("property")));
                ad.setPrice(rs.getDouble("price"));
                ad.setMq(rs.getDouble("mq"));
                ad.setStatus(rs.getString("status"));
                ad.setCity(rs.getString("city"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return ad;
    }

    @Override
    public void saveOrUpdate(Ad ad) {
        if(ad.getId() != null){ //UPDATE
            String query = "UPDATE ads SET title=?," +
                    " description =?," +
                    " user =?," +
                    " property =?," +
                    " price =?," +
                    " mq =?," +
                    " status =?," +
                    " city =? WHERE id =?";
            try {
                PreparedStatement st = conn.prepareStatement(query);
                st.setString(1, ad.getTitle());
                st.setString(2, ad.getDescription());
                st.setString(3, ad.getUser().getNickname());
                st.setInt(4, ad.getProperty().getId());
                st.setDouble(5, ad.getPrice());
                st.setDouble(6, ad.getMq());
                st.setString(7, ad.getStatus());
                st.setString(8, ad.getCity());

                st.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{ //INSERT
            String query = "INSERT INTO ads VALUES(DEFAULT, ?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, ad.getTitle());
                st.setString(2, ad.getDescription());
                st.setString(3, ad.getUser().getNickname());
                st.setInt(4, ad.getProperty().getId());
                st.setDouble(5, ad.getPrice());
                st.setDouble(6, ad.getMq());
                st.setString(7, ad.getStatus());
                st.setString(8, ad.getCity());

                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    ad.setId(rs.getInt("id"));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void delete(Ad ad) {
        String query = "DELETE FROM ads WHERE id =?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setInt(1, ad.getId());

            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
