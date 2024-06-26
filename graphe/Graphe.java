/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.graphe;

/**
 *
 * @author p2300399
 */
import java.awt.BorderLayout;
import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import javax.swing.JPanel;
import sae.graphe.Exceptions.FileFormatError;
import sae.graphe.Exceptions.FileReadError;

public class Graphe extends JPanel{
    private static Graph g;
    private static int kmax=0, conf=0;

    public static int getConf() {
        return conf;
    }

    public static void setConf(int conf) {
        Graphe.conf = conf;
    }

    public static Graph getGraphe(){
        return g;
    }

    public static int getKmax() {
        return kmax;
    }
    //charge un ficier graphe sous format .txt


    /**
     * Charge un graphe à partir d'un fichier
     * @param nom_fichier Le nom du fichier à charger.
     * @throws FileFormatError Si le format du fichier est incorrect.
     * @throws FileNotFoundException Si le fichier n'est pas trouvé.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    public static void chargerGraphe (String nom_fichier) throws FileFormatError, FileNotFoundException, IOException {
        int nbsommets=0;
        g = new SingleGraph(nom_fichier);

        // Le fichier d'entrée
        try (BufferedReader br = new BufferedReader(new FileReader(new File(nom_fichier)))) {
            if (!nom_fichier.contains("txt")||!nom_fichier.contains("graph")){
                throw new FileFormatError(nom_fichier);
            }
            else{
                kmax = Integer.parseInt(br.readLine());
                nbsommets=Integer.parseInt(br.readLine());
            String ligne;
            while (((ligne = br.readLine()) != null)) {
                try{
                    if(ligne.length()>9){
                        throw new FileReadError(nom_fichier);
                    }
                    else if(ligne.length()<3){
                        throw new FileReadError(nom_fichier);
                    }
                    else{
                String[] ligneDecouper = ligne.split(" ");
                String som1 = ligneDecouper[0];
                String som2 = ligneDecouper[1];

                //ajout ou non des sommets au graphe
                if (!hasNode(g, som1)) {
                    g.addNode(som1);
                    }
                if (!hasNode(g, som2)) {
                    g.addNode(som2);
                    }
                //ajout ou non de l'arête entre les sommets si non reliés
                if (!existEdge(g, som1, som2)) {
                    g.addEdge("" + som1 + som2+Math.random(), som1, som2);
                    }
                    }
                }
                catch (EdgeRejectedException | ElementNotFoundException | IdAlreadyInUseException | FileReadError e){
                    System.out.println(e.getMessage());
                }
            }
//            g.display();
            }
        }
        catch(FileFormatError e){
            System.out.println(e.getMessage());
        }
    }

    /**
     * Vérifie si un noeud est déjà dans le graphe
     * @param g Le graphe à vérifier.
     * @param sommet Le sommet à vérifier.
     * @return true si le noeud est déjà dans le graphe, false sinon.
     */
    private static boolean hasNode(Graph g, String sommet) {
        boolean present = false;
        for (Node n : g) {
            if (n.getId().equals(sommet)) {
                present = true;
            }

        }
        return present;
    }

    /**
     * Vérifie si une arête existe entre deux sommets
     * @param g Le graphe à vérifier.
     * @param s1 Le premier sommet.
     * @param s2 Le deuxième sommet.
     * @return true si l'arête existe, false sinon.
     */
    private static boolean existEdge(Graph g, String s1, String s2) {
        boolean exist = false;
        for (Node n : g) {
            if (n.getId().equals(s1)) {
                Iterator<Node> it = n.getNeighborNodeIterator();
                while (it.hasNext() && (!exist)) {
                    Node voisin = it.next();
                    if (voisin.getId().equals(s2)) {
                        exist = true;
                    }
                }
            }

        }
        return exist;
    }

    /**
     * Calcule le degré moyen du graphe
     * @param g Le graphe à utiliser pour le calcul.
     * @return Le degré moyen du graphe.
     */
    public static double calculedegremoyen(Graph g){
        int nbdegres=0, somme_degres=0;
        for(Node n:g){
            somme_degres+=n.getDegree();
            nbdegres++;
        }
        return somme_degres/nbdegres;
    }

    /**
     * Calcule et affiche le nombre de noeuds d'un graphe
     * @param g Le graphe à utiliser pour le calcul.
     * @return Le nombre de noeuds du graphe.
     */
    public static int afficheNbNoeuds(Graph g){
        int nbnoeuds=0;
        for(Node n:g){
            nbnoeuds++;
            }
        return nbnoeuds;
    }


    /**
     * Calcule et affiche le nombre d'arêtes d'un graphe
     * @param g Le graphe à utiliser pour le calcul.
     * @return Le nombre d'arêtes du graphe.
     */
    public static int afficheNbAretes(Graph g){
        ArrayList<Node> noeuds = new ArrayList<>();
        int nbaretes=0;
        for(Node n:g){
            noeuds.add(n);
            Iterator<Node> ite=n.getNeighborNodeIterator();
            while(ite.hasNext()){
                Node voisin=ite.next();
                if (!Algos.ispresent(noeuds, voisin))
                    nbaretes++;
            }
        }
        return nbaretes;
    }


    /**
     * Calculer et affiche le nombre de composantes connexes d'un graphe
     * @param g Le graphe à utiliser pour le calcul.
     * @return Le nombre de composantes connexes du graphe.
     */
    public static int afficheNbComposantesConnexes(Graph g) {
        int nbCompo = 0;

        // Marque tous les nœuds comme non visités
        for (Node n : g) {
            n.setAttribute("visite", 0);
        }

        // Parcourt tous les nœuds et effectue un parcours en largeur pour chaque nœud non visité
        for (Node n : g) {
            if ((int)n.getAttribute("visite") == 0) {
                rechercheprofondeur(g, n);
                nbCompo++;
            }
        }

        return nbCompo;
    }


    /**
     * Effectue une recherche en profondeur
     * @param g Le graphe à utiliser pour la recherche.
     * @param start Le noeud de départ pour la recherche.
     */
    private static void rechercheprofondeur(Graph g, Node start) {
        Stack<Node> pile = new Stack<>();
        pile.push(start);

        while (!pile.isEmpty()) {
            Node courant = pile.pop();

            if ((int)courant.getAttribute("visite") == 0) {
                courant.setAttribute("visite", 1);

                Iterator<Node> ite = courant.getNeighborNodeIterator();
                while (ite.hasNext()) {
                    Node voisin = ite.next();
                    if ((int)voisin.getAttribute("visite") == 0) {
                        pile.push(voisin);
                    }
                }
            }
        }
    }

    /**
     * Calcule et affiche le diamètre d'un graphe
     * @param g Le graphe à utiliser pour le calcul.
     * @return Le diamètre du graphe.
     */
    public static int affichediametre(Graph g){
        int diametre=0, i=0;
        for(Node n:g){
            n.addAttribute("id", i);
            i++;
        }
        for(Node n:g){
            int dmax=parcourslargeur(g, n);
            if (dmax>diametre){
                diametre=dmax;
            }  
        }
        return diametre;
    }

    /**
     * Effectuer une recherche grace à un parcours en largeur
     * @param g Le graphe à utiliser pour la recherche.
     * @param n Le noeud de départ pour la recherche.
     * @return La distance maximale trouvée lors de la recherche.
     */
    private static int parcourslargeur(Graph g, Node n){
        int taille=g.getNodeCount();
        int[] distance= new int[taille];
        for(int i=0;i<taille;i++){
            distance[i]=-1;
        }
//        Arrays.fill(distance, -1);
        Queue<Node> queue=new LinkedList<>();
        distance[n.getAttribute("id")]=0;
        queue.add(n);
        int dmax=0;
        
        while (!queue.isEmpty()) {
            Node u = queue.poll();
            Iterator<Node> ite = u.getNeighborNodeIterator();
            while (ite.hasNext()) {
                Node voisin = ite.next();
                int vid = voisin.getAttribute("id");
                if (distance[vid] == -1) {  // v n'est pas encore visité
                    distance[vid] = distance[u.getAttribute("id")] + 1;
                    queue.add(voisin);
                }
            }
        }
        for(int dist:distance){
            if (dist>dmax)
                dmax=dist;
            }
        return dmax;
    }

}
