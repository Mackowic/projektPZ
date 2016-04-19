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
 * @author Klaudia
 */
public class ProjektyController implements Initializable {
    @FXML
    private Button b_dodaj;
    @FXML
    private Button b_wstecz;
    @FXML
    private Button b_wiecej;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
         b_dodaj.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
            
            Parent dodaj_projekt_parent = FXMLLoader.load(getClass().getResource("Dodaj_projekt.fxml"));
            Scene dodaj_projekt_scene = new Scene(dodaj_projekt_parent);
            Stage dodaj_projekt_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            dodaj_projekt_stage.setScene(dodaj_projekt_scene);
            dodaj_projekt_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});
          b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
            
            Parent main_parent = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene main_projekt_scene = new Scene(main_parent);
            Stage main_projekt_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            main_projekt_stage.setScene(main_projekt_scene);
            main_projekt_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});
       
        // TODO
    }    
    
}
