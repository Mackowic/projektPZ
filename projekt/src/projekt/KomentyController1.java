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
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class KomentyController1 implements Initializable {

    @FXML
    private Button b_wstecz;
    @FXML
    private Button b_zatwierdz;
    @FXML
    private TextArea a_komentarz;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Anonimowa metoda dla przycisku b_wstecz - otwiera widok ZadanieX.fxml
         * gdzie mozna zobaczyc swoje zadanie
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

        /**
         * Anonimowa metoda dla przycisku b_zatwierdz - dodaje komentarz do
         * zadania i otwiera widok ZadanieX.fxml gdzie mozna zobaczyc swoje
         * zadanie
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_zatwierdz.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Moje_zadaniaController moje = new Moje_zadaniaController();
                    loginController login = new loginController();
                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con;
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
                    PreparedStatement statment;
                    statment = con.prepareStatement("insert into komentarze (Opis, idUzytkownika, idZadania) values ('" + a_komentarz.getText() + "','" + login.uzytkownikID + "','" + moje.IDzad + "')");
                    statment.executeUpdate();

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("ZadanieX.fxml"));
                    Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                    Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_zadanie_stage.setScene(moje_zadanie_scene);
                    moje_zadanie_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(KomentyController1.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(KomentyController1.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
    }

}
