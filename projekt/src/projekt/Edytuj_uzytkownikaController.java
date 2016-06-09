/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Edytuj_uzytkownikaController implements Initializable {

    @FXML
    private Button b_zapisz;
    @FXML
    private Button b_wstecz;
    @FXML
    private TextField f_imie;
    @FXML
    private TextField f_nazwisko;
    @FXML
    private TextField f_email;
    @FXML
    private TextField f_telefon;
    @FXML
    private ComboBox<String> c_status;

    public static String ranga, ranga1 = "";

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            UzytkownicyController uzy = new UzytkownicyController();

            f_imie.setText(uzy.Imie);
            f_nazwisko.setText(uzy.Nazwisko);
            f_email.setText(uzy.Mail);
            f_telefon.setText(uzy.Telefon);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
            PreparedStatement statment = con.prepareStatement("select ranga from uzytkownicy where Mail='" + f_email.getText() + "'");
            ResultSet result = statment.executeQuery();

            if (result.next()) {
                System.out.println("ranga : " + result.getString(1));
                ranga = result.getString(1);// do testowania
            }

            statment = con.prepareStatement("select Ranga from uprawnienia where idUprawnienia='" + result.getString(1) + "'");
            result = statment.executeQuery();
            if (result.next()) {
                System.out.println("nazwa rangi : " + result.getString(1)); // do testowania
            }
            c_status.setPromptText(result.getString(1));

            ObservableList<String> options
                    = FXCollections.observableArrayList(
                            "VIP",
                            "Pracownik"
                    );
            c_status.setItems(options);

            /**
             * Anonimowa metoda dla przycisku b_anuluj - otwiera widok
             * Projekt_X.fxml
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
                        Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
                        Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        uzytkownicy_stage.setScene(uzytkownicy_scene);
                        uzytkownicy_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            /**
             * Anonimowa metoda dla przycisku b_zapisz - zatwierdza dokonane
             * zmiany w danych uzytkownika , updatuje baze i otwiera widok
             * uzytkownicy.fxml
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             * @exeption NullPointerException ex - wyjatek wytepujacych kiedy
             * jedna z wykorzystywaneych zmiennych jest nullem
             */
            b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjaPola() && walidacjaImie() && walidacjaNazwisko() && walidacjaTelefon() && walidacjaEmail()) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment;
                            ResultSet result;
                            System.out.println("ID uzytkownika" + uzy.ID);
                            System.out.println("Imie : " + f_imie.getText());
                            //zapytanie do zmiany
                            System.out.println("wartosc tego gowna : " + c_status.getValue());

                            try {
                                if (c_status.getValue() == null) {
                                    ranga1 = ranga;
                                    System.out.println("wartosc normalne : " + ranga1);
                                } else {
                                    ranga1 = c_status.getValue();
                                    statment = con.prepareStatement("select idUprawnienia from uprawnienia where Ranga='" + ranga1 + "'");
                                    result = statment.executeQuery();
                                    if (result.next()) {
                                        System.out.println("iduprawnienia : " + result.getString(1)); // do testowania
                                        ranga1 = result.getString(1);
                                    }
                                }
                            } catch (NullPointerException ex) {
                                ranga1 = ranga;
                                System.out.println("wartosc w errorze : " + ranga1);

                            }

                            System.out.println("wartosc null : " + ranga1);

                            statment = con.prepareStatement("UPDATE uzytkownicy SET imie='" + f_imie.getText() + "', nazwisko='" + f_nazwisko.getText() + "', mail='" + f_email.getText() + "' ,telefon='" + f_telefon.getText() + "', ranga='" + ranga1 + "' WHERE  idUzytkownika='" + uzy.ID + "'");
                            statment.executeUpdate();

                            Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
                            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
                            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            uzytkownicy_stage.setScene(uzytkownicy_scene);
                            uzytkownicy_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {

                            if (ex.getCause() instanceof Exception) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                DialogPane dialogPane = alert.getDialogPane();
                                dialogPane.getStylesheets().add(
                                        getClass().getResource("/css/myDialogs.css").toExternalForm());
                                dialogPane.getStyleClass().add("myDialog");
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Błąd połączenia z bazą danych");
                                alert.showAndWait();
                            } else {
                                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Edytuj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }

                private boolean walidacjaPola() {
                    if (f_imie.getText().isEmpty() | f_nazwisko.getText().isEmpty()
                            | f_email.getText().isEmpty() | f_telefon.getText().isEmpty()) {

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Puste pole");
                        alert.setHeaderText(null);
                        alert.setContentText("Wpisz dane do pola");
                        alert.showAndWait();

                        return false;
                    }
                    return true;
                }

                //Artur
                /**
                 * Metoda sprawdzająca czy pole emailu jest poprawne i ma
                 * wymagane znaki
                 *
                 * @author Artur
                 * @return zwraca wartosc true lub false
                 */
                private boolean walidacjaEmail() {
                    Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-­Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z­]{2,})$");
                    Matcher m = p.matcher(f_email.getText());
                    if (m.find() && m.group().equals(f_email.getText())) {
                        return true;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Nie poprawny e-mail");
                        alert.setHeaderText(null);
                        alert.setContentText("Wprowadź poprawny e-mail");
                        alert.showAndWait();

                        return false;
                    }

                }

                //Artur
                /**
                 * Metoda sprawdzająca czy pole imienia jest poprawne i ma
                 * wymagane znaki
                 *
                 * @author Artur
                 * @return zwraca wartosc true lub false
                 */
                private boolean walidacjaImie() {
                    Pattern p = Pattern.compile("[a-zA-ZĄŚŻŹĆŃŁÓĘąśżźćńłóę]+");
                    Matcher m = p.matcher(f_imie.getText());
                    if (m.find() && m.group().equals(f_imie.getText())) {
                        return true;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Nie poprawne imię");
                        alert.setHeaderText(null);
                        alert.setContentText("Wprowadź poprawne imię bez znaków specjalnych i liter !@#$%^&*(){}[]?/<>: itp.");
                        alert.showAndWait();

                        return false;
                    }

                }

                //Artur
                /**
                 * Metoda sprawdzająca czy pole nazwiska jest poprawne i ma
                 * wymagane znaki
                 *
                 * @author Artur
                 * @return zwraca wartosc true lub false
                 */
                private boolean walidacjaNazwisko() {
                    Pattern p = Pattern.compile("[a-zA-ZĄŚŻŹĆŃŁÓĘąśżźćńłóę]+");
                    Matcher m = p.matcher(f_nazwisko.getText());
                    if (m.find() && m.group().equals(f_nazwisko.getText())) {
                        return true;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Nie poprawne Nazwisko");
                        alert.setHeaderText(null);
                        alert.setContentText("Wprowadź poprawne Nazwisko bez znaków specjalnych i liter !@#$%^&*(){}[]?/<>: itp.");
                        alert.showAndWait();

                        return false;
                    }

                }

                //Artur
                /**
                 * Metoda sprawdzająca czy pole telefonu jest poprawne i ma
                 * wymagane znaki
                 *
                 * @author Artur
                 * @return zwraca wartosc true lub false
                 */
                private boolean walidacjaTelefon() {
                    Pattern p = Pattern.compile("[0-9]+");
                    Matcher m = p.matcher(f_telefon.getText());
                    if (m.find() && m.group().equals(f_telefon.getText())) {
                        return true;

                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Nie poprawny nr.telefonu");
                        alert.setHeaderText(null);
                        alert.setContentText("Wprowadź poprawny nr.telefonu");
                        alert.showAndWait();

                        return false;
                    }

                }
            });
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Edytuj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Edytuj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
