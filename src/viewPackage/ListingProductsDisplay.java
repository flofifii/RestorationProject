package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.ArrayList;

public class ListingProductsDisplay extends JPanel {
    private ProductController productController;

    public void setProductController(ProductController deleteSongController) {
        this.productController = productController;
    }

    public ListingProductDisplay(MainWindow window) {
        setProductController(new ProductController());
        JTable table;
        try {
            ArrayList<Product> products = ProductController.getAllProducts();
            TableModel tableModel = new TableModel<>(products, "products");
            table = new JTable(tableModel);
            window.addScrollPane(new JScrollPane(table));
        }
        catch (ConnectionException | TitleException e){
            JOptionPane.showMessageDialog (null, e.getMessage(),"Erreur", JOptionPane.ERROR_MESSAGE) ;
        }

    }
}
