package viewPackage;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final JPanel centerPanel;
    private final CardLayout cards;

    public static final String HOME = "HOME";
    public static final String PRODUCTS = "PRODUCTS";
    public static final String ORDERS = "ORDERS";
    public static final String SEARCH = "SEARCH";

    public MainWindow() {
        super("Restoration – Gestion");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        // Layout principal
        setLayout(new BorderLayout());

        // Zone centrale avec CardLayout
        cards = new CardLayout();
        centerPanel = new JPanel(cards);

        // Ajouter les vues
        centerPanel.add(new HomePanel(), HOME);
        centerPanel.add(new ProductPanel(), PRODUCTS);
        centerPanel.add(new JLabel("Vue Commandes (à faire)", SwingConstants.CENTER), ORDERS);
        centerPanel.add(new JLabel("Vue Recherche (à faire)", SwingConstants.CENTER), SEARCH);

        add(centerPanel, BorderLayout.CENTER);

        // Barre de statut (optionnelle)
        JLabel status = new JLabel("Prêt");
        status.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(status, BorderLayout.SOUTH);

        // Créer et configurer la barre de menu
        setJMenuBar(createMenuBar());

        // Afficher la page d’accueil par défaut
        cards.show(centerPanel, HOME);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // Menu "Navigation"
        JMenu menuNav = new JMenu("Navigation");

        JMenuItem homeItem = new JMenuItem("Accueil");
        JMenuItem productsItem = new JMenuItem("Produits");
        JMenuItem ordersItem = new JMenuItem("Commandes");
        JMenuItem searchItem = new JMenuItem("Recherche");

        // Actions de navigation
        homeItem.addActionListener(e -> cards.show(centerPanel, HOME));
        productsItem.addActionListener(e -> cards.show(centerPanel, PRODUCTS));
        ordersItem.addActionListener(e -> cards.show(centerPanel, ORDERS));
        searchItem.addActionListener(e -> cards.show(centerPanel, SEARCH));

        // Ajouter au menu
        menuNav.add(homeItem);
        menuNav.add(productsItem);
        menuNav.add(ordersItem);
        menuNav.add(searchItem);

        // Ajouter le menu à la barre
        menuBar.add(menuNav);

        return menuBar;
    }
}