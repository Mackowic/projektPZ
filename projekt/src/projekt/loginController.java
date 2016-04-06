/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projekt;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.*;

/**
 *
 * @author Maciek
 */
public class loginController  implements Initializable {
    
     @FXML
    private TextField textfield_login;

    @FXML
    private PasswordField pwfield_haslo;

    @FXML
    private Button button_zaloguj;
    
    @FXML
    private Label l_error;
    
    public  String login, haslo,login2,haslo2;
    public static String ble,ble2;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    

        
     
        button_zaloguj.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
        
try { 
         login2=textfield_login.getText();
         haslo2=pwfield_haslo.getText();
         ble=login2;
         ble2=haslo2;

System.out.println(login2); //do testowania        
         
Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
PreparedStatement statment = con.prepareStatement("select Mail , idHasla from uzytkownicy where Mail='"+login2+"'");
ResultSet result = statment.executeQuery(); 

if(result.next()){
System.out.println(result.getString(1)+" "+result.getInt(2)); // do testowania
}

statment = con.prepareStatement("select Haslo from hasla where idHasla = '"+result.getInt(2)+"'");
result = statment.executeQuery();  

if(result.next()){
System.out.println(result.getString(1)); // do testowania
}

hashowanie hash = new hashowanie();

 if(hash.crypt(haslo2).equals(result.getString(1))){
            
           
                
                Parent main_parent = FXMLLoader.load(getClass().getResource("main.fxml"));
                Scene main_scene = new Scene(main_parent);
                Stage main_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                main_stage.setScene(main_scene);
                main_stage.show();
                
            
             
       } 
       else{
       l_error.setText("Złe dane");
       }
    
  
         
   } catch (ClassNotFoundException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
      } catch (SQLException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);} 
                catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            }
}
        
       
        
      
});


    } 

    
}
