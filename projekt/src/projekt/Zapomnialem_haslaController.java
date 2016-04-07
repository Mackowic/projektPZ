/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projekt;

import java.io.IOException;
import javax.mail.PasswordAuthentication;
import java.net.URL;
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
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Zapomnialem_haslaController implements Initializable {
    @FXML
    private TextField f_podaj_email;
    @FXML
    private Button b_anuluj;
    @FXML
    private Button b_przeslij_nowe_haslo;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
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
        
b_przeslij_nowe_haslo.setOnAction(new EventHandler<ActionEvent>() {
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

		try {

int kod;
Random generator = new Random();

   kod=generator.nextInt();

                    
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress("zapomniane_haslo@poczta.fm"));
			message.setRecipients(Message.RecipientType.TO,
					InternetAddress.parse(f_podaj_email.getText()));
			message.setSubject("Zmiana hasła");
			message.setText("Twoj kod do zmiany hasła to"+kod);

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

            
            
            Parent token_parent = FXMLLoader.load(getClass().getResource("token.fxml"));
            Scene token_scene = new Scene(token_parent);
            Stage token_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            token_stage.setScene(token_scene);
            token_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});       
        
    }    
    
}
