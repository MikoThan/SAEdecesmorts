/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.graphe;

import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.SingleGraph;

import java.io.FileInputStream;
import java.util.Scanner;


/**
 * @author iuseh
 */
public class Graphegen {

    Graph graph = new SingleGraph("");
    private Aeroport[] taba;
    private Vol[] tabv;
    private int nbaeroports, nbvols;

    public Graphegen(String nFichierA, String nFichierV) {
        this.taba = chargerAeroports(nFichierA);
        this.tabv = chargerVols(nFichierV);
    }

    private Aeroport trouverAero(String na) {
        for (Aeroport a : taba) {
            if (a.getCode().equals(na)) {
                return a;
            }
        }
        return null;
    }

    private void Intersection(Vol v1, Vol v2) {
        if (
                (v1.conflit(v2))

                        &&

                        !graph.getNode(v1.getIdVol()).hasEdgeBetween(v2.getIdVol())

        ) {
            System.out.println(v1.getDep().getCode() + "-" + v1.getArrv().getCode() + " en conflit avec " + v2.getDep().getCode() + "-" + v2.getArrv().getCode());

            graph.addEdge(v1.getIdVol() + v2.getIdVol(), v1.getIdVol(), v2.getIdVol());
        }
    }

    private int compteAeroports(String nom_fichier) {
        int cpt = 0;
        try {
            FileInputStream file = new FileInputStream(nom_fichier);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                cpt++;
                scanner.nextLine();
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.nbaeroports = cpt;
        return cpt;
    }

    public Aeroport[] chargerAeroports(String nom_fichier) {
        Aeroport[] tab = new Aeroport[compteAeroports(nom_fichier)];
        try {
            FileInputStream file = new FileInputStream(nom_fichier);
            Scanner scanner = new Scanner(file);
            int i = 0;
            while (scanner.hasNextLine()) {
                String cour = scanner.nextLine();
                String[] split_cour = cour.split(";");
                tab[i] = new Aeroport(split_cour[0], split_cour[1], Integer.valueOf(split_cour[2]), Integer.valueOf(split_cour[3]), Integer.valueOf(split_cour[4]), Integer.valueOf(split_cour[6]), Integer.valueOf(split_cour[7]), Integer.valueOf(split_cour[8]),split_cour[5].charAt(0),split_cour[9].charAt(0));
                i++;
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }

    private int compteVols(String nom_fichier) {
        int cpt = 0;
        try {
            FileInputStream file = new FileInputStream(nom_fichier);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                cpt++;
                scanner.nextLine();
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.nbvols = cpt;
        return cpt;
    }

    public Vol[] chargerVols(String nom_fichier) {
        Vol[] tab = new Vol[compteVols(nom_fichier)];
        try (
            FileInputStream file = new FileInputStream(nom_fichier);
            Scanner scanner = new Scanner(file)){
            int i = 0;
            while (scanner.hasNextLine()) {
                String cour = scanner.nextLine();
                String[] split_cour = cour.split(";");
                tab[i] = new Vol(split_cour[0], this.trouverAero(split_cour[1]), this.trouverAero(split_cour[2]), Integer.parseInt(split_cour[3]), Integer.parseInt(split_cour[4]), Integer.parseInt(split_cour[5]));
                i++;
            }
            scanner.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }

    public Graph genGraph() {
        for (Vol v : tabv) {
            Node node = graph.addNode(v.getIdVol());
            node.setAttribute("ui.label", v.getDep().getCode()+"-"+v.getArrv().getCode());
        }
        for (Vol v : tabv) {
            for (Vol vp : tabv) {
                if (!v.equals(vp)) {
                    Intersection(v, vp);
                }
            }
        }
        return graph;
    }

    public class Point {
        public double x;
        public double y;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    public Vol[] getTabv() {
        return tabv;
    }

    public int getNbaeroports() {
        return nbaeroports;
    }

    public int getNbvols() {
        return nbvols;
    }

    
    
}
