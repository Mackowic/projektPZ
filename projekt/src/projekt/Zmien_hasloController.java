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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Zmien_hasloController implements Initializable {
    @FXML
    private TextField f_stare_haslo;
    @FXML
    private TextField f_nowe_haslo;
    @FXML
    private TextField f_powtorz_haslo;
    @FXML
    private Button b_zmien;
    @FXML
    private Button b_anuluj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
          b_zmien.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
     try {
            loginController login = new loginController();
             System.out.println(login.ble2);
if(login.ble2.equals(f_stare_haslo.getText()) && f_nowe_haslo.getText().equals(f_powtorz_haslo.getText()))             
{
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
PreparedStatement statment = con.prepareStatement("UPDATE hasla SET Haslo='"+f_nowe_haslo.getText()+"' WHERE  Haslo='"+login.ble2+"'");
statment.executeUpdate(); 

try {
            
            Parent moje_dane_parent = FXMLLoader.load(getClass().getResource("moje_dane.fxml"));
            Scene moje_dane_scene = new Scene(moje_dane_parent);
            Stage moje_dane_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            moje_dane_stage.setScene(moje_dane_scene);
            moje_dane_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }

}else{
JOptionPane.showMessageDialog(null,"Podales złe dane","Error",                  
                  JOptionPane.INFORMATION_MESSAGE); 

} 
         
   } catch (ClassNotFoundException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SQLException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
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
