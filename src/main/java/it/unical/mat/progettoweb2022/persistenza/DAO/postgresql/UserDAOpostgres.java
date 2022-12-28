package it.unical.mat.progettoweb2022.persistenza.DAO.postgresql;

import it.unical.mat.progettoweb2022.model.User;
import it.unical.mat.progettoweb2022.persistenza.DAO.UserDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOpostgres implements UserDAO {
    Connection conn;

    public UserDAOpostgres(Connection conn){
        this.conn = conn;
    }


    @Override
    public List<User> findAll() {
        List<User> users = null;
        String query = "SELECT * FROM users";
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            users = new ArrayList<User>();
            while(rs.next()){
                User user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                user.setNickname(rs.getString("nickname"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setBanned(rs.getBoolean("isbanned"));

                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public User findByPrimaryKey(String nickname) {
        User user = null;
        String query = "SELECT * FROM users WHERE nickname =?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, nickname);
            ResultSet rs = st.executeQuery();
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setLastname(rs.getString("lastname"));
                user.setAge(rs.getInt("age"));
                user.setNickname(rs.getString("nickname"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setRole(rs.getString("role"));
                user.setBanned(rs.getBoolean("isbanned"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    @Override
    public boolean saveOrUpdate(User user) {
        String query;
        if(user.getId() != null) { //UPDATE if exists
            query = "UPDATE users SET " +
                    " name = ?," +
                    " lastname = ?," +
                    " age = ?," +
                    " password = ?," +
                    " email = ?," +
                    " role = ?," +
                    " isbanned = ?" +
                    " WHERE nickname = ?";

            try {
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, user.getName());
                st.setString(2, user.getLastname());
                st.setInt(3, user.getAge());
                st.setString(4, user.getPassword());
                st.setString(5, user.getEmail());
                st.setString(6, user.getRole());
                st.setBoolean(7, user.getBanned());
                st.setString(8, user.getNickname());

                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if (rs.next()) {
                    user.setId(rs.getInt("id"));
                    user.setRole(rs.getString("role"));
                    user.setBanned(rs.getBoolean("isbanned"));
                }
            } catch (SQLException e) {
                return false;
            }

        }else{ //INSERT if not exists
            query = "INSERT INTO users VALUES(DEFAULT,?, ?, ?, ?, ?, ?, ?, ?)";
            try {
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                st.setString(1, user.getName());
                st.setString(2, user.getLastname());
                st.setInt(3, user.getAge());
                st.setString(4, user.getNickname());
                st.setString(5, user.getPassword());
                st.setString(6, user.getEmail());
                st.setString(7, user.getRole());
                st.setBoolean(8, false);

                st.executeUpdate();
                ResultSet rs = st.getGeneratedKeys();
                if(rs.next()){
                    user.setId(rs.getInt(1));
                }
            } catch (SQLException e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void delete(User user) {
        String query = "DELETE FROM users WHERE nickname = ?";
        try {
            PreparedStatement st = conn.prepareStatement(query);
            st.setString(1, user.getNickname());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
