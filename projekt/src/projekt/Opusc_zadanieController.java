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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Opusc_zadanieController implements Initializable {

    @FXML
    private Button b_wstecz;
    @FXML
    private Button b_zapisz;
    @FXML
    private TextArea a_komentarz;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Anonimowa metoda dla przycisku b_zapisz - pozwala uzytkownikowy na
         * opuszczenie zadania po wpisaniu komentarz/powodu, otwiera widok
         * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */

        b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (walidacjaPola()) {
                    try {
                        Moje_zadaniaController moje = new Moje_zadaniaController();
                        loginController login = new loginController();
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con;
                        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                        PreparedStatement statment;
                        statment = con.prepareStatement("Update zadania set idUzytkownika ='2' where idUzytkownika = '" + login.uzytkownikID + "' and projekt ='" + moje.Projekt + "' and Nazwa ='" + moje.Nazwa + "' and idZadania = '" + moje.IDzad + "'");
                        statment.executeUpdate();
                        statment = con.prepareStatement("insert into komentarze (Opis, idUzytkownika, idZadania) values ('" + a_komentarz.getText() + "','" + login.uzytkownikID + "','" + moje.IDzad + "')");
                        statment.executeUpdate();

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Info");
                        alert.setHeaderText(null);
                        alert.setContentText("Opusciles zadanie");
                        alert.showAndWait();

                        Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("Moje_zadania.fxml"));
                        Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                        Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        moje_zadanie_stage.setScene(moje_zadanie_scene);
                        moje_zadanie_stage.show();

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }

            }
        });

        /**
         * Anonimowa metoda dla przycisku b_wstecz - otwiera widok ZadanieX.fxml
         * gdzie mozna zobaczyc informacje o zadaniu
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("ZadanieX.fxml"));
                    Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                    Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_zadanie_stage.setScene(moje_zadanie_scene);
                    moje_zadanie_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }

    private boolean walidacjaPola() {
        if (a_komentarz.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Pole komentarz nie może być puste");
            alert.showAndWait();

            return false;
        }
        return true;
    }

}
