package org.example;


import org.example.data.AppUserDaoJDBC;
import org.example.model.AppUser;

public class App
{
    public static void main( String[] args ) {
        AppUserDaoJDBC dao = new AppUserDaoJDBC();

        //AppUser erik = dao.findById(1).orElseThrow(RuntimeException::new);
        //erik.setPassword("koolKid");
        //erik = dao.update(erik);

        //System.out.println(erik.getPassword());

        System.out.println(dao.findById(1));
    }
}
