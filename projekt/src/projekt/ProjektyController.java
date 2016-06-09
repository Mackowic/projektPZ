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
public class ProjektyController implements Initializable {

    @FXML
    private Button b_dodaj;

    @FXML
    private Button b_wiecej;
    @FXML
    private TableView<Project> tv_aktualne;
    @FXML
    private TableView<Project> tv_zakonczone;
    @FXML
    private TableColumn<Project, String> tc_nazwa;
    @FXML
    private TableColumn<Project, String> tc_opis;
    @FXML
    private TableColumn<Project, String> tc_poczatek;
    @FXML
    private TableColumn<Project, String> tc_koniec;
    @FXML
    private TableColumn<Project, String> tc_nazwa1;
    @FXML
    private TableColumn<Project, String> tc_opis1;
    @FXML
    private TableColumn<Project, String> tc_poczatek1;
    @FXML
    private TableColumn<Project, String> tc_koniec1;

    public static String Nazwa, Opis, Poczatek, Koniec;
    public static int IDprojektu = 0, IDzadania = 0;
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

            final ObservableList<Project> data = FXCollections.observableArrayList();
            final ObservableList<Project> data1 = FXCollections.observableArrayList();
            tc_nazwa.setCellValueFactory(new PropertyValueFactory<Project, String>("Nazwa"));
            tc_opis.setCellValueFactory(new PropertyValueFactory<Project, String>("Opis"));
            tc_poczatek.setCellValueFactory(new PropertyValueFactory<Project, String>("Poczatek"));
            tc_koniec.setCellValueFactory(new PropertyValueFactory<Project, String>("Koniec"));

            tc_nazwa1.setCellValueFactory(new PropertyValueFactory<Project, String>("Nazwa"));
            tc_opis1.setCellValueFactory(new PropertyValueFactory<Project, String>("Opis"));
            tc_poczatek1.setCellValueFactory(new PropertyValueFactory<Project, String>("Poczatek"));
            tc_koniec1.setCellValueFactory(new PropertyValueFactory<Project, String>("Koniec"));

            statment = con.prepareStatement("select ranga from uzytkownicy where idUzytkownika = '" + uzytkownikID + "'");
            result = statment.executeQuery();
            if (result.next()) {
                System.out.println(result.getString(1));
            }
            if (result.getString(1).equals("2")) {

                statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty where Koniec > CURRENT_DATE");
                result = statment.executeQuery();

                while (result.next()) {
                    data.add(new Project(
                            result.getString("Nazwa"),
                            result.getString("Opis"),
                            result.getString("Poczatek"),
                            result.getString("Koniec")
                    ));
                    tv_aktualne.setItems(data);
                }

                statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty where Koniec < CURRENT_DATE");
                result = statment.executeQuery();

                while (result.next()) {
                    data1.add(new Project(
                            result.getString("Nazwa"),
                            result.getString("Opis"),
                            result.getString("Poczatek"),
                            result.getString("Koniec")
                    ));
                    tv_zakonczone.setItems(data1);
                }

            } else {

                statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty where Koniec > CURRENT_DATE and INSTR(ludzie, '" + uzytkownikID + "')");
                result = statment.executeQuery();

                while (result.next()) {
                    data.add(new Project(
                            result.getString("Nazwa"),
                            result.getString("Opis"),
                            result.getString("Poczatek"),
                            result.getString("Koniec")
                    ));
                    tv_aktualne.setItems(data);
                }

                statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty where Koniec < CURRENT_DATE and INSTR(ludzie, '" + uzytkownikID + "')");
                result = statment.executeQuery();

                while (result.next()) {
                    data1.add(new Project(
                            result.getString("Nazwa"),
                            result.getString("Opis"),
                            result.getString("Poczatek"),
                            result.getString("Koniec")
                    ));
                    tv_zakonczone.setItems(data1);
                }

            }
            /**
             * Anonimowa metoda dla tabeli tv_aktualne - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_aktualne.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        Project project = (Project) tv_aktualne.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec, idProjektu, idZadania from projekty where Nazwa ='" + project.getNazwa() + "'  and Opis ='" + project.getOpis() + "' and Poczatek ='" + project.getPoczatek() + "' and Koniec = '" + project.getKoniec() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Poczatek = result1.getString(3);
                            Koniec = result1.getString(4);
                            IDprojektu = result1.getInt(5);
                            IDzadania = result1.getInt(6);

                        }

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
                            alert.setContentText("błąd");
                            alert.showAndWait();
                        }
                    }
                }
            });
            /**
             * Anonimowa metoda dla tabeli tv_zakonczone - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_zakonczone.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        Project project = (Project) tv_zakonczone.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec, idProjektu, idZadania from projekty where Nazwa ='" + project.getNazwa() + "'  and Opis ='" + project.getOpis() + "' and Poczatek ='" + project.getPoczatek() + "' and Koniec = '" + project.getKoniec() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Poczatek = result1.getString(3);
                            Koniec = result1.getString(4);
                            IDprojektu = result1.getInt(5);
                            IDzadania = result1.getInt(6);

                        }

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
                            alert.setContentText("błąd");
                            alert.showAndWait();
                        }
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
             * Anonimowa metoda dla przycisku b_zadania - otwiera widok
             * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
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
             * Anonimowa metoda dla przycisku b_dodaj - otwiera widok
             * Dodaj_projekt.fxml gdzie mozna utowrzyc nowy projekt
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */

            b_dodaj.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent dodaj_projekt_parent = FXMLLoader.load(getClass().getResource("Dodaj_projekt.fxml"));
                        Scene dodaj_projekt_scene = new Scene(dodaj_projekt_parent);
                        Stage dodaj_projekt_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        dodaj_projekt_stage.setScene(dodaj_projekt_scene);
                        dodaj_projekt_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            /**
             * Anonimowa metoda dla przycisku b_zmien_haslo - otwiera widok
             * main.fxml czyli menu glowne
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             */

            // TODO
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
        }
        /**
         * Anonimowa metoda dla przycisku b_wiecej - otwiera widok
         * Projekt_X.fxml gdzie mozna zobaczyc dane odnosnie danego projektu
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_wiecej.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (walidacjaTV()) {
                    try {

                        Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                        Scene projektx_scene = new Scene(projektx_parent);
                        Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        projektx_stage.setScene(projektx_scene);
                        projektx_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                    }
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

    }

    private boolean walidacjaTV() {
        if (IDzadania == 0 | IDprojektu == 0) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Wybierz projekt");
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrano projektu");
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
