package controllerPackage;

import businessPackage.ProductManager;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import java.util.ArrayList;

public class ProductController {
    private ProductManager manager;

    public ProductController() {
        setManager(new ProductManager());
    }

    public void setManager(ProductManager manager) {
        this.manager = manager;
    }


    public ArrayList<Product> getAllProducts() throws ConnectionException, TitleException {
        return manager.getAllProducts();
    }

    public Product getProductByName(String productName) throws ConnectionException, TitleException {
        return manager.getProductByName(productName);
    }

    public void createProduct(Product product) throws ConnectionException, TitleException{
        manager.createProduct(product);
    }

    public void updateProduct(Product product) throws ConnectionException, TitleException{
        manager.updateProduct(product);
    }

    public void deleteProduct(String name) throws ConnectionException, TitleException {
        manager.deleteProduct(name);
    }

}
