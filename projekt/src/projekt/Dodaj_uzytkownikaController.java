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
    private Button b_zapisz;
    @FXML
    private Button b_anuluj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Anonimowa metoda dla przycisku b_anuluj - cofa do poprzedniwgo widoku
         * Tworzy stage i scene do widoku uzytkownicy.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
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

        /**
         * Anonimowa metoda dla przycisku b_zapisz - powoduje dodanie nowego
         * uzytkownika i wyslanie do niego jego pierwszego hasla Tworzy stage i
         * scene do widoku uzytkownicy.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjątek obsługujący baze danych
         * @exception MessagingException ex - wyjątek oblusujacy biblioteke do
         * wysylania emaili
         */
        b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (walidacjaPola() && walidacjaImie() && walidacjaNazwisko() && walidacjaTelefon() && walidacjaEmail() && walidacjaEmail2()) {
                        final String email = "taskprojectmanager@gmail.com";
                        final String pass = "87654321q";
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

                            Random generator = new Random();

                            int haslo = generator.nextInt();

                            Message message = new MimeMessage(session);
                            message.setFrom(new InternetAddress("taskprojectmanager@gmail.com"));
                            message.setRecipients(Message.RecipientType.TO,
                                    InternetAddress.parse(f_email.getText()));
                            message.setSubject("Twoje konto zostalo utworzone");
                            String message1 = "<img src=\"http://i.imgur.com/h6phkHn.jpg?1\" alt=\"Project Task Menager \"   \" /> ";

                            message1 += "<font size=\"5\" color=\"black\"><br><br>Twoje konto użytkownika programu <b>Project Task Menager</b> zostało właśnie uworzone.<br><br> Twoje pierwsze hasło to: <b>" + haslo + "</b><br><br>Pozdrawiamy,<br><b>Zespół Project Task Menager</b></font>";

                            message.setContent(message1, "text/html;charset=UTF-8");
                            //

                            Transport.send(message);

                            System.out.println("Done");
                            hashowanie hash = new hashowanie();
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("insert into hasla (Haslo) VALUES ('" + hash.crypt(Integer.toString(haslo)) + "')");
                            statment.executeUpdate();
                            statment = con.prepareStatement("select idHasla from hasla where  Haslo = '" + hash.crypt(Integer.toString(haslo)) + "'");
                            ResultSet result = statment.executeQuery();

                            if (result.next()) {
                                System.out.println("asdas" + result.getInt(1));
                            }
                            statment = con.prepareStatement("insert into uzytkownicy (Imie, Nazwisko, Mail, Telefon, idHasla, ranga) VALUES ('" + f_imie.getText() + "', '" + f_nazwisko.getText() + "' , '" + f_email.getText() + "', '" + f_telefon.getText() + "', '" + result.getInt(1) + "','3' ) ");
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
                                alert.setContentText("E-mail istnieje w bazie danych");
                                alert.showAndWait();
                            }
                        } catch (MessagingException ex) {
                            if (ex.getCause() instanceof Exception) {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                DialogPane dialogPane = alert.getDialogPane();
                                dialogPane.getStylesheets().add(
                                        getClass().getResource("/css/myDialogs.css").toExternalForm());
                                dialogPane.getStyleClass().add("myDialog");
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Brak połączenia z internetem");
                                alert.showAndWait();
                            } else {
                                Alert alert = new Alert(Alert.AlertType.WARNING);
                                DialogPane dialogPane = alert.getDialogPane();
                                dialogPane.getStylesheets().add(
                                        getClass().getResource("/css/myDialogs.css").toExternalForm());
                                dialogPane.getStyleClass().add("myDialog");
                                alert.setTitle("Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Nie ma takiego e-maila");
                                alert.showAndWait();
                            }
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Dodaj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Dodaj_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            //Artur
            /**
             * Metoda sprawdzająca czy wymagane pola nie są puste
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaPola() {
                if (f_imie.getText().isEmpty() | f_nazwisko.getText().isEmpty()
                        | f_email.getText().isEmpty() | f_telefon.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Puste pole");
                    alert.setHeaderText(null);
                    alert.setContentText("Wpisz dane do pola");
                    alert.showAndWait();

                    return false;
                }
                return true;
            }

            //Artur
            /**
             * Metoda sprawdzająca czy pole emailu jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaEmail() {
                Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-­Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z­]{2,})$");
                Matcher m = p.matcher(f_email.getText());
                if (m.find() && m.group().equals(f_email.getText())) {
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

            //Artur
            /**
             * Metoda sprawdzająca czy pole imienia jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaImie() {
                Pattern p = Pattern.compile("[a-zA-Ząśżźćńłóę]+");
                Matcher m = p.matcher(f_imie.getText());
                if (m.find() && m.group().equals(f_imie.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Nie poprawne imię");
                    alert.setHeaderText(null);
                    alert.setContentText("Wprowadź poprawne imię bez znaków specjalnych i liter !@#$%^&*(){}[]?/<>: itp.");
                    alert.showAndWait();

                    return false;
                }

            }

            //Artur
            /**
             * Metoda sprawdzająca czy pole nazwiska jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaNazwisko() {
                Pattern p = Pattern.compile("[a-zA-Ząśżźćńłóę]+");
                Matcher m = p.matcher(f_nazwisko.getText());
                if (m.find() && m.group().equals(f_nazwisko.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Nie poprawne Nazwisko");
                    alert.setHeaderText(null);
                    alert.setContentText("Wprowadź poprawne Nazwisko bez znaków specjalnych i liter !@#$%^&*(){}[]?/<>: itp.");
                    alert.showAndWait();

                    return false;
                }

            }

            //Artur
            /**
             * Metoda sprawdzająca czy pole telefonu jest poprawne i ma wymagane
             * znaki
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaTelefon() {
                Pattern p = Pattern.compile("[0-9]+");
                Matcher m = p.matcher(f_telefon.getText());
                if (m.find() && m.group().equals(f_telefon.getText())) {
                    return true;

                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Nie poprawny nr.telefonu");
                    alert.setHeaderText(null);
                    alert.setContentText("Wprowadź poprawny nr.telefonu");
                    alert.showAndWait();

                    return false;
                }

            }

            private boolean walidacjaEmail2() throws ClassNotFoundException, SQLException {

                Class.forName("com.mysql.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                PreparedStatement statment = con.prepareStatement("select Mail from uzytkownicy where Mail = '" + f_email.getText() + "'");
                ResultSet result = statment.executeQuery();

                if (result.next()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("E-mail istnieje w bazie danych!!");
                    alert.showAndWait();
                    return false;

                } else {

                    System.out.println("Wrociłem ");

                    return true;
                }

            }

        });
    }

}
