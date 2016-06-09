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
import java.sql.*;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;
import static projekt.loginController.ranga2;
import static projekt.loginController.uzytkownikID;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Moje_daneController implements Initializable {

    @FXML
    private Label l_imie;
    @FXML
    private Label l_nazwisko;
    @FXML
    private Label l_login;
    @FXML
    private Label l_haslo;
    @FXML
    private Label l_telefon;
    @FXML
    private Button b_zmien_haslo;
    @FXML
    private Button b_popraw_dane;

    @FXML
    private Button b_wyloguj;
    @FXML
    private Pane logo;
    @FXML
    private Pane tlomenu;
    @FXML
    private Button b_zadania;
    @FXML
    private Button b_projekty;
    @FXML
    private Button b_testuj;
    @FXML
    private Button b_moje_dane;
    @FXML
    private Button b_uzytkownicy;
    @FXML
    private Button b_ustawienia_systemowe;
    @FXML
    private Button b_lokalny;
    @FXML
    private Button b_globalny;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            loginController login = new loginController();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
            PreparedStatement statment = con.prepareStatement("select Imie, Nazwisko, Mail, Telefon  from uzytkownicy where Mail='" + login.ble + "'");
            ResultSet result = statment.executeQuery();
            statment = con.prepareStatement("select ranga from uzytkownicy where idUzytkownika = '" + uzytkownikID + "'");
            result = statment.executeQuery();
            if (result.next()) {
                ranga2 = result.getString(1);
                System.out.println(result.getString(1));
            }
            if (result.getString(1).equals("2")) {
                b_testuj.setVisible(false);
            } else {
                b_uzytkownicy.setVisible(false);

            }

            System.out.println(login.ble);

            statment = con.prepareStatement("select Imie, Nazwisko, Mail, Telefon  from uzytkownicy where Mail='" + login.ble + "'");
            result = statment.executeQuery();

            if (result.next()) {
                l_imie.setText(result.getString(1));
                l_nazwisko.setText(result.getString(2));
                l_login.setText(result.getString(3));
                l_telefon.setText(result.getString(4));// do testowania
            }
            l_haslo.setText(gwiazdkowanie());

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Moje_daneController.class.getName()).log(Level.SEVERE, null, ex);
        }

        /**
         * Anonimowa metoda dla przycisku b_zmien_haslo - otwiera widok
         * zmien_haslo.fxml gdzie mozna zmienic haslo
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_zmien_haslo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent zmien_haslo_parent = FXMLLoader.load(getClass().getResource("zmien_haslo.fxml"));
                    Scene zmien_haslo_scene = new Scene(zmien_haslo_parent);
                    Stage zmien_haslo_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    zmien_haslo_stage.setScene(zmien_haslo_scene);
                    zmien_haslo_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_zadania - otwiera widok
         * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_zadania.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("Moje_zadania.fxml"));
                    Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                    Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_zadanie_stage.setScene(moje_zadanie_scene);
                    moje_zadanie_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        /**
         * Anonimowa metoda dla przycisku b_uzytkownicy - otwiera widok
         * uzytkownicy.fxml gdzie mozna stworzyc nowego uzytkownika, usunac
         * uzytkownika
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
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

        /**
         * Anonimowa metoda dla przycisku b_wiecej - otwiera widok ZadanieX.fxml
         * gdzie znajduja sie informacje o zadaniu
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_ustawienia_systemowe.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent zadaniex_parent = FXMLLoader.load(getClass().getResource("ustawienia_systemowe.fxml"));
                    Scene zadaniex_scene = new Scene(zadaniex_parent);
                    Stage zadaniex_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    zadaniex_stage.setScene(zadaniex_scene);
                    zadaniex_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        /**
         * Anonimowa metoda dla przycisku b_moje_dane - otwiera widok
         * moje_dane.fxml gdzie mozna dane swojego konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
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
        /**
         * Anonimowa metoda dla przycisku b_projekty - otwiera widok
         * Projekty.fxml gdzie mozna zobaczyc i utworzyc swoje projekty
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
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
        /**
         * Anonimowa metoda dla przycisku b_wyloguj - otwiera widok login.fxml
         * gdzie mozna sie zalogowac do konta
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        b_wyloguj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Class.forName("com.mysql.jdbc.Driver");
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                    con.close();

                    Parent login_parent = FXMLLoader.load(getClass().getResource("login.fxml"));
                    Scene login_scene = new Scene(login_parent);
                    Stage login_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    login_stage.setScene(login_scene);
                    login_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_testuj - otwiera widok testuj.fxml
         * gdzie mozna przetestowac
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_testuj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("testuj.fxml"));
                    Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                    Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    moje_zadanie_stage.setScene(moje_zadanie_scene);
                    moje_zadanie_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        /**
         * Anonimowa metoda dla przycisku b_popraw_dane - otwiera widok
         * popraw_dane.fxml gdzie mozna poprawic dane
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_popraw_dane.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {

                    Parent popraw_dane_parent = FXMLLoader.load(getClass().getResource("popraw_dane.fxml"));
                    Scene popraw_dane_scene = new Scene(popraw_dane_parent);
                    Stage popraw_dane_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    popraw_dane_stage.setScene(popraw_dane_scene);
                    popraw_dane_stage.show();

                } catch (IOException ex) {
                    Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });

        /**
         * Anonimowa metoda dla przycisku b_lokalne - otwiera widok
         * ZadanieX.fxml gdzie znajduja sie informacje o zadaniu
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_lokalny.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    if (czyprojekt()) {
                        PDF pdf = new PDF();
                        pdf.raport_lokalny();
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Gratulacje!!");
                        alert.setHeaderText(null);
                        alert.setContentText("Utowrzyłeś własnie raport lokalny ktory znajduje sie : " + System.getProperty("user.dir"));
                        alert.showAndWait();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

        });

        /**
         * Anonimowa metoda dla przycisku b_lokalne - otwiera widok
         * ZadanieX.fxml gdzie znajduja sie informacje o zadaniu
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_globalny.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent
                    ) {
                        try {
                            PDF pdf = new PDF();
                            pdf.raport_globalny();
                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Gratulacje!!");
                            alert.setHeaderText(null);
                            alert.setContentText("Utowrzyłeś własnie raport globalny ktory znajduje sie : " + System.getProperty("user.dir"));
                            alert.showAndWait();
                        } catch (IOException ex) {
                            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
        );

    }

    /**
     * Metoda która zamienie haslo na gwiazdki
     *
     * @return haslo - string zawierajacy odpowiednia liczbe gwiazdek
     */
    private String gwiazdkowanie() {
        loginController login = new loginController();
        int liczba = login.ble2.length();
        String haslo = "";
        for (int i = 0; i < liczba; i++) {
            haslo += "*";
        }
        return haslo;
    }

    private boolean czyprojekt() throws ClassNotFoundException, SQLException {
        loginController login = new loginController();
        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
        PreparedStatement statment = con.prepareStatement("select count(*) as liczba from projekty where idUzytkownika = '" + login.uzytkownikID + "'");
        ResultSet result = statment.executeQuery();

        if (result.next()) {

            if (result.getInt("liczba") == 0) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.getStylesheets().add(
                        getClass().getResource("/css/myDialogs.css").toExternalForm());
                dialogPane.getStyleClass().add("myDialog");
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Nie masz zadnych projektow zeby stworzyc raport lokalny");
                alert.showAndWait();
                return false;

            } else {
                System.out.println("wszedlem");
                return true;

            }

        }
        return false;

    }

}
