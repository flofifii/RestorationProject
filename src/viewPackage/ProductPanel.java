package viewPackage;

import javax.swing.*;
import java.awt.*;

public class ProductPanel extends JPanel {

    private final JButton showProductsButton;
    private final JButton newProductButton;

    private ListingProductPanel listingPanel;   // créé au premier clic
    private boolean tableAdded = false;

    public ProductPanel() {
        setLayout(new BorderLayout());

        // --- toolbar sous le menu
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        showProductsButton = new JButton("Afficher les produits");
        newProductButton   = new JButton("Nouveau produit");
        toolbar.add(showProductsButton);
        toolbar.add(newProductButton);
        add(toolbar, BorderLayout.NORTH);

        // zone centrale vide au démarrage
        add(new JPanel(), BorderLayout.CENTER);

        // actions
        showProductsButton.addActionListener(e -> showOrRefreshTable());
        newProductButton.addActionListener(e -> openCreateProductDialog());
    }

    private void showOrRefreshTable() {
        if (!tableAdded) {
            listingPanel = new ListingProductPanel(); // table
            remove(1); // retire le panel central vide (index 0=NORTH, 1=CENTER)
            add(listingPanel, BorderLayout.CENTER);
            tableAdded = true;
        }
        listingPanel.refresh(); // charge/rafraîchit les données
        revalidate();
        repaint();
    }

    /** Ouvre la fenêtre modale avec le formulaire de création */
    private void openCreateProductDialog() {
        // parent window pour centrer le dialog
        Window owner = SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(owner, "Nouveau produit", Dialog.ModalityType.APPLICATION_MODAL);

        // on passe le dialog (pour pouvoir le fermer) + un callback de succès (rafraîchir la table)
        CreateProductPanel form = new CreateProductPanel(dialog, this::refresh);

        dialog.setContentPane(form);
        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setResizable(false);
        dialog.setVisible(true); // modal → bloque jusqu’à fermeture
    }

    /** Rafraîchit la liste uniquement si elle est déjà visible */
    private void refresh() {
        if (tableAdded && listingPanel != null) {
            listingPanel.refresh();
        }
    }
}