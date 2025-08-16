package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Order;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class OrderPanel extends JPanel {
    private OrderController controller;

    private final JPanel contentPanel;            // zone centrale interchangeable
    private ListingOrderPanel listingPanel;       // garder la référence à la liste

    public OrderPanel() {
        setController(new OrderController());
        setLayout(new BorderLayout());

        // ===== Barre de boutons (nord)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnShow = new JButton("Afficher les commandes");
        JButton btnNew = new JButton("Nouvelle commande");
        JButton btnUpdate = new JButton("Modifier commande");
        JButton btnDelete = new JButton("Supprimer commande");

        topPanel.add(btnShow);
        topPanel.add(btnNew);
        topPanel.add(btnUpdate);
        topPanel.add(btnDelete);

        add(topPanel, BorderLayout.NORTH);

        // ===== Zone centrale vide au début
        contentPanel = new JPanel(new BorderLayout());
        add(contentPanel, BorderLayout.CENTER);

        // ===== Actions boutons
        btnShow.addActionListener(e -> showListing());
        btnNew.addActionListener(e -> showCreate());
        btnUpdate.addActionListener(e -> showUpdate());
        btnDelete.addActionListener(e -> showDelete());
    }

    public void setController(OrderController controller) {
        this.controller = controller;
    }

    /** Affiche la liste des commandes */
    private void showListing() {
        contentPanel.removeAll();
        listingPanel = new ListingOrderPanel(); // on garde la référence
        contentPanel.add(listingPanel, BorderLayout.CENTER);
        refresh();
    }

    /** Ouvre un dialog de création */
    private void showCreate() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        CreateOrderDialog dialog = new CreateOrderDialog(parent);
        dialog.setVisible(true); // Ouvre la fenêtre

        // si la liste est affichée, on la rafraîchit
        if (listingPanel != null) {
            listingPanel.refresh();
        }
    }

    /** Ouvre un dialog de modification */
    private void showUpdate() {
        Integer number = askUserToSelectOrderNumber();
        if (number == null) return;

        Window parent = SwingUtilities.getWindowAncestor(this);
        EditOrderDialog dialog = new EditOrderDialog(parent, number);
        dialog.setVisible(true);

        if (listingPanel != null) {
            listingPanel.refresh();
        }
    }

    /** Supprimer une commande */
    private void showDelete() {
        Integer number = askUserToSelectOrderNumber();
        if (number == null) return;

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Supprimer définitivement la commande n° " + number + " ?",
                "Confirmation suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );
        if (confirm != JOptionPane.YES_OPTION) return;

        try {
            controller.deleteOrder(number);
            JOptionPane.showMessageDialog(this, "Commande supprimée.");

            if (listingPanel != null) {
                listingPanel.refresh();
            }
        } catch (ConnectionException | TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    /** Ouvre une liste des numéros de commandes et retourne le choix (ou null si annulé) */
    private Integer askUserToSelectOrderNumber() {
        try {
            ArrayList<Order> orders = controller.getAllOrders();
            if (orders.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucune commande disponible.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }

            Integer[] numbers = orders.stream()
                    .map(Order::getNumber)
                    .toArray(Integer[]::new);

            Object selected = JOptionPane.showInputDialog(
                    this,
                    "Choisis la commande :",
                    "Sélection commande",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    numbers,
                    numbers[0]
            );

            if (selected == null) return null;
            return (Integer) selected;

        } catch (ConnectionException | TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Utilitaire pour rafraîchir l'UI */
    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
