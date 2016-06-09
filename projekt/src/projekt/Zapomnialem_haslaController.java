/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projekt;

import java.io.IOException;
import java.net.ConnectException;
import javax.mail.PasswordAuthentication;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
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

    public int kod;
    public static String mail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Anonimowa metoda dla przycisku b_anuluj - otwiera widok login.fxml
         * gdzie mozna zmienic haslo
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
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

        /**
         * Anonimowa metoda dla przycisku b_przeslij_nowe_haslo - wysyla na
         * email token resetujacy poprzednie haslo i updatuje odpowiednio baze
         * danych, otwiera widok token.fxml gdzie mozna zmienic haslo
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         * @exception MessagingException ex - wyjątek oblusujacy biblioteke do
         * wysylania emaili
         * @exeption RuntimeException ex - wyjatek wystepujacy kiedy zostanie
         * przekraczony czas odpowiedzi
         * @exeption RuntimeException e - wyjatek wystepujacy kiedy zostanie
         * przekraczony czas odpowiedzi
         */
        b_przeslij_nowe_haslo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                final String email = "taskprojectmanager@gmail.com";
                final String pass = "87654321q";
                if (walidacjaEmail()) {
                    try {

                        Properties props = new Properties();
                        props.put("mail.smtp.host", "smtp.gmail.com");
                        props.put("mail.smtp.socketFactory.port", "465");
                        props.put("mail.smtp.socketFactory.class",
                                "javax.net.ssl.SSLSocketFactory");
                        props.put("mail.smtp.auth", "true");
                        props.put("mail.smtp.port", "465");

                        Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {

                                return new PasswordAuthentication(email, pass);
                            }

                        });
                        if (walidacjaEmail2()) {
                            try {

                                mail = f_podaj_email.getText();
                                Random generator = new Random();

                                kod = generator.nextInt();

                                Class.forName("com.mysql.jdbc.Driver");
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?useUnicode=true&characterEncoding=utf-8", "root", "");
                                PreparedStatement statment = con.prepareStatement("insert into tokens (Token) VALUES ('" + kod + "') ");
                                statment.executeUpdate();

                                Message message = new MimeMessage(session);
                                message.setFrom(new InternetAddress("zapomniane_haslo@poczta.fm"));
                                message.setRecipients(Message.RecipientType.TO,
                                        InternetAddress.parse(f_podaj_email.getText()));
                                //tu pozmieniac dla Klaudii :)
                                message.setSubject("Zmiana hasła");
                                String message1 = "<img src=\"http://i.imgur.com/h6phkHn.jpg?1\" alt=\"Project Task Menager \"   \" /> ";

                                message1 += "<font size=\"5\" color=\"black\"><br><br>Twoj <b>kod</b> do zmiany hasła to: <b>" + kod;
                                message1 += "<font size=\"5\" color=\"black\"></b><br><br>Nie zapomnij zmienić hasła na nowe! <br><br>Jeżeli to nie Ty zmieniałeś hasło - zignoruj tą wiadomość. <br> <br><br>Pozdrawiamy,<br><br><b> Zespół Project Task Menager<br></b></font>";

                                message.setContent(message1, "text/html;charset=UTF-8");

                                //message.setText("Twoj kod do zmiany hasła to: " + kod);
                                Transport.send(message);

                                System.out.println("Done");

                                Parent token_parent = FXMLLoader.load(getClass().getResource("token.fxml"));
                                Scene token_scene = new Scene(token_parent);
                                Stage token_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                                token_stage.setScene(token_scene);
                                token_stage.show();

                            } catch (MessagingException e) {
                                throw new RuntimeException(e);
                            } catch (ClassNotFoundException ex) {
                                Logger.getLogger(Zapomnialem_haslaController.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (SQLException ex) {
                                if (ex.getCause() instanceof Exception) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    DialogPane dialogPane = alert.getDialogPane();
                                    dialogPane.getStylesheets().add(
                                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                                    dialogPane.getStyleClass().add("myDialog");
                                    alert.setTitle("Error");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Błąd połączenia z bazą danych");
                                    alert.showAndWait();
                                } else {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    DialogPane dialogPane = alert.getDialogPane();
                                    dialogPane.getStylesheets().add(
                                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                                    dialogPane.getStyleClass().add("myDialog");
                                    alert.setTitle("Error");
                                    alert.setHeaderText(null);
                                    alert.setContentText("Brak połaczenia z internetem");
                                    alert.showAndWait();
                                }

                            }
                        }

                        //Artur 
                    } catch (IOException ex) {
                        Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (RuntimeException ex) {

                        if (ex.getCause() instanceof ConnectException) {
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Brak połączenia z internetem");
                            alert.showAndWait();
                        }

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Zapomnialem_haslaController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        if (ex.getCause() instanceof Exception) {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Błąd połączenia z bazą danych");
                            alert.showAndWait();
                        } else {
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Błędne dane");
                            alert.showAndWait();
                        }
                    }
                }
            }

            //Artur
            /**
             * Metoda sprawdzająca czy w polu email sa poprawne znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaEmail() {
                Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-­Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z­]{2,})$");
                Matcher m = p.matcher(f_podaj_email.getText());
                if (m.find() && m.group().equals(f_podaj_email.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Nie poprawny e-mail");
                    alert.setHeaderText(null);
                    alert.setContentText("Wprowadź poprawny e-mail");
                    alert.showAndWait();

                    return false;
                }

            }

        });
        //Artur i  Maciek  
        /**
         * Metoda sprawdzająca czy email podany w polu f_podaj_email istnieje w
         * bazie
         *
         * @author Artur
         * @author Maciek
         * @return zwraca wartosc true lub false
         * @throws SQLException ex - wyjatek zajmujący się obsługą bazy danych
         * @throws ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
    }

    private boolean walidacjaEmail2() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
        PreparedStatement statment = con.prepareStatement("select Mail from uzytkownicy where Mail like '" + f_podaj_email.getText() + "'");
        ResultSet result = statment.executeQuery();

        if (result.next()) {
            System.out.println(result.getString(1) + " "); // do testowania
        }

        if (f_podaj_email.getText().equals(result.getString(1))) {

            return true;

        } else {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Podaj poprawny e-mail");
            alert.showAndWait();
            return false;
        }
    }

}
