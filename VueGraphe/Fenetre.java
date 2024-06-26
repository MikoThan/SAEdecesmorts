
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VueGraphe;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ImageIcon;
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
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.graphstream.graph.Graph;
import org.graphstream.ui.swingViewer.View;
import org.graphstream.ui.swingViewer.Viewer;
import sae.graphe.*;
import sae.graphe.Vol;
import sae.graphe.Exceptions.AeroportError;
import sae.graphe.Exceptions.FileFormatError;
import vueJXMap.MapViewerPanel;

/**
 *
 * @author p2300399
 */
public class Fenetre extends JFrame {
   
    private JPanel graphPanel, volPanel;
    private JLabel infoLabel, degrémoy, nbsom, nbar, nbcompo, diam, conflits=new JLabel("Nombre de conflits : ");
    private JLabel triid, tridep, triarrv, trih, trim, trit;
    private JTextField id, dep, arrv, h, m, t;
    private JComboBox<String> algorithmComboBox;
    private JTextField kMaxField;
    private static JTextField maxMarginField;
    private String graphFilePath="";
    private String flightsFilePath="";
    private String airportsFilePath="";
    private JMenu menu;
    private JMenuBar barre_menu = new JMenuBar();
    private JMenuItem coloriergraphe = new JMenuItem("coloration de graphe"), affichervols = new JMenuItem("affichage les vols"), affichercarte = new JMenuItem("afficher carte de france");
    private int kmax = 0;
    private Vol[] vols;
    private VolModel modelevol = new VolModel();
    private Graph g;
    private MapViewerPanel viewPanel = new MapViewerPanel();
    private JTable tablevols = new JTable(modelevol);
    private JScrollPane jsp = new JScrollPane(tablevols);
    private final ImageIcon icon = new ImageIcon("E:/GROSSE SAE C LA SAUCE/SAE JAVA/Sae graphe/attention.gif");
    private final ImageIcon alarme = new ImageIcon("F:/GROSSE SAE C LA SAUCE/SAE JAVA/Sae graphe/alarme.gif");
     //Ne pas oublier de rechanger la lettre du directory de la clef (elle change à chaque fois cette relou)
    
    public Fenetre() throws UnsupportedLookAndFeelException {

        setTitle("SAE Graphe");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1500, 1200);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        this.setLocation(200, 100);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(new BorderLayout());
        initMenu();

    }
    
//créer le panel global qui va contenir le panel du graphe et le panel de chargement et de coloration
    private void initUI() {
        JPanel globalPanel = new JPanel(new BorderLayout());
        if (graphPanel==null)
            initGraphPanel();
        globalPanel.add(graphPanel, BorderLayout.CENTER);
        globalPanel.add(initInfoPanel(), BorderLayout.EAST);
        maxMarginField.setText("15");
        this.setContentPane(globalPanel);
        this.pack();
    }

    //créer le panel du graphe
    private void initGraphPanel() {
        graphPanel = new JPanel(new BorderLayout());
        graphPanel.setBackground(Color.LIGHT_GRAY);
        JLabel graphLabel = new JLabel("");
        graphLabel.setFont(new Font("Arial", Font.BOLD, 24));
        graphPanel.add(graphLabel, BorderLayout.NORTH);
    }

    //créer le panel de coloration et de chargement
    private JPanel initInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GridBagConstraints cont = new GridBagConstraints();
        cont.insets=new Insets(10,10,10,10);
        cont.fill = GridBagConstraints.BOTH;
        cont.gridx = 0;
        cont.gridy = GridBagConstraints.RELATIVE;
        infoPanel.setBackground(Color.LIGHT_GRAY);

        infoLabel = new JLabel("Informations sur le graphe");
        infoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        infoPanel.add(infoLabel, cont);

        degrémoy = new JLabel("Degré moyen : ");
        cont.gridy = 1;
        infoPanel.add(degrémoy, cont);

        nbsom = new JLabel("Nombre de sommets : ");
        cont.gridy = 2;
        infoPanel.add(nbsom, cont);

        nbar = new JLabel("Nombre d'arêtes : ");
        cont.gridy = 3;
        infoPanel.add(nbar, cont);

        nbcompo = new JLabel("Nombre de Composantes Connexes : ");
        cont.gridy = 4;
        infoPanel.add(nbcompo, cont);

        diam = new JLabel("Diamètre : ");
        cont.gridy = 5;
        infoPanel.add(diam, cont);

        String[] algorithms = {"WelshPowell", "DSATUR"};
        algorithmComboBox = new JComboBox<>(algorithms);
        algorithmComboBox.setSelectedItem("DSATUR");
        cont.gridy = 6;
        infoPanel.add(new JLabel("Choix de l'algorithme de coloration"), cont);
        cont.gridy = 7;
        infoPanel.add(algorithmComboBox, cont);

        JButton graphFileButton = new JButton("Chemin vers le fichier graphe");
        graphFileButton.addActionListener(new FileChooserActionListener("graph"));
        JButton flightsFileButton = new JButton("Chemin vers le fichier vols");
        flightsFileButton.addActionListener(new FileChooserActionListener("flights"));
        JButton airportsFileButton = new JButton("Chemin vers le fichier aéroports");
        airportsFileButton.addActionListener(new FileChooserActionListener("airports"));

        cont.gridy = 8;
        infoPanel.add(graphFileButton, cont);
        cont.gridy = 9;
        infoPanel.add(flightsFileButton, cont);
        cont.gridy = 10;
        infoPanel.add(airportsFileButton, cont);

        kMaxField = new JTextField();
        kMaxField.setMaximumSize(new Dimension(Integer.MAX_VALUE, kMaxField.getPreferredSize().height));
        maxMarginField = new JTextField();
        maxMarginField.setMaximumSize(new Dimension(Integer.MAX_VALUE, maxMarginField.getPreferredSize().height));

        cont.gridy = 11;
        infoPanel.add(new JLabel("K-Max"), cont);
        cont.gridy = 12;
        infoPanel.add(kMaxField, cont);

        cont.gridy = 11;
        cont.gridx = 1;
        infoPanel.add(new JLabel("Marge max en minutes"), cont);
        cont.gridy = 12;
        infoPanel.add(maxMarginField, cont);

        JButton calculateButton = new JButton("Calculer");
        calculateButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            String kMaxText = kMaxField.getText();
            String maxMarginText = maxMarginField.getText();


            kmax = Integer.parseInt(kMaxText);
            if (kmax <= 0) {
                JOptionPane.showMessageDialog(Fenetre.this, "Veuillez choisir un kmax strictement positif", "Erreur Kmax", JOptionPane.INFORMATION_MESSAGE, alarme);
                return;
            }

            int maxMargin = Integer.parseInt(maxMarginText);
            Vol.setTdiff(maxMargin);

            if (algorithmComboBox.getSelectedItem().equals("WelshPowell")) {
                Algos.WelshPowell(g, kmax);
                conflits.setText("Nombre de conflits : " + Graphe.getConf());
                Algos.coloriergraphe(g, kmax, "wp");
                if (graphFilePath.isEmpty()) {
                    Algos.genererFile(g, "wp", flightsFilePath);
                } else {
                    Algos.genererFile(g, "wp", graphFilePath);
                }
            } else {
                Algos.Dsatur(g, kmax);
                conflits.setText("Nombre de conflits : " + Graphe.getConf());
                Algos.coloriergraphe(g, kmax, "color");
                if (graphFilePath.isEmpty()) {
                    Algos.genererFile(g, "color", flightsFilePath);
                } else {
                    Algos.genererFile(g, "color", graphFilePath);
                }
            }
            

            updateGraphPanel();
        } catch (Exception ex) {
           
        }
    }
});
        cont.gridx = 0;
        cont.gridwidth = 3;
        cont.gridy = 13;
        infoPanel.add(calculateButton, cont);

        cont.gridy = 14;
        infoPanel.add(conflits, cont);
        return infoPanel;
    }
//Update le panel de gauche ou se trouve le Graph
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
//Permet l'ouverture d'un explorateur de fichier pour choisir les fichier de vols/graph
    private class FileChooserActionListener implements ActionListener{

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
                    {
                        try {
                            Graphe.chargerGraphe(graphFilePath);
                            g = Graphe.getGraphe();
                            degrémoy.setText("Degré moyen : " + Graphe.calculedegremoyen(g));
                            nbsom.setText("Nombre de sommets : " + Graphe.afficheNbNoeuds(g));
                             nbar.setText("Nombre d'arêtes : " + Graphe.afficheNbAretes(g));
                            nbcompo.setText("Nombre de Composantes Connexes : " + Graphe.afficheNbComposantesConnexes(g));
                            diam.setText("Diamètre : " + Graphe.affichediametre(g));
                             updateGraphPanel();
                            kmax = Graphe.getKmax();
                            kMaxField.setText(String.valueOf(kmax));
                        } catch (Exceptions.FileFormatError | IOException ex) {
                            System.out.println(ex.getMessage());
                            
                        }
                    }
                        
                        break;

                    case "flights":
                        if (airportsFilePath.equals("")) {
                            JOptionPane.showMessageDialog(Fenetre.this, "Le fichier des aéroports n'est pas défini","Erreur fichier", JOptionPane.ERROR_MESSAGE,icon);

                        } 
                        else if (!selectedFile.getAbsolutePath().contains("csv")){
                            JOptionPane.showMessageDialog(Fenetre.this, "Le fichier des aéroports n'est pas au format csv","Erreur format fichier", JOptionPane.ERROR_MESSAGE,icon);
                        }
                            else {
                            flightsFilePath = selectedFile.getAbsolutePath();
                            maxMarginField.setText("15");
                            Graphegen gen = new Graphegen(airportsFilePath, flightsFilePath);
                            vols = new Vol[gen.getNbvols()];
                            vols = gen.getTabv();
                            g = gen.genGraph();
                            degrémoy.setText("Degré moyen : " + Graphe.calculedegremoyen(g));
                            nbsom.setText("Nombre de sommets : " + Graphe.afficheNbNoeuds(g));
                            nbar.setText("Nombre d'arêtes : " + Graphe.afficheNbAretes(g));
                            nbcompo.setText("Nombre de Composantes Connexes : " + Graphe.afficheNbComposantesConnexes(g));
                            diam.setText("Diamètre : " + Graphe.affichediametre(g));
                            updateGraphPanel();
                            kmax = Graphe.getKmax();
                            kMaxField.setText(String.valueOf(kmax));
                        }

                        break;
                    case "airports":
                        
                        if (!selectedFile.getAbsolutePath().contains(".txt")||(!selectedFile.getAbsolutePath().contains("aeroport"))){
                            JOptionPane.showMessageDialog(Fenetre.this, "Le fichier des aéroports n'est pas au bon format, veuillez le rechoisir","Erreur fichier aéroport", JOptionPane.ERROR_MESSAGE,icon);
                        }
                        else{
                            airportsFilePath = selectedFile.getAbsolutePath();
                        }
                        break;
                        
                }
            }
        }
    }

    //créer le menu d'interaction
    private void initMenu() {

        menu = new JMenu("Interactions");
        menu.add(coloriergraphe);
        menu.addSeparator();
        menu.add(affichervols);
        menu.addSeparator();
        menu.add(affichercarte);

        barre_menu.add(menu);
        this.setJMenuBar(barre_menu);

        coloriergraphe.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                viewPanel.close();
                initUI();
            }

        });

        affichervols.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                viewPanel.close();
                if (flightsFilePath.equals("")) {
                    JOptionPane.showMessageDialog(Fenetre.this, "Le fichier des vols n'est pas chargé","Erreur fichier vols", JOptionPane.ERROR_MESSAGE,icon);
                } else {
                    initAffichage();

                    for (Vol vol : vols) {
                        modelevol.addVol(vol);
                    }
                    if (!id.getText().equals("")) {
                        modelevol.filterId(id.getText());
                    }

                    if (!dep.getText().equals("")) {
                        modelevol.filterDep(dep.getText());
                    }

                    if (!arrv.getText().equals("")) {
                        modelevol.filterArr(arrv.getText());
                    }

                    if (!h.getText().equals("")) {
                        modelevol.filterM(m.getText());
                        if (!m.getText().equals("")) {
                            modelevol.filterM(m.getText());
                        } else {
                            modelevol.filterM(m.getText());
                        }
                    }
                    if (!t.getText().equals("")) {
                        modelevol.filterTime(t.getText());
                    }
                }
            }

        });

        affichercarte.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                if (!flightsFilePath.equals("")){
                    viewPanel = new MapViewerPanel(flightsFilePath);
                    viewPanel.visualize();}
                else{
                    viewPanel.visualizeVierge();}
            }

        });
    }
//creer le panel d'affichage des vols avec filtrages
    private void initAffichage() {

        volPanel = new JPanel();
        volPanel.setLayout(new GridBagLayout());
        GridBagConstraints con = new GridBagConstraints();
        con.fill = GridBagConstraints.BOTH;
        con.insets=new Insets(10,10,10,10);
        triid = new JLabel("tri par ID");
        triid.setBorder(new LineBorder(Color.BLUE));
        triid.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 1;
        con.gridy = 2;
        volPanel.add(triid, con);

        tridep = new JLabel("tri par aéroport de départ");
        tridep.setBorder(new LineBorder(Color.BLUE));
        tridep.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 3;
        volPanel.add(tridep, con);

        triarrv = new JLabel("tri par aéroport d'arrivée");
        triarrv.setBorder(new LineBorder(Color.BLUE));
        triarrv.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 5;
        volPanel.add(triarrv, con);

        trih = new JLabel("tri par heure d'arrivée");
        trih.setBorder(new LineBorder(Color.BLUE));
        trih.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 7;
        volPanel.add(trih, con);

        trim = new JLabel("tri par minutes");
        trim.setBorder(new LineBorder(Color.BLUE));
        trim.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 9;
        volPanel.add(trim, con);
        
        trit = new JLabel("tri par temps");
        trit.setBorder(new LineBorder(Color.BLUE));
        trit.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 11;
        volPanel.add(trit, con);

        id = new JTextField(20);
        id.setHorizontalAlignment(SwingConstants.LEFT);
        con.gridx = 1;
        con.gridy = 3;
        volPanel.add(id, con);

        dep = new JTextField(20);
        dep.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 3;
        volPanel.add(dep, con);

        arrv = new JTextField(20);
        arrv.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 5;
        volPanel.add(arrv, con);

        h = new JTextField(20);
        h.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 7;
        volPanel.add(h, con);

        m = new JTextField(20);
        m.setHorizontalAlignment(SwingConstants.CENTER);
        con.gridx = 9;
        volPanel.add(m, con);

        t = new JTextField(20);
        t.setHorizontalAlignment(SwingConstants.RIGHT);
        con.gridx = 11;
        volPanel.add(t, con);

        con.gridx = 1;
        con.gridy = 5;
        volPanel.add(tablevols, con);
        
        
        con.gridwidth = GridBagConstraints.REMAINDER; // Span all columns
        jsp.getViewport().setView(tablevols);
        volPanel.add(jsp, con);
        

        // Ajout des DocumentListener pour filtrer dynamiquement
        id.getDocument().addDocumentListener(new FiltreListener());
        dep.getDocument().addDocumentListener(new FiltreListener());
        arrv.getDocument().addDocumentListener(new FiltreListener());
        h.getDocument().addDocumentListener(new FiltreListener());
        m.getDocument().addDocumentListener(new FiltreListener());
        t.getDocument().addDocumentListener(new FiltreListener());

        setContentPane(volPanel);
        pack();

    }

    private class FiltreListener implements DocumentListener {

        @Override
        public void insertUpdate(DocumentEvent e) {
            appliquerFiltres();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            appliquerFiltres();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            appliquerFiltres();
        }
    }

    //a modif
    private void appliquerFiltres() {
        String filtreId = id.getText().trim().toLowerCase();
        String filtreDep = dep.getText().trim().toLowerCase();
        String filtreArrv = arrv.getText().trim().toLowerCase();
        String filtreH = h.getText().trim().toLowerCase();
        String filtreM = m.getText().trim().toLowerCase();
        String filtreT = t.getText().trim().toLowerCase();


        // Filtrage par ID
        if (!filtreId.isEmpty()) {
            modelevol.filterId(filtreId);
        }

        // Filtrage par Départ
        if (!filtreDep.equals("")) {
            modelevol.filterDep(filtreDep);
        }

        // Filtrage par Arrivée
        if (!filtreArrv.equals("")) {
            modelevol.filterArr(filtreArrv);
        }

        // Filtrage par Heure
        if (!filtreH.isEmpty()) {
            modelevol.filterH(filtreH);
        }

        // Filtrage par Minutes
        if (!filtreM.isEmpty()) {
            modelevol.filterM(filtreM);
        }

        // Filtrage par Temps
        if (!filtreT.isEmpty()) {
            modelevol.filterTime(filtreT);
        }

        tablevols.revalidate();
        tablevols.repaint();
    }

    public static JTextField getMaxMarginField() {
        return maxMarginField;
    }

}
