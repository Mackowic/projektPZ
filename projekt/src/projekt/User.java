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
public class User {

    private final SimpleStringProperty Nazwisko;
    private final SimpleStringProperty Imie;

    /**
     * Konstruktor klasy User
     *
     * @param Nazwisko - nazwisko uzytkownika
     * @param Imie - imie uzytkownika
     */
    public User(String Nazwisko, String Imie) {

        this.Nazwisko = new SimpleStringProperty(Nazwisko);
        this.Imie = new SimpleStringProperty(Imie);
    }

    /**
     * Metoda getNazwisko() pobierajaca nazwisko uzytkownika ze zmiennej
     * Nazwisko
     *
     * @return zwraca nazwisko uzytkownika
     */
    public String getNazwisko() {
        return Nazwisko.get();
    }

    /**
     * Metoda getImie() pobierajaca imie uzytkownika ze zmiennej Imie
     *
     * @return zwraca imie uzytkownika
     */
    public String getImie() {
        return Imie.get();
    }

    /**
     * Metoda setNazwisko() ktora ustawia wartosc zmiennej Nazwisko ze zmiennej
     * nazwisko
     *
     * @param nazwisko - nazwisko uzytkownika
     */
    public void setNazwisko(String nazwisko) {
        Nazwisko.set(nazwisko);
    }

    /**
     * Metoda setImie() ktora ustawia wartosc zmiennej Imie ze zmiennej imie
     *
     * @param imie - imie uzytkownika
     */
    public void setImie(String imie) {
        Imie.set(imie);
    }
}
