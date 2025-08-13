package viewPackage;

import javax.swing.*;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow window = new MainWindow();
            // instancie et injecte la vue de listing (elle ajoutera elle-même la table via addScrollPane)
            new ListingProductsDisplay(window);
            window.setVisible(true);
        });
    }
}

zffzfez