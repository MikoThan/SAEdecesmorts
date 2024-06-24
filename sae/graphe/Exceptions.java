/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package sae.graphe;

/**
 *
 * @author p2300399
 */
public class Exceptions extends Exception {
    
    
    public static class FileFormatError extends Exception{
        
        public FileFormatError(String nom_fichier){
            super("le fichier "+nom_fichier+" n'est pas en .txt");
        }
    }
    
    public static class FileReadError extends Exception{
        
        public FileReadError(String nom_fichier){
            super("le fichier "+nom_fichier+" comporte une anomalie");
        }
    }
    
    public static class AeroportError extends Exception{
        
        public AeroportError(){
            super("le fichier des aéroports n'a pas le bon format");
        }
    }
}
