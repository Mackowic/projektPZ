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
import java.util.Properties;
import java.util.Random;
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
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Dodaj_uzytkownikaController implements Initializable {
    @FXML
    private TextField f_imie;
    @FXML
    private TextField f_nazwisko;
    @FXML
    private TextField f_telefon;
    @FXML
    private TextField f_email;
    @FXML
    private ComboBox<?> c_status;
    @FXML
    private Button b_zapisz;
    @FXML
    private Button b_anuluj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
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
         final String email = "zapomniane_haslo@poczta.fm";
       final String pass = "lampalampanos123!"; 
       
       
        try {
            Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.poczta.fm");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");

		 
          Session session = Session.getDefaultInstance(props,new javax.mail.Authenticator() {
                        protected PasswordAuthentication getPasswordAuthentication() {
                            
                            return new PasswordAuthentication(email,pass);
                        }
  
          });
          
          Random generator = new Random();

    int haslo=generator.nextInt();
    
    Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zapomniane_haslo@poczta.fm"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(f_email.getText()));
			message.setSubject("Twoje konto zostalo utworzone");
			message.setText("Twoje pierwsze haslo to -"+haslo);

			Transport.send(message);

			System.out.println("Done");
    hashowanie hash = new hashowanie();
            Class.forName("com.mysql.jdbc.Driver");
Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
PreparedStatement statment = con.prepareStatement("insert into hasla (Haslo) VALUES ('"+hash.crypt(Integer.toString(haslo))+"')");
statment.executeUpdate();
statment = con.prepareStatement("select idHasla from hasla where  Haslo = '"+hash.crypt(Integer.toString(haslo)) +"'");
ResultSet result  = statment.executeQuery();

if(result.next()){
    System.out.println("asdas"+result.getInt(1));
// do testowania
}
statment = con.prepareStatement("insert into uzytkownicy (Imie, Nazwisko, Mail, Telefon, idHasla) VALUES ('"+f_imie.getText()+"', '"+f_nazwisko.getText()+"' , '"+f_email.getText()+"', '"+f_telefon.getText()+"', '"+result.getInt(1)+"' ) ");
statment.executeUpdate();

                
                Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            uzytkownicy_stage.setScene(uzytkownicy_scene);
            uzytkownicy_stage.show();
                
            } catch (IOException ex) {
                Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dodaj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Dodaj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (MessagingException ex) {
            Logger.getLogger(Dodaj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});  
    }    
    
}
