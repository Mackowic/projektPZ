/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Maciek
 */
public class Tasks3 {

    private final SimpleStringProperty Nazwa;
    private final SimpleStringProperty Projekt;

    /**
     * Konstruktor klasy Tasks3
     *
     * @param Nazwa - nazwa zadania
     * @param Autor - autor zadania
     * @param Projekt - projekt w ktorym jest zadanie
     */
    public Tasks3(String Nazwa, String Projekt) {

        this.Nazwa = new SimpleStringProperty(Nazwa);
        this.Projekt = new SimpleStringProperty(Projekt);
    }

    /**
     * Metoda getNazwa() pobierajaca nazwe zadania ze zmiennej Nazwa
     *
     * @return zwraca nazwe zadania
     */
    public String getNazwa() {
        return Nazwa.get();
    }

    /**
     * Metoda getProjekt() pobierajaca nazwe projektu w ktorym jest zadanie ze
     * zmiennej Projekt
     *
     * @return zwraca nazwe projektu w ktorym jest zadanie
     */
    public String getProjekt() {
        return Projekt.get();
    }

    /**
     * Metoda setNazwa() ktora ustawia wartosc zmiennej Nazwa ze zmiennej nazwa
     *
     * @param nazwa - nazwa zadania
     */
    public void setNazwa(String nazwa) {
        Nazwa.set(nazwa);
    }

    /**
     * Metoda setProjekt() ktora ustawia wartosc zmiennej Projekt ze zmiennej
     * projekt
     *
     * @param projekt - nazwa projektu w ktorym jest zadanie
     */
    public void setProjekt(String projekt) {
        Projekt.set(projekt);
    }
}
