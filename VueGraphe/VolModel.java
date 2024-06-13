/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VueGraphe;

import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import javax.swing.table.TableColumnModel;
import sae.graphe.Vol;

/**
 *
 * @author p2300399
 */
public class VolModel extends AbstractTableModel {
    private ArrayList<Vol> vols = new ArrayList<>();
    private ArrayList<Vol> volsfiltres = new ArrayList<>();
    private final String[] colonnes = {"Identifiant", "Vol 1", "Vol 2", "Abscisse", "Ordonnée", "Côte"};

    public void loadVols(ArrayList<Vol> vols) {
        this.vols = vols;
        this.volsfiltres = new ArrayList<>(vols); // Initialement, pas de filtre
        fireTableDataChanged();
    }
    
    // Ajout d'un vol
    public void addVol(Vol vol) {
        vols.add(vol);
        volsfiltres.add(vol);
        fireTableDataChanged();
        fireTableRowsInserted(volsfiltres.size() - 1, volsfiltres.size() - 1);
    }
    

    // Suppression d'un vol
    public void removeVol(Vol vol) {
        int index = volsfiltres.indexOf(vol);
        if (index >= 0) {
            volsfiltres.remove(vol);
            fireTableRowsDeleted(index, index);
        }
        vols.remove(vol);
    }

    // Filtrage des vols
    public void filter(String filter) {
        volsfiltres.clear();
        for (Vol v : vols) {
            if (v.equals(filter)) {
                volsfiltres.add(v);
            }
        }
        fireTableDataChanged();
    }

    // Obtention d'un vol sélectionné
    public Vol getSelectedItem(Vol selectionvol) {
        int index = vols.indexOf(selectionvol);
        return vols.get(index);
    }

    // Nombre de lignes
    @Override
    public int getRowCount() {
        return volsfiltres.size();
    }

    // Nombre de colonnes
    @Override
    public int getColumnCount() {
        return colonnes.length;
    }

    // Obtention de la valeur à une cellule spécifique
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object vol = volsfiltres.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return colonnes[0]; 
            case 1:
                return colonnes[1];
            case 2:
                return colonnes[2];
            case 3:
                return colonnes[3];
            case 4:
                return colonnes[4];
            case 5:
                return colonnes[5];
            default:
                return null;
        }
    }

    // Nom des colonnes
    @Override
    public String getColumnName(int col) {
        return colonnes[col];
    }

    // Type des colonnes
    @Override
    public Class<?> getColumnClass(int colindex) {
        return String.class; // Modifier selon le type des colonnes
    }
}
