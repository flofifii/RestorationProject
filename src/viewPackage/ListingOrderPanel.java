package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Order;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ListingOrderPanel extends JPanel {

    private final OrderController controller = new OrderController();
    private final DefaultTableModel tableModel;
    private final JTable table;
    private final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ListingOrderPanel() {
        setLayout(new BorderLayout());

        // Colonnes à afficher
        String[] columns = {
                "Numéro", "Client", "Paiement", "À emporter",
                "Date création", "Date livraison",
                "Commentaire", "Demande spécifique"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };

        table = new JTable(tableModel);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // Chargement initial
        refresh();
    }

    /** Recharge la liste des commandes depuis le controller. */
    public void refresh() {
        tableModel.setRowCount(0);
        try {
            ArrayList<Order> orders = controller.getAllOrders();

            for (Order o : orders) {
                tableModel.addRow(new Object[]{
                        o.getNumber(),
                        (o.getcustomer() != null) ? o.getcustomer().getFirstName() + " " + o.getcustomer().getLastName() : "",
                        o.getPaymentMethod(),
                        (o.isTakeaway() != null && o.isTakeaway()) ? "Oui" : "Non",
                        (o.getCreationDate() != null) ? o.getCreationDate().format(df) : "",
                        (o.getDeliveryDate() != null) ? o.getDeliveryDate().format(df) : "",
                        (o.getCommentary() != null) ? o.getCommentary() : "",
                        (o.getSpecificRequest() != null) ? o.getSpecificRequest() : ""
                });
            }
        } catch (ConnectionException | TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}