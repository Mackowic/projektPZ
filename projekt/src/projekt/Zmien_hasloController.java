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
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Zmien_hasloController implements Initializable {

    @FXML
    private PasswordField f_stare_haslo;
    @FXML
    private PasswordField f_nowe_haslo;
    @FXML
    private PasswordField f_powtorz_haslo;
    @FXML
    private Button b_zmien;
    @FXML
    private Button b_anuluj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Anonimowa metoda dla przycisku b_zmien - zatwierdza zmiane hasla na
         * nowe i updatuje zmiany w bazie danych, otwiera widok moje_dane.fxml
         * gdzie mozna zobaczyc swoje dane
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
                loginController login = new loginController();
                System.out.println("Stare haslo: " + login.ble2);
                if (walidacjaPola() && walidacjaHasło() && walidacjaHasłoStare() && walidacjaHaslo1()) {

                    try {

                        {

                            hashowanie hash = new hashowanie();
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
                            PreparedStatement statment = con.prepareStatement("UPDATE hasla SET Haslo='" + hash.crypt(f_nowe_haslo.getText()) + "' WHERE  Haslo='" + hash.crypt(login.ble2) + "'");
                            statment.executeUpdate();
                            login.ble2 = f_nowe_haslo.getText();

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

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
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
                            alert.setContentText("Błąd");
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
                if (f_stare_haslo.getText().isEmpty() | f_nowe_haslo.getText().isEmpty()
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
             * Metoda sprawdzająca czy wymagane pola f_nowe_haslo i
             * f_powtorz_haslo sa takie same
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

            //Artur i Maciek
            /**
             * Metoda sprawdzająca czy haslo podane w polu f_stare_haslo jest
             * prawdziwe
             *
             * @author Artur
             * @author Maciek
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaHasłoStare() {
                loginController login = new loginController();
                System.out.println(login.ble);

                if (f_stare_haslo.getText().equals(login.ble2)) {

                    return true;

                } else {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Stare hasło się nie zgadza");
                    alert.showAndWait();

                    return false;
                }
            }

        });

        /**
         * Anonimowa metoda dla przycisku b_zmien_haslo - otwiera widok
         * moje_dane.fxml gdzie mozna zobaczyc swoje dane
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
