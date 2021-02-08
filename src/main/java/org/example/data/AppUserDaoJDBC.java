package org.example.data;

import org.example.model.AppUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class AppUserDaoJDBC {

    //CREATE
    /*
        INSERT INTO table_name (column1, column2, column3, ...)
        VALUES (value1, value2, value3, ...);
     */
    public AppUser create(AppUser appUser){
        if(appUser == null) throw new NullPointerException("appUser was: null");
        if(appUser.getAppUserId() != 0) throw new IllegalArgumentException("Aborted because appUser is already persisted");
        AppUser persisted = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet keySet = null;
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("INSERT INTO app_user (first_name, last_name, username, password) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, appUser.getFirstName().trim());
            statement.setString(2, appUser.getLastName().trim());
            statement.setString(3, appUser.getUsername().trim());
            statement.setString(4, appUser.getPassword().trim());
            statement.execute();

            keySet = statement.getGeneratedKeys();
            while(keySet.next()){
                persisted = new AppUser(
                        keySet.getInt(1),
                        appUser.getFirstName(),
                        appUser.getLastName(),
                        appUser.getUsername(),
                        appUser.getPassword()
                );
            }
        }catch (SQLException ex){
            ex.printStackTrace();
        }finally {
            closeAll(keySet, statement, connection);
        }
        if(persisted == null){
            return appUser;
        }else {
            return persisted;
        }
    }

    //READ
    public Optional<AppUser> findById(int appUserId){
        AppUser appUser = null;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("SELECT * FROM app_user WHERE app_user_id = ?");
            statement.setInt(1, appUserId);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                appUser = resultSetToAppUser(resultSet);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            closeAll(resultSet, statement, connection);
        }

        return Optional.ofNullable(appUser);
    }

    public Collection<AppUser> findAll(){
        Collection<AppUser> result = new ArrayList<>();
        try(
                Connection connection = ConnectionFactory.getConnection();
                Statement statement =  connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM app_user")
        ) {
            while (resultSet.next()){
                result.add(resultSetToAppUser(resultSet));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private void closeAll(AutoCloseable...closeables){
        if(closeables != null){
            try{
                for(AutoCloseable closeable : closeables){
                    if(closeable != null){
                        closeable.close();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private AppUser resultSetToAppUser(ResultSet resultSet) throws SQLException {
        return new AppUser(
                resultSet.getInt("app_user_id"),
                resultSet.getString("first_name"),
                resultSet.getString("last_name"),
                resultSet.getString("username"),
                resultSet.getString("password")
        );
    }

    //UPDATE
    /*
        UPDATE table_name
        SET column1 = value1, column2 = value2, ...
        WHERE condition;
     */
    public AppUser update(AppUser appUser){
        if(appUser == null) throw new NullPointerException("appUser was: null");
        if(appUser.getAppUserId() == 0) throw new IllegalArgumentException("Aborted because appUser is not yet persisted");
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("UPDATE app_user SET first_name = ?, last_name = ?, username = ?, password = ? WHERE app_user_id = ?");
            statement.setString(1, appUser.getFirstName());
            statement.setString(2, appUser.getLastName());
            statement.setString(3, appUser.getUsername());
            statement.setString(4, appUser.getPassword());
            statement.setInt(5, appUser.getAppUserId());
            statement.execute();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            closeAll(statement, connection);
        }
        return appUser;
    }

    //REMOVE
    /*
        DELETE FROM table_name WHERE condition;
     */
    public boolean delete(int appUserId){
        boolean deleted = false;
        Connection connection = null;
        PreparedStatement statement = null;
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.prepareStatement("DELETE FROM app_user WHERE app_user_id = ?");
            statement.setInt(1, appUserId);
            int records = statement.executeUpdate();
            deleted = records > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }finally {
            closeAll(statement, connection);
        }
        return deleted;
    }

}
