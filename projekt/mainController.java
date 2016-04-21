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
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 *
 * @author Maciek
 */
public class mainController implements Initializable {


    @FXML
    private Button b_zadania;
    @FXML
    private Button b_projekty;
    @FXML
    private Button b_moje_dane;
    @FXML
    private Button b_uzytkownicy;
    @FXML
    private Button b_wyloguj;
    
    @FXML
    private Label l_name;
    @FXML
    private Button b_ustwaienia_systemowe;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    
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
      b_uzytkownicy.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
            
            Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            uzytkownicy_stage.setScene(uzytkownicy_scene);
            uzytkownicy_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});   

 b_wyloguj.setOnAction(new EventHandler<ActionEvent>() {
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
    }} 
