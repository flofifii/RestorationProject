package viewPackage;
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
    }}