package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;

public class ListingProductsDisplay extends JPanel {
    private ProductController productController;

    public void setProductController(ProductController productController) {
        this.productController = productController;
    }

    public ListingProductsDisplay(MainWindow window) {
        setProductController(new ProductController());

        JTable table;
        try {
            ArrayList<Product> products = productController.getAllProducts();

            // Création du modèle de table
            String[] columnNames = { "Nom", "Fournisseur", "Catégorie", "Stock" };
            DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

            // Ajout des données
            for (Product p : products) {
                Object[] rowData = {
                        p.getName(),
                        p.getSupplier().getName(),
                        p.getCategory().getName(),
                        p.getStockQuantity()
                };
                tableModel.addRow(rowData);
            }

            table = new JTable(tableModel);
            window.addScrollPane(new JScrollPane(table));
        }
        catch (ConnectionException | TitleException e){
            JOptionPane.showMessageDialog(null, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}