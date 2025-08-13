package dataAccess;

import exceptionPackage.ConnectionException;
import model.Product;

import java.util.ArrayList;

public interface ProductDAO {
    //ArrayList<String> getProduct() throws ConnectionException;
    ArrayList<Product> getAllProducts() throws ConnectionException /*TitleException*/;
}
