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
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class TokenController implements Initializable {

    @FXML
    private TextField f_klucz;
    @FXML
    private TextField f_nowe_haslo;
    @FXML
    private TextField f_powtorz_haslo;
    @FXML
    private Button b_anuluj;
    @FXML
    private Button b_ok;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Anonimowa metoda dla przycisku b_anuluj - otwiera widok login.fxml
         * gdzie mozna sie zalogowac do konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent login_parent = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Scene login_scene = new Scene(login_parent);
                    Stage login_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    login_stage.setScene(login_scene);
                    login_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_ok - zatwierdza zmiane hasla na nowe
         * i updatuje dane w bazie, otwiera widok login.fxml gdzie mozna sie
         * zalogowac do konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        b_ok.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    Zapomnialem_haslaController email = new Zapomnialem_haslaController();
                    System.out.println(email.mail);
                    if (walidacjaPola() && walidacjaKlucz2() && walidacjaHasło() && walidacjaHaslo1()) {

                        try {
                            hashowanie hash = new hashowanie();

                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
                            PreparedStatement statment = con.prepareStatement("select idHasla from uzytkownicy where Mail like '" + email.mail + "'");
                            ResultSet result = statment.executeQuery();

                            if (result.next()) {
                                System.out.println(result.getInt(1)); // do testowania
                            }
                            statment = con.prepareStatement("UPDATE hasla SET Haslo='" + hash.crypt(f_nowe_haslo.getText()) + "' WHERE  idHasla='" + Integer.toString(result.getInt(1)) + "'");
                            statment.executeUpdate();

                            statment = con.prepareStatement("delete from tokens where Token like '" + f_klucz.getText() + "'");
                            statment.executeUpdate();

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
                                alert.setContentText("Błedne dane");
                                alert.showAndWait();
                            }
                        }

                        try {

                            Parent login_parent = FXMLLoader.load(getClass().getResource("login.fxml"));
                            Scene login_scene = new Scene(login_parent);
                            Stage login_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            login_stage.setScene(login_scene);
                            login_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(TokenController.class.getName()).log(Level.SEVERE, null, ex);
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
                        alert.setContentText("Błedne dane");
                        alert.showAndWait();
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
                if (f_klucz.getText().isEmpty() | f_nowe_haslo.getText().isEmpty()
                        | f_powtorz_haslo.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Wpisz brakujące pola");
                    alert.showAndWait();

                    return false;
                }
                return true;
            }

            //Artur
            /**
             * Metoda sprawdzająca czy hasla z dwoch wymaganych pol f_nowe_haslo
             * i f_powtorz_haslo sa takie same
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaHasło() {

                if (f_nowe_haslo.getText().equals(f_powtorz_haslo.getText())) {

                    return true;

                } else {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Hasła nie są takie same");
                    alert.showAndWait();

                    return false;
                }
            }
            //Artur

        });

    }

    private boolean walidacjaHaslo1() {
        Pattern p = Pattern.compile("^.{5,30}$");
        Matcher m = p.matcher(f_nowe_haslo.getText());
        if (m.find() && m.group().equals(f_nowe_haslo.getText())) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Nie poprawne haslo");
            alert.setHeaderText(null);
            alert.setContentText("Hasło powinno zawierać od 5 do 30 znaków.");
            alert.showAndWait();

            return false;
        }

    }

    /**
     * Metoda sprawdzająca czy wpisany token jest prawdziwy
     *
     * @author Artur
     * @return zwraca wartosc true lub false
     */
    private boolean walidacjaKlucz2() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
        PreparedStatement statment = con.prepareStatement("select Token from tokens where Token like '" + f_klucz.getText() + "'");
        ResultSet result = statment.executeQuery();

        if (result.next()) {
            System.out.println(result.getString(1) + " "); // do testowania
        }

        if (f_klucz.getText().equals(result.getString(1))) {

            return true;

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Podaj poprawny klucz");
            alert.showAndWait();
            return false;
        }
    }
}
