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
public class Moje_zadaniaController implements Initializable {
     @FXML
    private Button b_wstecz;
    @FXML
    private Button b_wiecej;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
         b_wiecej.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
                
                Parent zadaniex_parent = FXMLLoader.load(getClass().getResource("ZadanieX.fxml"));
                Scene zadaniex_scene = new Scene(zadaniex_parent);
                Stage zadaniex_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                zadaniex_stage.setScene(zadaniex_scene);
                zadaniex_stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            }
 
    }
});  
        b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
                
                Parent main_parent = FXMLLoader.load(getClass().getResource("main.fxml"));
                Scene main_scene = new Scene(main_parent);
                Stage main_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                main_stage.setScene(main_scene);
                main_stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            }
 
    }
}); 
        // TODO
    }    
    
}
