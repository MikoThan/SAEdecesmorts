
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VueGraphe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import sae.graphe.Algos;
import sae.graphe.Graphe;

/**
 *
 * @author p2300399
 */
public class Fenetre extends JFrame {
    private JPanel graphPanel;
    private JLabel infoLabel, degrémoy, nbsom, nbar, nbcompo, diam;
    private JComboBox<String> algorithmComboBox;
    private JTextField kMaxField;
    private JTextField maxMarginField;
    private String graphFilePath;
    private String flightsFilePath;
    private String airportsFilePath;
    private JMenu menu;
    private JMenuBar barre_menu = new JMenuBar();
    private JMenuItem coloriergraphe = new JMenuItem("coloration de graphe"), affichervols = new JMenuItem("affichage les vols"), affichercarte = new JMenuItem("afficher carte de france");
    private int kmax = 0;
    private Graph g;

    public Fenetre() {

        setTitle("SAE Graphe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000 ,1200);
        setLayout(new BorderLayout());
        initMenu();
        
    }
//créer le panel global qui va contenir le panel du graphe et le panel de chargement et de coloration
    private void initUI() {
        JPanel globalPanel = new JPanel(new BorderLayout());
        
        initGraphPanel();
        globalPanel.add(graphPanel, BorderLayout.CENTER);
        globalPanel.add(initInfoPanel(), BorderLayout.EAST);
        
        this.setContentPane(globalPanel);
        this.pack();
    }
    //créer le panel du graphe
    private void initGraphPanel() {
        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setBackground(Color.LIGHT_GRAY);
        JLabel graphLabel = new JLabel("Graphe");
        graphLabel.setFont(new Font("Arial", Font.BOLD, 24));
        graphPanel.add(graphLabel, BorderLayout.NORTH);
    }
    //créer le panel de coloration et de chargement
    private JPanel initInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.fill = GridBagConstraints.BOTH;
        cont.gridx = 0;
        cont.gridy = GridBagConstraints.RELATIVE;
        infoPanel.setBackground(Color.LIGHT_GRAY);

        infoLabel = new JLabel("Informations sur le graphe");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(infoLabel, cont);
        
        degrémoy = new JLabel("Degré moyen : ");
        cont.gridy=1;
        infoPanel.add(degrémoy, cont);
        
        nbsom = new JLabel("Nombre de sommets : ");
        cont.gridy=2;
        infoPanel.add(nbsom, cont);
        
        nbar = new JLabel("Nombre d'arêtes : ");
        cont.gridy=3;
        infoPanel.add(nbar, cont);
        
        nbcompo = new JLabel("Nombre de Composantes Connexes : ");
        cont.gridy=4;
        infoPanel.add(nbcompo, cont);
        
        diam = new JLabel("Diamètre : ");
        cont.gridy=5;
        infoPanel.add(diam, cont);

        String[] algorithms = {"WelshPowell", "DSATUR"};
        algorithmComboBox = new JComboBox<>(algorithms);
        cont.gridy=6;
        infoPanel.add(new JLabel("Choix d'algorithme de coloration"), cont);
        cont.gridy=7;
        infoPanel.add(algorithmComboBox, cont);

        JButton graphFileButton = new JButton("Chemin vers le fichier graphe");
        graphFileButton.addActionListener(new FileChooserActionListener("graph"));
        JButton flightsFileButton = new JButton("Chemin vers le fichier vols");
        flightsFileButton.addActionListener(new FileChooserActionListener("flights"));
        JButton airportsFileButton = new JButton("Chemin vers le fichier aéroports");
        airportsFileButton.addActionListener(new FileChooserActionListener("airports"));
        
        cont.gridy=8;
        infoPanel.add(graphFileButton, cont);
        cont.gridy=9;
        infoPanel.add(flightsFileButton, cont);
        cont.gridy=10;
        infoPanel.add(airportsFileButton, cont);

        kMaxField = new JTextField();
        kMaxField.setMaximumSize(new Dimension(Integer.MAX_VALUE, kMaxField.getPreferredSize().height));
        maxMarginField = new JTextField();
        maxMarginField.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxMarginField.getPreferredSize().height));

        cont.gridy=11;
        infoPanel.add(new JLabel("K-Max"), cont);
        cont.gridy=12;
        infoPanel.add(kMaxField, cont);

        cont.gridy=11;
        cont.gridx=1;
        infoPanel.add(new JLabel("Marge max en minutes"), cont);
        cont.gridy=12;
        infoPanel.add(maxMarginField, cont);

        JButton calculateButton = new JButton("Calculer");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                kmax = Integer.parseInt(kMaxField.getText());
                if (algorithmComboBox.getSelectedItem().equals("WelshPowell")){
                    Algos.WelshPowell(g, kmax);
                    Algos.coloriergraphe(g, kmax, "wp");}
                else{
                    Algos.Dsatur(g, kmax);
                    Algos.coloriergraphe(g, kmax, "color");
                } 
                updateGraphPanel();
            }
        });
        cont.gridx=0;
        cont.gridwidth=3;
        cont.gridy=13;
        infoPanel.add(calculateButton, cont);
        
        return infoPanel;
    }

    private void updateGraphPanel() {
        graphPanel.removeAll();
        Viewer viewer = new Viewer(g, Viewer.ThreadingModel.GRAPH_IN_ANOTHER_THREAD);
        viewer.enableAutoLayout();
        View viewPanel = viewer.addDefaultView(false);
        viewPanel.addMouseWheelListener(e -> {
            int rotation = e.getWheelRotation();
            double scaleFactor = 1.1;
            double zoomFactor = (rotation < 0) ? 1.0 / scaleFactor : scaleFactor;
            viewPanel.getCamera().setViewPercent(viewPanel.getCamera().getViewPercent() * zoomFactor);
        });
        graphPanel.add((Component) viewPanel, BorderLayout.CENTER);
        graphPanel.revalidate();
        graphPanel.repaint();
    }

    private class FileChooserActionListener implements ActionListener {
        private String fileType;

        public FileChooserActionListener(String fileType) {
            this.fileType = fileType;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                switch (fileType) {
                    case "graph":
                        graphFilePath = selectedFile.getAbsolutePath();
                            Graphe.chargerGraphe(graphFilePath);
                            g = Graphe.getGraphe();
                            degrémoy.setText("Degré moyen : "+Graphe.calculedegremoyen(g));
                            nbsom.setText("Nombre de sommets : "+Graphe.afficheNbNoeuds(g));
                            nbar.setText("Nombre d'arêtes : "+Graphe.afficheNbAretes(g));
                            nbcompo.setText("Nombre de Composantes Connexes : "+Graphe.afficheNbComposantesConnexes(g));
                            diam.setText("Diamètre : "+Graphe.affichediametre(g));
                            updateGraphPanel();
                            kmax=Graphe.getKmax();
                            kMaxField.setText(String.valueOf(kmax));
                            break;
                    case "flights":
                        flightsFilePath = selectedFile.getAbsolutePath();
                        break;
                    case "airports":
                        airportsFilePath = selectedFile.getAbsolutePath();
                        break;
                }
            }
        }
    }
    
    //créer le menu d'interaction
    private void initMenu(){
        
        menu=new JMenu("Interactions");
        menu.add(coloriergraphe);
        menu.addSeparator();
        menu.add(affichervols);
        menu.addSeparator();
        menu.add(affichercarte);
        
        barre_menu.add(menu);
        this.setJMenuBar(barre_menu);
        
        coloriergraphe.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                initUI();
            }
            
        });

        affichervols.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            
        });
        
        affichercarte.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent arg0) {
                throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
            }
            
        });
        
    }
}