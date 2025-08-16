package viewPackage;

import controllerPackage.ProductController;

import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel {
    private ProductController controller;

    private final JPanel contentPanel; // zone centrale interchangeable
    private ListingProductPanel listingPanel; // garder la référence à la liste

    public ProductPanel() {
        setController(new ProductController());
        setLayout(new BorderLayout());

        // ===== Barre de boutons (nord)
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnShow = new JButton("Afficher les produits");
        JButton btnNew = new JButton("Nouveau produit");
        JButton btnUpdate = new JButton("Modifier produit");
        JButton btnDelete = new JButton("Supprimer produit");

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


        // update et delete après
    }
    public void setController(ProductController controller) {
        this.controller = controller;
    }


    /** Affiche la liste des produits */
    private void showListing() {
        contentPanel.removeAll();
        listingPanel = new ListingProductPanel(); // on garde la référence
        contentPanel.add(new ListingProductPanel(), BorderLayout.CENTER);
        refresh();
    }

    /** Ouvre un dialog de création */
    private void showCreate() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        CreateProductDialog dialog = new CreateProductDialog(parent);
        dialog.setVisible(true); // Ouvre la fenêtre
    }

    /** Ouvre un dialog de modification */
    private void showUpdate() {
        Window parent = SwingUtilities.getWindowAncestor(this);
        String productName = askUserToSelectProductName();
        EditProductDialog dialog = new EditProductDialog(parent, productName);
        dialog.setVisible(true);
    }

    /** Supprimer un produit */
    private void showDelete() {
        // Demande à l'utilisateur de choisir un produit
        String productName = askUserToSelectProductName();
        if (productName == null) return; // rien choisi ou annulé

        // Message de confirmation
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Es-tu sûr de vouloir supprimer le produit : " + productName + " ?",
                "Confirmation suppression",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return; // annulé
        }

        try {
            controllerPackage.ProductController controller = new controllerPackage.ProductController();
            controller.deleteProduct(productName);

            JOptionPane.showMessageDialog(
                    this,
                    "Produit supprimé avec succès.",
                    "Suppression",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // Si la liste est affichée → rafraîchir
            if (listingPanel != null) {
                listingPanel.refresh();
            }

        } catch (exceptionPackage.ConnectionException | exceptionPackage.TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }




    private String askUserToSelectProductName() {
        controllerPackage.ProductController controller = new controllerPackage.ProductController();

        try {
            // Récupère les produits
            java.util.ArrayList<model.Product> products = controller.getAllProducts();
            if (products.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Aucun produit disponible.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return null;
            }

            // Convertit en tableau de noms
            String[] names = products.stream()
                    .map(model.Product::getName)
                    .toArray(String[]::new);

            // Affiche la boîte de dialogue
            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Choisis le produit :",
                    "Sélection produit",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    names,
                    names[0]
            );

            // Retourne le nom ou null si annulé
            if (selected == null || selected.isBlank()) {
                return null;
            }
            return selected;

        } catch (exceptionPackage.ConnectionException | exceptionPackage.TitleException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /** Utilitaire pour rafraîchir */
    private void refresh() {
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}