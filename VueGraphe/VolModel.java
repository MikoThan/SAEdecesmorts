/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package VueGraphe;

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.table.AbstractTableModel;
import sae.graphe.Aeroport;
import sae.graphe.Vol;

/**
 *
 * @author p2300399
 */
public class VolModel extends AbstractTableModel {
    private ArrayList<Vol> vols =new ArrayList<>();
    private String[] colonnes = {"identifiant", "depart","arrivee","heures","minutes","temps"};
    private ArrayList<Vol> volsfiltres =new ArrayList<>();
    private String filtreId = "";
    private String filtreDep = "";
    private String filtreArrv = "";
    private String filtreH = "";
    private String filtreM = "";
    private String filtreT = "";

    public void addVol(Vol v) {
        vols.add(v);
        volsfiltres.add(v);
        fireTableDataChanged();
    }

    public void removeVol(Vol v) {
        vols.remove(v);
        volsfiltres.remove(v);
        fireTableDataChanged();
    }

    public void reboot() {
        volsfiltres.clear();
        volsfiltres.addAll(vols);
    }

    public void filterId(String id) {
        filtreId = id.toLowerCase();
        appliquerFiltres();
    }

    public void filterDep(String dep) {
        filtreDep = dep.toLowerCase();
        appliquerFiltres();
    }

    public void filterArr(String arrv) {
        filtreArrv = arrv.toLowerCase();
        appliquerFiltres();
    }

    public void filterH(String h) {
        filtreH = h.toLowerCase();
        appliquerFiltres();
    }

    public void filterM(String m) {
        filtreM = m.toLowerCase();
        appliquerFiltres();
    }

    public void filterTime(String t) {
        filtreT = t.toLowerCase();
        appliquerFiltres();
    }

    private void appliquerFiltres() {
        reboot();
        Iterator<Vol> iterator = volsfiltres.iterator();
        while (iterator.hasNext()) {
            Vol v = iterator.next();

            if (!filtreId.isEmpty() && !String.valueOf(v.getIdVol()).toLowerCase().contains(filtreId)) {
                iterator.remove();
                continue;
            }
            if (!filtreDep.equals("") && !v.getDep().getCode().toLowerCase().contains(filtreDep)) {
                iterator.remove();
                continue;
            }    
            
            if (!filtreArrv.equals("") && !v.getArrv().getCode().toLowerCase().contains(filtreArrv)) {
                iterator.remove();
                continue;
            }
            if (!filtreH.isEmpty() && !String.valueOf(v.getH()).toLowerCase().contains(filtreH)) {
                iterator.remove();
                continue;
            }
            if (!filtreM.isEmpty() && !String.valueOf(v.getM()).toLowerCase().contains(filtreM)) {
                iterator.remove();
                continue;
            }
            if (!filtreT.isEmpty() && !String.valueOf(v.getDuree()).toLowerCase().contains(filtreT)) {
                iterator.remove();
            }
        }

        fireTableDataChanged();
    }
    
@Override
    public int getRowCount() {
        return volsfiltres.size();
    }

    @Override
    public int getColumnCount() {
        return colonnes.length;
    }

    @Override
    public String getColumnName(int columnIndex) {
        return colonnes[columnIndex];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex) {
            case 0:
                return Integer.class;
            case 1:
                return Aeroport.class;
            case 2:
                return Aeroport.class;
            case 3:
                return Integer.class;
            case 4:
                return Integer.class;
            case 5:
                return Integer.class;
            default:return Object.class;
        }
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Vol vol = volsfiltres.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return vol.getIdVol();
            case 1:
                return vol.getDep().getCode();
            case 2:
                return vol.getArrv().getCode();
            case 3:
                return vol.getH();
            case 4:
                return vol.getM();
            case 5:
                return vol.getDuree();
            default:
                return null;
        }
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}

