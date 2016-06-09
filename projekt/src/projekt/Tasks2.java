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
public class Tasks2 {

    private final SimpleStringProperty Nazwa;
    private final SimpleStringProperty Opis;
    private final SimpleStringProperty Projekt;

    /**
     * Konstruktor klasy Tasks2
     *
     * @param Nazwa - nazwa zadania
     * @param Opis - opis zadania
     * @param Projekt - projekt w ktorym jest zadanie
     */
    public Tasks2(String Nazwa, String Opis, String Projekt) {

        this.Nazwa = new SimpleStringProperty(Nazwa);
        this.Opis = new SimpleStringProperty(Opis);
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
     * Metoda getOpis() pobierajaca opis zadania ze zmiennej Opis
     *
     * @return zwraca opis zadania
     */
    public String getOpis() {
        return Opis.get();
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
     * Metoda setOpis() ktora ustawia wartosc zmiennej Opis ze zmiennej opis
     *
     * @param opis - opis zadania
     */
    public void setOpis(String opis) {
        Opis.set(opis);
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
