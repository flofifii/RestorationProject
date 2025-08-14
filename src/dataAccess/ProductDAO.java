package dataAccess;

import exceptionPackage.*;
import model.Product;

import java.util.ArrayList;

public interface ProductDAO {
    //ArrayList<String> getProduct() throws ConnectionException;
    ArrayList<Product> getAllProducts() throws ConnectionException, TitleException;

    Product getProductByName(String name) throws ConnectionException, TitleException;
    void createProduct(Product product) throws ConnectionException, TitleException;
    //void updateProduct(Product product) throws ConnectionException, TitleException;
    //void deleteProduct(String name) throws ConnectionException, TitleException;
}

