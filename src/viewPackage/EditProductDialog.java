package viewPackage;

import controllerPackage.ProductController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Category;
import model.Product;
import model.Supplier;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditProductDialog extends JDialog {
    private ProductController controller;

    private JTextField nameField;
    private JComboBox<String> supplierCombo;
    private JComboBox<String> categoryCombo;
    private JSpinner minQtySpinner;
    private JSpinner stockSpinner;
    private JSpinner reorderSpinner;
    private JFormattedTextField dateField;
    private JCheckBox frozenCheck;
    private JTextArea descArea;

    public EditProductDialog(Window parent, String productName) {
        super(parent, "Modifier le produit", ModalityType.APPLICATION_MODAL);

        setController(new ProductController());
        buildUI();
        loadProduct(productName);   // <— préremplissage
        pack();
        setLocationRelativeTo(parent);
    }

    public void setController(ProductController controller) {
        this.controller = controller;
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        // Nom (PK)
        form.add(new JLabel("Nom :"));
        nameField = new JTextField();
        nameField.setEditable(false); // PK non modifiable
        form.add(nameField);

        // Fournisseur (tu pourras remplacer par liste dynamique)
        form.add(new JLabel("Fournisseur :"));
        supplierCombo = new JComboBox<>(new String[]{
                "Pommes de Terre SARL", "Surgelés Belges", "Boissons du Plat Pays"
        });
        form.add(supplierCombo);

        // Catégorie
        form.add(new JLabel("Catégorie :"));
        categoryCombo = new JComboBox<>(new String[]{
                "Légumes", "Surgelés", "Condiments", "Boissons"
        });
        form.add(categoryCombo);

        // Nombres
        form.add(new JLabel("Quantité min :"));
        minQtySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        form.add(minQtySpinner);

        form.add(new JLabel("Stock :"));
        stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        form.add(stockSpinner);

        form.add(new JLabel("Réassort :"));
        reorderSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        form.add(reorderSpinner);

        // Date
        form.add(new JLabel("Date de vente (yyyy-MM-dd) :"));
        dateField = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd")));
        dateField.setColumns(10);
        form.add(dateField);

        // Surgelé
        form.add(new JLabel("Surgelé :"));
        frozenCheck = new JCheckBox();
        form.add(frozenCheck);

        // Description
        form.add(new JLabel("Description :"));
        descArea = new JTextArea(3, 20);
        form.add(new JScrollPane(descArea));

        // Boutons
        JPanel btns = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancelBtn = new JButton("Annuler");
        JButton saveBtn = new JButton("Enregistrer");
        btns.add(cancelBtn);
        btns.add(saveBtn);

        cancelBtn.addActionListener(e -> dispose());
        saveBtn.addActionListener(e -> onSave());

        setLayout(new BorderLayout(10, 10));
        add(form, BorderLayout.CENTER);
        add(btns, BorderLayout.SOUTH);
    }

    private void loadProduct(String name) {
        try {
            Product p = controller.getProductByName(name);
            if (p == null) {
                JOptionPane.showMessageDialog(this, "Produit introuvable : " + name,
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }
            nameField.setText(p.getName());
            supplierCombo.setSelectedItem(p.getSupplier() != null ? p.getSupplier().getName() : null);
            categoryCombo.setSelectedItem(p.getCategory() != null ? p.getCategory().getName() : null);
            minQtySpinner.setValue(p.getMinQuantityDesired());
            stockSpinner.setValue(p.getStockQuantity());
            reorderSpinner.setValue(p.getReorderQuantity());
            dateField.setText(p.getDateOfSale() != null ? p.getDateOfSale().toString() : "");
            frozenCheck.setSelected(p.isFrozen());
            descArea.setText(p.getDescription() != null ? p.getDescription() : "");
        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onSave() {
        try {
            String name = nameField.getText().trim();
            String supplierName = (String) supplierCombo.getSelectedItem();
            String categoryName = (String) categoryCombo.getSelectedItem();
            int minQty = (Integer) minQtySpinner.getValue();
            int stock = (Integer) stockSpinner.getValue();
            int reorder = (Integer) reorderSpinner.getValue();

            LocalDate date;
            try {
                date = LocalDate.parse(dateField.getText().trim());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Date invalide (format yyyy-MM-dd).");
            }

            boolean frozen = frozenCheck.isSelected();
            String desc = descArea.getText().isBlank() ? null : descArea.getText().trim();

            Product updated = new Product(
                    name,
                    new Supplier(supplierName),
                    new Category(categoryName),
                    minQty, stock, reorder,
                    date, frozen, desc
            );

            controller.updateProduct(updated);
            JOptionPane.showMessageDialog(this, "Produit mis à jour !");
            dispose();
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur DB", JOptionPane.ERROR_MESSAGE);
        }
    }
}