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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.text.SimpleDateFormat;

public class CreateProductDialog extends JDialog {

    private JTextField nameField;
    private JComboBox<String> supplierCombo;
    private JComboBox<String> categoryCombo;
    private JSpinner minQtySpinner;
    private JSpinner stockSpinner;
    private JSpinner reorderSpinner;
    private JFormattedTextField dateField;
    private JCheckBox frozenCheck;
    private JTextArea descArea;

    private ProductController controller;

    public CreateProductDialog(Window parent) {
        super(parent, "Nouveau produit", ModalityType.APPLICATION_MODAL);

        setController(new ProductController());
        buildUI();
        pack();
        setLocationRelativeTo(parent);
    }

    public void setController(ProductController controller) {
        this.controller = controller;
    }

    private void buildUI() {
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));

        // Nom
        formPanel.add(new JLabel("Nom :"));
        nameField = new JTextField();
        formPanel.add(nameField);

        // Fournisseur (à remplacer plus tard par une liste dynamique depuis la BD)
        formPanel.add(new JLabel("Fournisseur :"));
        supplierCombo = new JComboBox<>(new String[]{
                "Pommes de Terre SARL", "Surgelés Belges", "Boissons du Plat Pays"
        });
        formPanel.add(supplierCombo);

        // Catégorie
        formPanel.add(new JLabel("Catégorie :"));
        categoryCombo = new JComboBox<>(new String[]{
                "Légumes", "Surgelés", "Condiments", "Boissons"
        });
        formPanel.add(categoryCombo);

        // Quantité min
        formPanel.add(new JLabel("Quantité min :"));
        minQtySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        formPanel.add(minQtySpinner);

        // Stock
        formPanel.add(new JLabel("Stock :"));
        stockSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        formPanel.add(stockSpinner);

        // Réassort
        formPanel.add(new JLabel("Réassort :"));
        reorderSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
        formPanel.add(reorderSpinner);

        // Date de vente
        formPanel.add(new JLabel("Date de vente (yyyy-MM-dd) :"));
        dateField = new JFormattedTextField(new DateFormatter(new SimpleDateFormat("yyyy-MM-dd")));
        dateField.setColumns(10);
        formPanel.add(dateField);

        // Surgelé
        formPanel.add(new JLabel("Surgelé :"));
        frozenCheck = new JCheckBox();
        formPanel.add(frozenCheck);

        // Description
        formPanel.add(new JLabel("Description :"));
        descArea = new JTextArea(3, 20);
        formPanel.add(new JScrollPane(descArea));

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton createBtn = new JButton("Créer");
        JButton cancelBtn = new JButton("Annuler");

        buttonPanel.add(cancelBtn);
        buttonPanel.add(createBtn);

        cancelBtn.addActionListener(e -> dispose());

        createBtn.addActionListener(e -> {
            try {
                Product product = buildProductFromForm();
                controller.createProduct(product);
                JOptionPane.showMessageDialog(this,
                        "Produit créé avec succès !",
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (ConnectionException | TitleException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this,
                        ex.getMessage(),
                        "Erreur de saisie",
                        JOptionPane.WARNING_MESSAGE);
            }
        });

        // Layout final
        setLayout(new BorderLayout(10, 10));
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private Product buildProductFromForm() {
        String name = nameField.getText().trim();
        if (name.isEmpty()) throw new IllegalArgumentException("Le nom ne peut pas être vide");

        String supplierName = (String) supplierCombo.getSelectedItem();
        String categoryName = (String) categoryCombo.getSelectedItem();

        int minQty = (Integer) minQtySpinner.getValue();
        int stock = (Integer) stockSpinner.getValue();
        int reorder = (Integer) reorderSpinner.getValue();

        LocalDate date;
        try {
            date = LocalDate.parse(dateField.getText().trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date invalide (format attendu : yyyy-MM-dd)");
        }

        boolean frozen = frozenCheck.isSelected();
        String desc = descArea.getText().trim();

        return new Product(
                name,
                new Supplier(supplierName),
                new Category(categoryName),
                minQty,
                stock,
                reorder,
                date,
                frozen,
                desc
        );
    }
}