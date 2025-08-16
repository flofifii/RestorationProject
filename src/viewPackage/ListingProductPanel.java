package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Product;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListingProductPanel extends JPanel {

    private final ProductController controller = new ProductController();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ListingProductPanel() {
        setLayout(new BorderLayout());

        // Colonnes à afficher
        String[] columns = {
                "Nom", "Fournisseur", "Catégorie",
                "Min", "Stock", "Réassort",
                "Date de vente", "Surgelé", "Description"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);

        //table.setFillsViewportHeight(true);
        /*Si jamais il y a moins de lignes que la hauteur visible,
        remplis quand même tout l’espace avec l’arrière-plan du tableau*/

        add(new JScrollPane(table), BorderLayout.CENTER);

        // Chargement initial
        refresh();
    }


    /** Recharge la liste des produits depuis le controller. */
    public void refresh() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Product> products = controller.getAllProducts();

            for (Product p : products) {
                tableModel.addRow(new Object[]{
                        p.getName(),
                        (p.getSupplier() != null) ? p.getSupplier().getName() : "",
                        (p.getCategory() != null) ? p.getCategory().getName() : "",
                        p.getMinQuantityDesired(),
                        p.getStockQuantity(),
                        p.getReorderQuantity(),
                        (p.getDateOfSale() != null) ? p.getDateOfSale().format(df) : "",
                        p.isFrozen() ? "Oui" : "Non",
                        p.getDescription() != null ? p.getDescription() : ""
                });
            }
        } catch (ConnectionException | TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}