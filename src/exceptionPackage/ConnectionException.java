package exceptionPackage;

import java.sql.SQLException;

public class ConnectionException extends SQLException {
    public ConnectionException(String message){
        super(message);
    }
}