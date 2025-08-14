package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Category;
import model.Product;
import model.Supplier;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class CreateProductPanel extends JPanel {
    private JTextField nameField, minQtyField, stockField, reorderField, dateField, descField;
    private JComboBox<String> supplierCombo, categoryCombo;
    private JCheckBox frozenCheck;
    private final ProductController controller;

    private final JDialog dialog;
    private final Runnable onSuccess; // appelé après création OK

    /** Constructeur conseillé : reçoit le dialog à fermer et un callback de succès */
    public CreateProductPanel(JDialog dialog, Runnable onSuccess) {
        this.controller = new ProductController();
        this.dialog = dialog;
        this.onSuccess = (onSuccess != null) ? onSuccess : () -> {};
        buildUI();
    }

    /** Optionnel : pouvoir l’utiliser sans dialog
    public CreateProductPanel() {
        this(null, null);
    }
     **/

    private void buildUI() {
        setLayout(new GridLayout(0, 2, 10, 10));

        add(new JLabel("Nom :"));
        nameField = new JTextField();
        add(nameField);

        add(new JLabel("Fournisseur :"));
        supplierCombo = new JComboBox<>(new String[]{"Pommes de Terre SARL", "Surgelés Belges", "Boissons du Plat Pays"});
        add(supplierCombo);

        add(new JLabel("Catégorie :"));
        categoryCombo = new JComboBox<>(new String[]{"Légumes", "Surgelés", "Condiments", "Boissons"});
        add(categoryCombo);

        add(new JLabel("Quantité min :"));
        minQtyField = new JTextField();
        add(minQtyField);

        add(new JLabel("Stock :"));
        stockField = new JTextField();
        add(stockField);

        add(new JLabel("Réassort :"));
        reorderField = new JTextField();
        add(reorderField);

        add(new JLabel("Date de vente (YYYY-MM-DD) :"));
        dateField = new JTextField();
        add(dateField);

        add(new JLabel("Surgelé :"));
        frozenCheck = new JCheckBox();
        add(frozenCheck);

        add(new JLabel("Description :"));
        descField = new JTextField();
        add(descField);

        // Boutons d’action
        JButton saveBtn = new JButton("Créer");
        JButton cancelBtn = new JButton("Annuler");
        saveBtn.addActionListener(e -> createProduct());
        cancelBtn.addActionListener(e -> {
            if (dialog != null) dialog.dispose();
        });

        add(saveBtn);
        add(cancelBtn);
    }

    private void createProduct() {
        try {
            String name = nameField.getText().trim();
            String supplierName = (String) supplierCombo.getSelectedItem();
            String categoryName = (String) categoryCombo.getSelectedItem();
            int minQty = Integer.parseInt(minQtyField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());
            int reorder = Integer.parseInt(reorderField.getText().trim());
            LocalDate date = LocalDate.parse(dateField.getText().trim());
            boolean frozen = frozenCheck.isSelected();
            String desc = descField.getText().isBlank() ? null : descField.getText().trim();

            Supplier supplier = new Supplier(supplierName);
            Category category = new Category(categoryName);

            Product p = new Product(name, supplier, category, minQty, stock, reorder, date, frozen, desc);
            controller.createProduct(p);

            JOptionPane.showMessageDialog(this, "Produit créé avec succès !");
            if (dialog != null) dialog.dispose(); // fermer la fenêtre
            onSuccess.run();                      // rafraîchir la liste

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Vérifie les nombres (min/stock/réassort).", "Erreur", JOptionPane.ERROR_MESSAGE);
        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}