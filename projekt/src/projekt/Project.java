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
public class Project {

    private final SimpleStringProperty Nazwa;
    private final SimpleStringProperty Opis;
    private final SimpleStringProperty Poczatek;
    private final SimpleStringProperty Koniec;

    /**
     * Konstruktor klasy Project
     *
     * @param Nazwa - nazwa projektu
     * @param Opis - opis projektu
     * @param Poczatek - data rozpoczecia
     * @param Koniec - data zakonczenia
     */
    public Project(String Nazwa, String Opis, String Poczatek, String Koniec) {

        this.Nazwa = new SimpleStringProperty(Nazwa);
        this.Opis = new SimpleStringProperty(Opis);
        this.Poczatek = new SimpleStringProperty(Poczatek);
        this.Koniec = new SimpleStringProperty(Koniec);
    }

    /**
     * Metoda getNazwa() pobierajaca nazwe projektu ze zmiennej Nazwa
     *
     * @return zwraca nazwe projektu
     */
    public String getNazwa() {
        return Nazwa.get();
    }

    /**
     * Metoda getOpis() pobierajaca opis projektu ze zmiennej Opis
     *
     * @return zwraca opis projektu
     */
    public String getOpis() {
        return Opis.get();
    }

    /**
     * Metoda getPoczatek() pobierajaca date rozpoczecia projektu ze zmiennej
     * Poczatek
     *
     * @return zwraca date rozpoczecia projektu
     */
    public String getPoczatek() {
        return Poczatek.get();
    }

    /**
     * Metoda getKoniec() pobierajaca date zakonczenia projektu ze zmiennej
     * Koniec
     *
     * @return zwraca date zakonczenia projektu
     */
    public String getKoniec() {
        return Koniec.get();
    }

    /**
     * Metoda setNazwa() ktora ustawia wartosc zmiennej Nazwa ze zmiennej nazwa
     *
     * @param nazwa - nazwa projektu
     */
    public void setNazwa(String nazwa) {
        Nazwa.set(nazwa);
    }

    /**
     * Metoda setOpis() ktora ustawia wartosc zmiennej Opis ze zmiennej opis
     *
     * @param opis - opis projektu
     */
    public void setOpis(String opis) {
        Opis.set(opis);
    }

    /**
     * Metoda setPoczatek() ktora ustawia wartosc zmiennej Poczatek ze zmiennej
     * poczatek
     *
     * @param poczatek - data rozpoczecia projektu
     */
    public void setPoczatek(String poczatek) {
        Poczatek.set(poczatek);
    }

    /**
     * Metoda setKoniec() ktora ustawia wartosc zmiennej Koniec ze zmiennej
     * koniec
     *
     * @param koniec - data zakonczenia projektu
     */
    public void setKoniec(String koniec) {
        Koniec.set(koniec);
    }
}
