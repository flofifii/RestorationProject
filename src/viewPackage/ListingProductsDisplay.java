package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class ListingProductsDisplay extends JPanel {
    private final ProductController productController = new ProductController();
    private final DefaultTableModel tableModel;

    public ListingProductsDisplay() {
        setLayout(new BorderLayout());

        // Barre d’actions interne
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton loadBtn = new JButton("Afficher les produits");
        actions.add(loadBtn);
        add(actions, BorderLayout.NORTH);

        // Tableau vide au départ
        String[] columns = {"Nom", "Fournisseur", "Catégorie", "Stock"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Action bouton
        loadBtn.addActionListener(e -> loadProducts());
    }

    private void loadProducts() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Product> products = productController.getAllProducts();
            for (Product p : products) {
                tableModel.addRow(new Object[]{
                        p.getName(),
                        (p.getSupplier() != null) ? p.getSupplier().getName() : "",
                        (p.getCategory() != null) ? p.getCategory().getName() : "",
                        p.getStockQuantity()
                });
            }
        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}