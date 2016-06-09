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
public class Tasks {

    private final SimpleStringProperty Nazwa;
    private final SimpleStringProperty Opis;
    private final SimpleStringProperty Status;

    /**
     * Konstruktor klasy Tasks
     *
     * @param Nazwa - nazwa zadania
     * @param Opis - opis zadania
     * @param Status - status zadania
     */
    public Tasks(String Nazwa, String Opis, String Status) {

        this.Nazwa = new SimpleStringProperty(Nazwa);
        this.Opis = new SimpleStringProperty(Opis);
        this.Status = new SimpleStringProperty(Status);
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
     * Metoda getStatus() pobierajaca status zadania ze zmiennej Status
     *
     * @return zwraca status zadania
     */
    public String getStatus() {
        return Status.get();
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
     * Metoda setStatus() ktora ustawia wartosc zmiennej Status ze zmiennej
     * status
     *
     * @param status - status zadania
     */
    public void setStatus(String status) {
        Status.set(status);
    }
}
