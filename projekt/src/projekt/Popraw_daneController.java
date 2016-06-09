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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Popraw_daneController implements Initializable {

    @FXML
    private TextField f_imie;
    @FXML
    private TextField f_nazwisko;
    @FXML
    private TextField f_login;
    @FXML
    private TextField f_telefon;
    @FXML
    private Button b_zmien;
    @FXML
    private Button b_anuluj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            loginController login = new loginController();
            System.out.println(login.ble);
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?useUnicode=true&characterEncoding=utf-8", "root", "");
            PreparedStatement statment = con.prepareStatement("select Imie, Nazwisko, Mail, Telefon  from uzytkownicy where Mail='" + login.ble + "'");
            ResultSet result = statment.executeQuery();

            if (result.next()) {
                f_imie.setText(result.getString(1));
                f_nazwisko.setText(result.getString(2));
                f_login.setText(result.getString(3));
                f_telefon.setText(result.getString(4));// do testowania
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /**
         * Anonimowa metoda dla przycisku b_zmien - zatwierdza zmiane danych i
         * otwiera widok moje_dane.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        b_zmien.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (walidacjaPola() && walidacjaImie() && walidacjaNazwisko() && walidacjaTelefon() && walidacjaEmail()) {
                    try {
                        System.out.println(f_imie.getText());
                        loginController login = new loginController();
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                        PreparedStatement statment = con.prepareStatement("UPDATE uzytkownicy SET imie='" + f_imie.getText() + "', nazwisko='" + f_nazwisko.getText() + "', mail='" + f_login.getText() + "' ,telefon='" + f_telefon.getText() + "' WHERE  idHasla='" + login.id + "'");
                        statment.executeUpdate();
                        login.ble = f_login.getText();
                        try {

                            Parent moje_dane_parent = FXMLLoader.load(getClass().getResource("moje_dane.fxml"));
                            Scene moje_dane_scene = new Scene(moje_dane_parent);
                            Stage moje_dane_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            moje_dane_stage.setScene(moje_dane_scene);
                            moje_dane_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Popraw_daneController.class.getName()).log(Level.SEVERE, null, ex);
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
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("E-mail istnieje w bazie danych");
                            alert.showAndWait();
                        }
                    }

                }
            }

            //Artur
            /**
             * Metoda sprawdzająca czy wymagane pola nie są puste
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaPola() {
                if (f_imie.getText().isEmpty() | f_nazwisko.getText().isEmpty()
                        | f_login.getText().isEmpty() | f_telefon.getText().isEmpty()) {

                    Alert alert = new Alert(AlertType.WARNING);
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
             * Metoda sprawdzająca czy pole emailu jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaEmail() {
                Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-­Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z­]{2,})$");
                Matcher m = p.matcher(f_login.getText());
                if (m.find() && m.group().equals(f_login.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(AlertType.WARNING);
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
             * Metoda sprawdzająca czy pole imienia jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaImie() {
                Pattern p = Pattern.compile("[a-zA-Ząśżźćńłóę]+");
                Matcher m = p.matcher(f_imie.getText());
                if (m.find() && m.group().equals(f_imie.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(AlertType.WARNING);
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
             * Metoda sprawdzająca czy pole nazwiska jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaNazwisko() {
                Pattern p = Pattern.compile("[a-zA-Ząśżźćńłóę]+");
                Matcher m = p.matcher(f_nazwisko.getText());
                if (m.find() && m.group().equals(f_nazwisko.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(AlertType.WARNING);
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
             * Metoda sprawdzająca czy pole telefonu jest poprawne i ma wymagane
             * znaki
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
                    Alert alert = new Alert(AlertType.WARNING);
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

        /**
         * Anonimowa metoda dla przycisku anuluj - otwiera widok moje)cane.fxml
         * gdzie mozna zobaczyc swoje dane
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_dane_parent = FXMLLoader.load(getClass().getResource("moje_dane.fxml"));
                    Scene moje_dane_scene = new Scene(moje_dane_parent);
                    Stage moje_dane_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_dane_stage.setScene(moje_dane_scene);
                    moje_dane_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

}
