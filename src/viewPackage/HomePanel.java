package viewPackage;

import javax.swing.*;
import java.awt.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new GridBagLayout());
        JLabel welcome = new JLabel("Bienvenue ! Utilisez le menu pour naviguer.");
        welcome.setFont(welcome.getFont().deriveFont(Font.PLAIN, 18f));
        add(welcome);
    }
}