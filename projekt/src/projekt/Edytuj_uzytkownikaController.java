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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Edytuj_uzytkownikaController implements Initializable {
    @FXML
    private Button b_zapisz;
    @FXML
    private Button b_wstecz;
    @FXML
    private TextField f_imie;
    @FXML
    private TextField f_nazwisko;
    @FXML
    private TextField f_email;
    @FXML
    private TextField f_telefon;
    @FXML
    private ComboBox<?> c_status;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        UzytkownicyController uzy = new UzytkownicyController();
        
        f_imie.setText(uzy.Imie);
        f_nazwisko.setText(uzy.Nazwisko);
        f_email.setText(uzy.Mail);
        f_telefon.setText(uzy.Telefon);
        
          b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
                
                Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            uzytkownicy_stage.setScene(uzytkownicy_scene);
            uzytkownicy_stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            }
 
    }
});  
        
         b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
                
            System.out.println(f_imie.getText());
         loginController login = new loginController();
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
         PreparedStatement statment = con.prepareStatement("UPDATE uzytkownicy SET imie='"+f_imie.getText()+"', nazwisko='"+f_nazwisko.getText()+"', mail='"+f_email.getText()+"' ,telefon='"+f_telefon.getText()+"' WHERE  idHasla='"+uzy.ID+"'");
         statment.executeUpdate();
         
            
            
                Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            uzytkownicy_stage.setScene(uzytkownicy_scene);
            uzytkownicy_stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(Edytuj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Edytuj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});  
    }    
    
}
