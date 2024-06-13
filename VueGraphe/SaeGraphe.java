/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package VueGraphe;

import java.util.ArrayList;
import javax.swing.SwingUtilities;
import org.graphstream.graph.Graph;
import sae.graphe.Algos;
import sae.graphe.Graphe;

/**
 *
 * @author p2300399
 */
public class SaeGraphe {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //12
        SwingUtilities.invokeLater(()->{
            Fenetre f = new Fenetre();
            f.setVisible(true);
        });
//        Graphe.chargerGraphe("src/SourceFile/graph-test12.txt");
//        Graphe.getGraphe().display();
//        Algos.WelshPowell(Graphe.getGraphe(), Graphe.getKmax()); //a retoucher
//        Algos.coloriergraphe(Graphe.getGraphe(), Graphe.getKmax(), "wp");
//        Algos.Dsatur(Graphe.getGraphe(), Graphe.getKmax());
//        Algos.coloriergraphe(Graphe.getGraphe(), Graphe.getKmax(), "color");
    }

}
