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
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
    private TextField f_email;
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
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
PreparedStatement statment = con.prepareStatement("select Imie, Nazwisko, Mail, Telefon  from uzytkownicy where Mail='"+login.ble+"'");
ResultSet result = statment.executeQuery(); 

if(result.next()){
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
        
      b_zmien.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        
        
     try {
         
         loginController login = new loginController();
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
         PreparedStatement statment = con.prepareStatement("UPDATE uzytkownicy SET imie='"+f_imie.getText()+"', nazwisko='"+f_nazwisko.getText()+"', mail='"+f_login.getText()+"' ,telefon='"+f_telefon.getText()+"' WHERE  idHasla='"+login.id+"'");
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
            Logger.getLogger(Popraw_daneController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});
          
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
