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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class main_uzyController implements Initializable {

    @FXML
    private Button b_zadania;
    @FXML
    private Button b_projekty;
    @FXML
    private Button b_moje_dane;
    @FXML
    private Button b_wyloguj;
    @FXML
    private Label l_name;
    @FXML
    private Button b_testuj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /**
         * Anonimowa metoda dla przycisku b_zadania - otwiera widok
         * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_zadania.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("Moje_zadania.fxml"));
                    Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                    Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_zadanie_stage.setScene(moje_zadanie_scene);
                    moje_zadanie_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_moje_dane - otwiera widok
         * moje_dane.fxml gdzie mozna dane swojego konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_moje_dane.setOnAction(new EventHandler<ActionEvent>() {
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
        /**
         * Anonimowa metoda dla przycisku b_projekty - otwiera widok
         * Projekty.fxml gdzie mozna zobaczyc i utworzyc swoje projekty
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_projekty.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent projekty_parent = FXMLLoader.load(getClass().getResource("Projekty.fxml"));
                    Scene projekty_scene = new Scene(projekty_parent);
                    Stage projekty_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    projekty_stage.setScene(projekty_scene);
                    projekty_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_wyloguj - otwiera widok login.fxml
         * gdzie mozna sie zalogowac do konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        b_wyloguj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
                    con.close();

                    Parent login_parent = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Scene login_scene = new Scene(login_parent);
                    Stage login_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    login_stage.setScene(login_scene);
                    login_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
     /**
         * Anonimowa metoda dla przycisku b_testuj - otwiera widok
         * testuj.fxml gdzie mozna przetestowac 
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
    b_testuj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("testuj.fxml"));
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

}
