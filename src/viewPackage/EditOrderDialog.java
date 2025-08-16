package viewPackage;

import controllerPackage.OrderController;
import exceptionPackage.ConnectionException;
import exceptionPackage.TitleException;
import model.Customer;
import model.Order;

import javax.swing.*;
import javax.swing.text.DateFormatter;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class EditOrderDialog extends JDialog {

    private final OrderController controller;

    // Champs UI
    private JSpinner numberSpinner;          // PK (désactivée)
    private JSpinner customerIdSpinner;
    private JComboBox<String> paymentMethodCombo;
    private JCheckBox takeawayCheck;
    private JFormattedTextField creationDateField;
    private JFormattedTextField deliveryDateField; // optionnel
    private JTextArea commentaryArea;              // optionnel
    private JTextArea specificRequestArea;         // optionnel

    public EditOrderDialog(Window parent, int orderNumber) {
        super(parent, "Modifier la commande", ModalityType.APPLICATION_MODAL);
        this.controller = new OrderController();

        buildUI();
        loadOrder(orderNumber);   // pré-remplissage
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }

    private void buildUI() {
        JPanel form = new JPanel(new GridLayout(0, 2, 10, 10));

        // Numéro (PK)
        form.add(new JLabel("Numéro :"));
        numberSpinner = new JSpinner(new SpinnerNumberModel(0, 0, Integer.MAX_VALUE, 1));
        numberSpinner.setEnabled(false);
        form.add(numberSpinner);

        // Client (ID)
        form.add(new JLabel("Client (customerId) :"));
        customerIdSpinner = new JSpinner(new SpinnerNumberModel(1, 1, Integer.MAX_VALUE, 1));
        form.add(customerIdSpinner);

        // Mode de paiement
        form.add(new JLabel("Mode de paiement :"));
        paymentMethodCombo = new JComboBox<>(new String[]{
                "Carte", "Espèces", "Virement", "Autre"
        });
        form.add(paymentMethodCombo);

        // À emporter
        form.add(new JLabel("À emporter :"));
        takeawayCheck = new JCheckBox();
        form.add(takeawayCheck);

        // Dates
        DateFormatter df = new DateFormatter(new SimpleDateFormat("yyyy-MM-dd"));

        form.add(new JLabel("Date de création (yyyy-MM-dd) :"));
        creationDateField = new JFormattedTextField(df);
        creationDateField.setColumns(10);
        form.add(creationDateField);

        form.add(new JLabel("Date de livraison (yyyy-MM-dd) :"));
        deliveryDateField = new JFormattedTextField(df);
        deliveryDateField.setColumns(10);
        form.add(deliveryDateField);

        // Commentaire
        form.add(new JLabel("Commentaire :"));
        commentaryArea = new JTextArea(3, 20);
        form.add(new JScrollPane(commentaryArea));

        // Demande spécifique
        form.add(new JLabel("Demande spécifique :"));
        specificRequestArea = new JTextArea(3, 20);
        form.add(new JScrollPane(specificRequestArea));

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

    private void loadOrder(int number) {
        try {
            Order o = controller.getOrderByNumber(number);
            if (o == null) {
                JOptionPane.showMessageDialog(this, "Commande introuvable : " + number,
                        "Erreur", JOptionPane.ERROR_MESSAGE);
                dispose();
                return;
            }

            numberSpinner.setValue(o.getNumber());
            // Customer ID depuis la classe Customer (hérite de Person → getPersonID())
            if (o.getcustomer() != null) {
                customerIdSpinner.setValue(o.getcustomer().getPersonID());
            }

            paymentMethodCombo.setSelectedItem(o.getPaymentMethod());
            takeawayCheck.setSelected(Boolean.TRUE.equals(o.isTakeaway()));
            creationDateField.setText(o.getCreationDate() != null ? o.getCreationDate().toString() : "");
            deliveryDateField.setText(o.getDeliveryDate() != null ? o.getDeliveryDate().toString() : "");
            commentaryArea.setText(o.getCommentary() != null ? o.getCommentary() : "");
            specificRequestArea.setText(o.getSpecificRequest() != null ? o.getSpecificRequest() : "");

        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            dispose();
        }
    }

    private void onSave() {
        try {
            int number = (Integer) numberSpinner.getValue();

            int customerId = (Integer) customerIdSpinner.getValue();
            if (customerId <= 0) throw new IllegalArgumentException("customerId doit être > 0.");

            String paymentMethod = (String) paymentMethodCombo.getSelectedItem();
            if (paymentMethod == null || paymentMethod.isBlank()) {
                throw new IllegalArgumentException("Le mode de paiement est obligatoire.");
            }

            LocalDate creationDate = parseDateOrThrow(creationDateField.getText(), "Date de création");
            LocalDate deliveryDate = parseDateOrNull(deliveryDateField.getText());

            boolean isTakeaway = takeawayCheck.isSelected();

            String commentary = blankToNull(commentaryArea.getText());
            String specificRequest = blankToNull(specificRequestArea.getText());

            // Reconstruire l'objet Order avec les nouvelles valeurs
            Customer customer = new Customer(customerId);
            Order updated = new Order(number, customer, paymentMethod, isTakeaway, creationDate);
            updated.setDeliveryDate(deliveryDate);
            updated.setCommentary(commentary);
            updated.setSpecificRequest(specificRequest);

            controller.updateOrder(updated);
            JOptionPane.showMessageDialog(this, "Commande mise à jour !");
            dispose();

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(this, iae.getMessage(), "Erreur de saisie", JOptionPane.WARNING_MESSAGE);
        } catch (ConnectionException | TitleException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur DB", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ===== utilitaires =====

    private static LocalDate parseDateOrThrow(String text, String label) {
        String val = (text != null) ? text.trim() : "";
        if (val.isEmpty()) {
            throw new IllegalArgumentException(label + " est obligatoire (format yyyy-MM-dd).");
        }
        try {
            return LocalDate.parse(val);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(label + " invalide (format yyyy-MM-dd).");
        }
    }

    private static LocalDate parseDateOrNull(String text) {
        if (text == null) return null;
        String val = text.trim();
        if (val.isEmpty()) return null;
        try {
            return LocalDate.parse(val);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date de livraison invalide (format yyyy-MM-dd).");
        }
    }

    private static String blankToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }
}
