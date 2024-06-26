/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.graphe;

import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import org.graphstream.graph.Edge;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

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
   
    //sert pour le tri
    private static void inserer(ArrayList<Node> tab, int i) {
        Node tmp;
        while (i > 0 && (int)tab.get(i).getDegree() > (int)tab.get(i - 1).getDegree()) {
            tmp = tab.get(i - 1);
            tab.set(i - 1, tab.get(i));
            tab.set(i, tmp);
            i -= 1;
        }
    }
    //fonction de tri insertion
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
        Graphe.setConf(nb_conf);
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
    //pb : met des couleurs entre 0 et kmax
    public static void WelshPowell(Graph g, int kmax){
        
        ArrayList<Node> noeuds = new ArrayList<>();
        ArrayList<Node> noeuds_traites = new ArrayList<>();
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

             //on attribue la couleur au sommet en vérifiant qu'elle soit valide 
            if (assignedColor <= kmax)
                n.setAttribute("wp", assignedColor);
            else 
                assignedColor = findAvailableColor(neighborColors, kmax);
            
            
        }
        //update du nb de conflits si deux noeuds adjacents ont la même couleur et si le noeud voisin n'a pas déjà été traité
        for(Node n: g){
            noeuds_traites.add(n);
            Iterator<Node> it = n.getNeighborNodeIterator();
            while(it.hasNext()){
                Node voisin = it.next();
                if ((int)voisin.getAttribute("wp")==(int)n.getAttribute("wp")&&! ispresent(noeuds_traites, voisin))
                    nb_conf++;
            }
            noeuds_traites.remove(n);
        }
        Graphe.setConf(nb_conf);
    }
    
    private static int findAvailableColor(Set<Integer> neighborColors, int kmax) {
        for (int i = 1; i <= kmax; i++) {
            if (!neighborColors.contains(i)) {
                return i;
            }
        }
        return kmax+1;
    }
    
    private static String afficheCoulNode(Graph g, String attribut){
        String chaine="";
        for(Node n:g){
            chaine+=n.getId()+";"+n.getAttribute(attribut)+"\n";
        }
        return chaine;
    }
    
    public static void genererFile(Graph g, String attribut, String cheminfile){
        cheminfile=concatFile(cheminfile)+".txt";
        String filePath = "F:/GROSSE SAE C LA SAUCE/SAE JAVA/Evaluation/"+cheminfile;  // Chemin du fichier à créer
        
        
        String content;
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            content=afficheCoulNode(g, attribut);
            writer.write(content);
            System.out.println("Fichier créé avec succès.");
            writer.close();
        }catch (IOException e) {
            System.err.println("Une erreur s'est produite lors de la creation du fichier : " + e.getMessage());
        }
    }
    
   private static String concatFile(String chemin) {
        int lastSlashIndex = chemin.lastIndexOf('/');
        int dotIndex = chemin.indexOf('.', lastSlashIndex);

        if (lastSlashIndex == -1 || dotIndex == -1) {
            // Si pas de '/' ou de '.' après le '/', retourner une chaîne vide ou lancer une exception
            return "";
        }

        // Extraire la sous-chaîne entre le dernier '/' et le '.' après ce '/'
        return chemin.substring(lastSlashIndex + 1, dotIndex);
    }
}


  