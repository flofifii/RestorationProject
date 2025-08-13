package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SingletonConnexion {
    private static Connection connexionUnique;

    public SingletonConnexion(){
    }

    public static Connection getInstance() throws SQLException {
        if (connexionUnique == null) {
            connexionUnique =  DriverManager.getConnection("jdbc:mysql://localhost:3306/restoration", "root", "root");
        }
        return connexionUnique;
    }
}
