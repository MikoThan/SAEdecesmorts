/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.graphe;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;

/**
 *
 * @author p2300399
 */
public class Algos {
    public static void coloriergraphe(Graph g, int kmax,  String attribut) {
        int max = kmax;
        Color[] cols = new Color[max + 1];
        for (int i = 0; i <= max; i++) {
            cols[i] = Color.getHSBColor((float) (Math.random()), 0.8f, 0.9f);
        }
        
        for (Node n : g) {
            int col = n.getAttribute(attribut);
            if (n.hasAttribute("ui.style")) {
                n.setAttribute("ui.style", "fill-color:rgba(" + cols[col].getRed() + "," + cols[col].getGreen() + "," + cols[col].getBlue() + ",200);");
            } else {
                n.addAttribute("ui.style", "fill-color:rgba(" + cols[col].getRed() + "," + cols[col].getGreen() + "," + cols[col].getBlue() + ",200);");
            }
        }
    }
   
    
    private static void inserer(ArrayList<Node> tab, int i) {
        Node tmp;
        while (i > 0 && (int)tab.get(i).getDegree() > (int)tab.get(i - 1).getDegree()) {
            tmp = tab.get(i - 1);
            tab.set(i - 1, tab.get(i));
            tab.set(i, tmp);
            i -= 1;
        }
    }

    private static void tri(ArrayList<Node> tab, int taille) {
        for (int i = 1; i < taille; i++) {
            inserer(tab, i);
        }
    }

    public static void Dsatur(Graph g, int kmax) {
        ArrayList<Node> noeuds = new ArrayList<>();
        ArrayList<Node> noeuds_traites = new ArrayList<>();
        int[] nb_fois_coul = new int[kmax];
        int nb_conf = 0;


        for (Node n : g) {
            noeuds.add(n);
            n.addAttribute("color", 0); 
            n.addAttribute("dsat", n.getDegree());
        }


        tri(noeuds, g.getNodeCount());

        while (!noeuds.isEmpty()) {
            Node n = noeuds.remove(0);
            noeuds_traites.add(n);

            if ((int)n.getAttribute("color") == 0) {
                boolean est_assignee_coul = false;
                
                //check des couleurs des voisins #cçaquimanquaitsinonctnaze
                boolean[] coul_used = new boolean[kmax];
                Iterator<Node> it = n.getNeighborNodeIterator();
                while (it.hasNext()) {
                    Node voisin = it.next();
                    int voisinColor = (int)voisin.getAttribute("color");
                    if (voisinColor != 0) {
                        coul_used[voisinColor - 1] = true;
                    }
                }
                // trouve une couleur non attribuée et l'attribue
                    for (int i = 0; i < kmax; i++) {
                        if (! coul_used[i]) {
                            n.setAttribute("color", i + 1);
                            nb_fois_coul[i]++;
                            est_assignee_coul=true;
                            break;
                        }
                    }

                // si toutes les couleurs sont attribuées alors on attribue la couleur la moins attribuée
                if (! est_assignee_coul) {
                    int nouvcoul = cherchemini(nb_fois_coul, kmax);
                    n.setAttribute("color", nouvcoul+1);
                    nb_fois_coul[nouvcoul]++;
                }
            }

            // Update des dsat des noeuds voisins
            Iterator<Node> it = n.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node voisin = it.next();
                if ((int)voisin.getAttribute("color") == 0) {
                    int dsat = (int)voisin.getAttribute("dsat");
                    voisin.setAttribute("dsat", dsat + 1);
                }
            }
        }
        //update du nb de conflits si deux noeuds adjacents ont la même couleur et si le noeud voisin n'a pas déjà été traité
        for(Node n: g){
            Iterator<Node> it = n.getNeighborNodeIterator();
            while(it.hasNext()){
                Node voisin = it.next();
                if ((voisin.getAttribute("color")==n.getAttribute("color"))&&(! ispresent(noeuds_traites, voisin)))
                    nb_conf++;
            }
            noeuds_traites.remove(n);
        }

        System.out.println("Nombre de conflits: " + nb_conf);
    }
    //renvoie true si cible est dans le tableau de noeud
    public static boolean ispresent(ArrayList<Node> nodetab,Node cible){
        boolean present = false;
        for(Node n: nodetab){
            if (n.equals(cible))
                present=true;
        }
        return present;
    }
    
    //cherche l'indice de la couleur la moins utilisée et le renvoie
    private static int cherchemini(int[] nb_fois_coul, int kmax){
        int mini=0;
        for (int i = 0; i < kmax; i++) {
            if (nb_fois_coul[i]<nb_fois_coul[mini]){
                mini=i;
            }
        }
        return mini;
    }
    
    public static void WelshPowell(Graph g, int kmax){
        
        ArrayList<Node> noeuds = new ArrayList<>();
        ArrayList<Node> noeuds_traites = new ArrayList<>();
        int[] nb_fois_coul = new int[kmax];
        int nb_conf = 0;


        for (Node n : g) {
            noeuds.add(n);
            n.addAttribute("wp", 0); 
        }
        tri(noeuds, g.getNodeCount());
        
        while (!noeuds.isEmpty()) {
            Node n = noeuds.remove(0);
            Set<Integer> neighborColors = new HashSet<>();

            // récupère les couleurs de tous les noeuds
            Iterator<Node> it = n.getNeighborNodeIterator();
            while (it.hasNext()) {
                Node voisin = it.next();
                int color = (int) voisin.getAttribute("wp");
                if (color != 0) {
                    neighborColors.add(color);
                }
            }

            // donne la plus petite couleur non atribuée possible
            int assignedColor = 1;
            while (neighborColors.contains(assignedColor)) {
                assignedColor++;
            }

            // si jamais la couleur assignée est supérieure au kmax alors ya un conflit
            if (assignedColor > kmax) {
                nb_conf++;
            } else {
                n.setAttribute("wp", assignedColor);
            }
            
        }
        System.out.println("Nombre de conflits: " + nb_conf);
    }
    
    private static boolean estVoisin(Node cible, Node n, Graph g){
        boolean estvoisin = false;
        if (n.getEdgeToward(n)==cible)
            estvoisin=true;
        
        return estvoisin;
    }
    
    
}


  