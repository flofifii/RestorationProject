package businessPackage;

import dataAccess.ProductDAO;
import dataAccess.RequestDB;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import java.util.ArrayList;

public class ProductManager {
    private ProductDAO dao;

    public ProductManager() {
        setDAO(new RequestDB());
    }

    public void setDAO(RequestDB dao) {
        this.dao = dao;
    }

    public ArrayList<Product> getAllProducts() throws ConnectionException, TitleException {
        return dao.getAllProducts();
    }
}
