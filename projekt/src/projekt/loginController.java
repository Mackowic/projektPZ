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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Maciek
 */
public class loginController implements Initializable {

    @FXML
    private TextField textfield_login;

    @FXML
    private PasswordField pwfield_haslo;

    @FXML
    private Button button_zaloguj;

    @FXML
    private Label l_error;

    @FXML
    private Hyperlink zapomnialem_hasla;

    public String login, haslo, login2, haslo2;
    public static String ble, ble2, id, uzytkownikID;
    public static String ranga2;
    @FXML
    private Label logo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Anonimowa metoda dla pola pwfield_haslo - po nacisnieciu przycisku
         * enter nedac na polu hasla ,laczy sie z baza i loguje do odpowiedniego
         * konta, otwiera widok main.fxml lub main_uzy.fxml czyli menu glowne
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        pwfield_haslo.setOnKeyPressed(new EventHandler<KeyEvent>() {

            public void handle(KeyEvent e) {
                if (e.getCode() == KeyCode.ENTER) {
                    if (walidacjaPola()) {
                        try {
                            login2 = textfield_login.getText();
                            haslo2 = pwfield_haslo.getText();
                            ble = login2;
                            ble2 = haslo2;

                            System.out.println(login2); //do testowania        

                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("select Mail , idHasla, idUzytkownika from uzytkownicy where Mail='" + login2 + "'");
                            ResultSet result = statment.executeQuery();

                            if (result.next()) {
                                System.out.println(result.getString(1) + " " + result.getInt(2)); // do testowania
                            }
                            id = result.getString(2);
                            uzytkownikID = result.getString(3);
                            statment = con.prepareStatement("select Haslo from hasla where idHasla = '" + result.getInt(2) + "'");
                            result = statment.executeQuery();

                            if (result.next()) {
                                System.out.println(result.getString(1)); // do testowania
                            }

                            hashowanie hash = new hashowanie();

                            if (hash.crypt(haslo2).equals(result.getString(1))) {

                                Parent Moje_zadania_parent = FXMLLoader.load(getClass().getResource("Moje_zadania.fxml"));
                                Scene Moje_zadania_scene = new Scene(Moje_zadania_parent);
                                Stage Moje_zadania_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
                                Moje_zadania_stage.setScene(Moje_zadania_scene);
                                Moje_zadania_stage.show();

                            } else {
                                l_error.setText("Złe dane");
                            }

                        } catch (ClassNotFoundException ex) {
                            System.err.println("Błąd odczytu classy");

                        } //Artur
                        catch (SQLException ex) {
                            System.out.println(ex.getMessage());

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
                                alert.setContentText("Błęde dane");
                                alert.showAndWait();

                            }

                        } catch (IOException ex) {
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

            /**
             * Metoda sprawdzająca czy wymagane pola nie są puste
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaPola() {
                if (textfield_login.getText().isEmpty() | pwfield_haslo.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");
                    alert.setTitle("Puste pole");
                    alert.setHeaderText(null);
                    alert.setContentText("Wpisz dane do pola login i hasło");
                    alert.showAndWait();

                    return false;
                }
                return true;
            }

        });
        /**
         * Anonimowa metoda dla przycisku button_zaloguj - laczy sie z baza i
         * loguje do odpowiedniego konta, otwiera widok main.fxml lub
         * main_uzy.fxml czyli menu glowne
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        button_zaloguj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (walidacjaPola() && walidacjaEmail()) {
                    try {
                        login2 = textfield_login.getText();
                        haslo2 = pwfield_haslo.getText();
                        ble = login2;
                        ble2 = haslo2;

                        System.out.println(login2); //do testowania        

                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                        PreparedStatement statment = con.prepareStatement("select Mail , idHasla, idUzytkownika from uzytkownicy where Mail='" + login2 + "'");
                        ResultSet result = statment.executeQuery();

                        if (result.next()) {
                            System.out.println(result.getString(1) + " " + result.getInt(2)); // do testowania
                        }
                        id = result.getString(2);
                        uzytkownikID = result.getString(3);
                        statment = con.prepareStatement("select Haslo from hasla where idHasla = '" + result.getInt(2) + "'");
                        result = statment.executeQuery();

                        if (result.next()) {
                            System.out.println(result.getString(1)); // do testowania
                        }

                        hashowanie hash = new hashowanie();

                        if (hash.crypt(haslo2).equals(result.getString(1))) {

//                             
                            Parent Moje_zadania_parent = FXMLLoader.load(getClass().getResource("Moje_zadania.fxml"));
                            Scene Moje_zadania_scene = new Scene(Moje_zadania_parent);
                            Stage Moje_zadania_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            Moje_zadania_stage.setScene(Moje_zadania_scene);
                            Moje_zadania_stage.show();

                        } else {
                            l_error.setText("Złe dane");
                        }

                    } catch (ClassNotFoundException ex) {
                        System.err.println("Błąd odczytu classy");

                    } //Artur
                    catch (SQLException ex) {
                        System.out.println(ex.getMessage());

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
                            alert.setContentText("Błęde dane");
                            alert.showAndWait();
                        }

                    } catch (IOException ex) {
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

            //Artur
            /**
             * Metoda sprawdzająca czy wymagane pola nie są puste
             *
             * @author Artur
             * @return zwraca wartosc true lub false
             */
            private boolean walidacjaPola() {
                if (textfield_login.getText().isEmpty() | pwfield_haslo.getText().isEmpty()) {

                    Alert alert = new Alert(Alert.AlertType.WARNING);

                    DialogPane dialogPane = alert.getDialogPane();
                    dialogPane.getStylesheets().add(
                            getClass().getResource("/css/myDialogs.css").toExternalForm());
                    dialogPane.getStyleClass().add("myDialog");

                    alert.setTitle("Puste pole");
                    alert.setHeaderText(null);
                    alert.setContentText("Wypełnij pola!!");
                    alert.showAndWait();

                    return false;
                }
                return true;
            }

            private boolean walidacjaEmail() {
                Pattern p = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-­Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z­]{2,})$");
                Matcher m = p.matcher(textfield_login.getText());
                if (m.find() && m.group().equals(textfield_login.getText())) {
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
        /**
         * Anonimowa metoda dla hiperlacza zapomnialem_hasla - otwiera widok
         * zapomnialem_hasla.fxml gdzie mozna zmienic haslo na nowe
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        zapomnialem_hasla.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    Parent zapomnialem_parent = FXMLLoader.load(getClass().getResource("zapomnialem_hasla.fxml"));
                    Scene zapomnialem_scene = new Scene(zapomnialem_parent);
                    Stage zapomnialem_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    zapomnialem_stage.setScene(zapomnialem_scene);
                    zapomnialem_stage.show();
                } catch (IOException ex) {
                    Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

    }
}
