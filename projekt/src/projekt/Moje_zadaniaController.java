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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import static projekt.loginController.ranga2;
import static projekt.loginController.uzytkownikID;

/**
 * FXML Controller class
 *
 * @author Klaudia
 */
public class Moje_zadaniaController implements Initializable {

    @FXML
    private Button b_wiecej;

    public static String Opis, Nazwa, Projekt;
    public static int IDzad = 0;
    @FXML
    private TableView<Tasks2> tv_aktualne;
    @FXML
    private TableColumn<Tasks2, String> tc_projekt;
    @FXML
    private TableColumn<Tasks2, String> tc_nazwa;
    @FXML
    private TableColumn<Tasks2, String> tc_opis;
    @FXML
    private TableView<Tasks2> tv_test;
    @FXML
    private TableColumn<Tasks2, String> tc_projekt2;
    @FXML
    private TableColumn<Tasks2, String> tc_nazwa2;
    @FXML
    private TableColumn<Tasks2, String> tc_opis2;
    @FXML
    private TableView<Tasks2> tv_zakonczone;
    @FXML
    private TableColumn<Tasks2, String> tc_projekt3;
    @FXML
    private TableColumn<Tasks2, String> tc_nazwa3;
    @FXML
    private TableColumn<Tasks2, String> tc_opis3;
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
    @FXML
    private Pane logo;
    @FXML
    private Button b_wyloguj;

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            loginController login = new loginController();
            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
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

            // tutaj trzeba poprawic zapytania sql tylko jak juz beda statusy zadan
            final ObservableList<Tasks2> data = FXCollections.observableArrayList();
            tc_projekt.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("projekt"));
            tc_nazwa.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Nazwa"));
            tc_opis.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Opis"));

            statment = con.prepareStatement("select Nazwa , Opis, Status_zadania, projekt from zadania where idUzytkownika = '" + login.uzytkownikID + "' and Status_zadania='Aktualne'");
            result = statment.executeQuery();

            while (result.next()) {

                data.add(new Tasks2(
                        result.getString("Nazwa"),
                        result.getString("Opis"),
                        result.getString("projekt")
                ));
                tv_aktualne.setItems(data);
            }

            final ObservableList<Tasks2> data1 = FXCollections.observableArrayList();

            tc_projekt2.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("projekt"));
            tc_nazwa2.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Nazwa"));
            tc_opis2.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Opis"));

            statment = con.prepareStatement("select Nazwa , Opis, Status_zadania, projekt from zadania where idUzytkownika = '" + login.uzytkownikID + "'and Status_zadania='FORTEST'");
            result = statment.executeQuery();

            while (result.next()) {

                data1.add(new Tasks2(
                        result.getString("Nazwa"),
                        result.getString("Opis"),
                        result.getString("projekt")
                ));
                tv_test.setItems(data1);
            }

            final ObservableList<Tasks2> data2 = FXCollections.observableArrayList();

            tc_projekt3.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("projekt"));
            tc_nazwa3.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Nazwa"));
            tc_opis3.setCellValueFactory(new PropertyValueFactory<Tasks2, String>("Opis"));

            statment = con.prepareStatement("select Nazwa , Opis, Status_zadania, projekt from zadania where idUzytkownika = '" + login.uzytkownikID + "'and Status_zadania='Zakonczone'");
            result = statment.executeQuery();

            while (result.next()) {

                data2.add(new Tasks2(
                        result.getString("Nazwa"),
                        result.getString("Opis"),
                        result.getString("projekt")
                ));
                tv_zakonczone.setItems(data2);
            }

            /**
             * Anonimowa metoda dla przycisku b_wiecej - otwiera widok
             * ZadanieX.fxml gdzie znajduja sie informacje o zadaniu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_wiecej.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjaTV()) {
                        try {

                            Parent zadaniex_parent = FXMLLoader.load(getClass().getResource("ZadanieX.fxml"));
                            Scene zadaniex_scene = new Scene(zadaniex_parent);
                            Stage zadaniex_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            zadaniex_stage.setScene(zadaniex_scene);
                            zadaniex_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(loginController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });

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
             * Anonimowa metoda dla przycisku b_moje_dane - otwiera widok
             * moje_dane.fxml gdzie mozna dane swojego konta
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * Anonimowa metoda dla przycisku b_uzytkownicy - otwiera widok
             * uzytkownicy.fxml gdzie mozna stworzyc nowego uzytkownika, usunac
             * uzytkownika
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * Anonimowa metoda dla przycisku b_wyloguj - otwiera widok
             * login.fxml gdzie mozna sie zalogowac do konta
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
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
             * Anonimowa metoda dla przycisku b_wiecej - otwiera widok
             * ZadanieX.fxml gdzie znajduja sie informacje o zadaniu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * Anonimowa metoda dla tabeli tv_aktualne - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane do zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_aktualne.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {

                    try {
                        Tasks2 tasks2 = (Tasks2) tv_aktualne.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis, projekt, idZadania from zadania where Nazwa ='" + tasks2.getNazwa() + "'  and Opis ='" + tasks2.getOpis() + "' and projekt ='" + tasks2.getProjekt() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Projekt = result1.getString(3);
                            IDzad = result1.getInt(4);

                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Moje_zadaniaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            /**
             * Anonimowa metoda dla tabeli tv_test - za pomaca fetchingu zbiera
             * dane z kliknietego wiersza tabeli, wysyla zapytanie do bazy i
             * zapisuje dane do zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_test.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {

                    try {
                        Tasks2 tasks2 = (Tasks2) tv_test.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis, projekt, idZadania from zadania where Nazwa ='" + tasks2.getNazwa() + "'  and Opis ='" + tasks2.getOpis() + "' and projekt ='" + tasks2.getProjekt() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Projekt = result1.getString(3);
                            IDzad = result1.getInt(4);

                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Moje_zadaniaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            /**
             * Anonimowa metoda dla przycisku b_testuj - otwiera widok
             * testuj.fxml gdzie mozna przetestowac
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * Anonimowa metoda dla tabeli tv_zakonczone - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane do zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_zakonczone.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {

                    try {
                        Tasks2 tasks2 = (Tasks2) tv_zakonczone.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis, projekt, idZadania from zadania where Nazwa ='" + tasks2.getNazwa() + "'  and Opis ='" + tasks2.getOpis() + "' and projekt ='" + tasks2.getProjekt() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Projekt = result1.getString(3);
                            IDzad = result1.getInt(4);

                        }
                    } catch (SQLException ex) {
                        Logger.getLogger(Moje_zadaniaController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            /**
             * Anonimowa metoda dla przycisku b_lokalne - otwiera widok
             * ZadanieX.fxml gdzie znajduja sie informacje o zadaniu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_globalny.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
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
            });

            // TODO
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Moje_zadaniaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Moje_zadaniaController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean walidacjaTV() {
        if (IDzad == 0) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Wybierz zadanie");
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrano zadania");
            alert.showAndWait();

            return false;
        }
        return true;
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
