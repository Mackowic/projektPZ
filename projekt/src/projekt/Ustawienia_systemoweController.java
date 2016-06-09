/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.io.IOException;
import java.net.URL;
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
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Ustawienia_systemoweController implements Initializable {
    @FXML
    private Button b_wstecz;

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
        b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
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
        
        
    }

}
