/*package viewPackage;
//import exceptionPackage.ConnectionException;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame{
    private JMenuBar menuBar;
    private JMenu searchMenu, modificationMenu;
    private Container frameContainer;
    private JMenuItem searchUserItem, searchUserItem2, searchUserItem3, statisticItem, deleteSongItem, createUpdateSongItem, listingSongItem;
    private JLabel flickeringLabel;
    //private TextFlickerThread flickerThread;
    private JPanel bottomPanel;

    public MainWindow() {
        super("Menu principal");
        setBounds(100, 100, 700, 700);
        frameContainer = this.getContentPane();
        frameContainer.setLayout(new BorderLayout());

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new FlowLayout(FlowLayout.CENTER));

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        searchMenu = new JMenu("Search");
        menuBar.add(searchMenu);
        searchUserItem = new JMenuItem("Search 1");
        searchMenu.add(searchUserItem);
        setVisible(true);
        searchUserItem2 = new JMenuItem("Search 2");
        setVisible(true);
        searchMenu.add(searchUserItem2);
        searchUserItem3 = new JMenuItem("Search 3");
        searchMenu.add(searchUserItem3);
        setVisible(true);
        statisticItem = new JMenuItem("statistic");
        searchMenu.add(statisticItem);
        setVisible(true);

        flickeringLabel = new JLabel("JAVAPROJECT");
        flickeringLabel.setForeground(Color.RED);
        bottomPanel.add(flickeringLabel);
        frameContainer.add(bottomPanel, BorderLayout.SOUTH);

    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new MainWindow();
            }
        });
    }}*/

package viewPackage;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private final JPanel centerPanel;

    public MainWindow() {
        super("Restoration – Gestion des produits");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // barre de statut (facultatif)
        JLabel statusBar = new JLabel("Prêt");
        statusBar.setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
        add(statusBar, BorderLayout.SOUTH);

        // zone centrale qui recevra tes vues
        centerPanel = new JPanel(new BorderLayout());
        add(centerPanel, BorderLayout.CENTER);
    }

    /** Ajoute/remplace le contenu central par un JScrollPane (utilisé par ListingProductsDisplay). */
    public void addScrollPane(JScrollPane scrollPane) {
        centerPanel.removeAll();
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    /** Optionnel : si tu veux poser un composant brut au centre (sans JScrollPane) */
    public void setCenter(Component comp) {
        centerPanel.removeAll();
        centerPanel.add(comp, BorderLayout.CENTER);
        centerPanel.revalidate();
        centerPanel.repaint();
    }
}