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
	

	public User(String Nazwisko, String Imie){

		this.Nazwisko = new SimpleStringProperty(Nazwisko);
		this.Imie = new SimpleStringProperty(Imie);
	}
public String getNazwisko(){
	return Nazwisko.get();
}
public String getImie(){
	return Imie.get();
}

public void setNazwisko(String nazwisko){
	Nazwisko.set(nazwisko);
}
public void setImie(String imie){
	Imie.set(imie);
}
}