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
import java.util.ArrayList;
import java.util.List;
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
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;
import static projekt.loginController.ranga2;
import static projekt.loginController.uzytkownikID;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Projekt_XController implements Initializable {

    @FXML
    private Button b_dodaj_zadanie;
    @FXML
    private Button b_dodaj_uzytkownika;
    @FXML
    private Button b_edytuj;
    @FXML
    private Button b_wstecz;
    @FXML
    private TableView<Tasks> tv_zadania;
    @FXML
    private TableColumn<Tasks, String> tc_nazwa;
    @FXML
    private TableColumn<Tasks, String> tc_opis;
    @FXML
    private TableColumn<Tasks, String> tc_status;
    @FXML
    private TableView<User> tv_uzytkownicy;
    @FXML
    private TableColumn<User, String> tc_nazwisko;
    @FXML
    private TableColumn<User, String> tc_imie;
    String string;
    @FXML
    private Button b_usun;

    public static String Imie, Nazwisko, Mail, Telefon, Nazwa, Opis, Status_zadania, Projekt;
    public static int ID, IDzadan;
    @FXML
    private TabPane tabpane;
    @FXML
    private Button b_edytuj1;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ProjektyController projekt = new ProjektyController();
        System.out.println("id projektu: " + projekt.IDprojektu);
        try {
            final ObservableList<User> data = FXCollections.observableArrayList();
            final ObservableList<Tasks> data1 = FXCollections.observableArrayList();

            tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            tc_nazwa.setCellValueFactory(new PropertyValueFactory<Tasks, String>("Nazwa"));
            tc_opis.setCellValueFactory(new PropertyValueFactory<Tasks, String>("Opis"));
            tc_status.setCellValueFactory(new PropertyValueFactory<Tasks, String>("Status"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment;
            ResultSet result;
            statment = con.prepareStatement("select idUzytkownika from projekty where idUzytkownika = '" + uzytkownikID + "' and idProjektu = '" + projekt.IDprojektu + "'");
            result = statment.executeQuery();
            if (result.next() || ranga2.equals("2")) {
            } else {
                b_dodaj_zadanie.setVisible(false);
                b_dodaj_uzytkownika.setVisible(false);
                b_edytuj.setVisible(false);
                b_usun.setVisible(false);
                b_edytuj1.setVisible(false);

            }

            statment = con.prepareStatement("select ludzie from projekty where idProjektu = '" + projekt.IDprojektu + "'");
            result = statment.executeQuery();
            if (result.next()) {
                string = result.getString(1);
            }
            System.out.println("nasz string ludzi : " + string);
            String wynik1[] = string.split(",");
            for (int i = 0; i < wynik1.length; i++) {
                //testowanie
                System.out.println("pokazywanie suba : " + wynik1[i]);

                //
                statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy where idUzytkownika = '" + wynik1[i] + "'");
                result = statment.executeQuery();

                while (result.next()) {
                    if (result.getString("Nazwisko").equals("Brak uzytkownika")) {

                        continue;
                    } else {
                        data.add(new User(
                                result.getString("Nazwisko"),
                                result.getString("Imie")
                        ));
                        tv_uzytkownicy.setItems(data);
                    }
                }
            }
            System.out.println("nazwa projektu " + projekt.Nazwa);
            statment = con.prepareStatement("select Nazwa , Opis, Status_zadania  from zadania where projekt = '" + projekt.Nazwa + "'");
            result = statment.executeQuery();

            while (result.next()) {
                data1.add(new Tasks(
                        result.getString("Nazwa"),
                        result.getString("Opis"),
                        result.getString("Status_zadania")
                ));
                tv_zadania.setItems(data1);
            }

            /**
             * Anonimowa metoda dla przycisku b_dodaj_zadanie - otwiera widok
             * Dodaj_zadanie.fxml gdzie mozna dodac nowe zadanie do projektu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_dodaj_zadanie.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent Dodaj_zadanie_parent = FXMLLoader.load(getClass().getResource("Dodaj_zadanie.fxml"));
                        Scene Dodaj_zadanie_scene = new Scene(Dodaj_zadanie_parent);
                        Stage Dodaj_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Dodaj_zadanie_stage.setScene(Dodaj_zadanie_scene);
                        Dodaj_zadanie_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            /**
             * Anonimowa metoda dla przycisku b_dodaj_uzytkownika - otwiera
             * widok Dolacz_uzytkownika.fxml gdzie mozna dodac nowego
             * uzytkownika do projektu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_dodaj_uzytkownika.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent dolacz_uzytkownia_parent = FXMLLoader.load(getClass().getResource("Dolacz_uzytkownika.fxml"));
                        Scene dolacz_uzytkownia_scene = new Scene(dolacz_uzytkownia_parent);
                        Stage dolacz_uzytkownia_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        dolacz_uzytkownia_stage.setScene(dolacz_uzytkownia_scene);
                        dolacz_uzytkownia_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            /**
             * Anonimowa metoda dla przycisku b_edytuj - otwiera widok
             * Edytuj_projekt1.fxml gdzie mozna edytowac dane projektu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_edytuj.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent edytuj_projekt1_parent = FXMLLoader.load(getClass().getResource("Edytuj_projekt1.fxml"));
                        Scene edytuj_projekt1_scene = new Scene(edytuj_projekt1_parent);
                        Stage edytuj_projekt1_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        edytuj_projekt1_stage.setScene(edytuj_projekt1_scene);
                        edytuj_projekt1_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            /**
             * Anonimowa metoda dla przycisku b_wstecz - otwiera widok
             * Projekty.fxml gdzie mozna znajduje sie lista wszystkich projektow
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent projekty_parent = FXMLLoader.load(getClass().getResource("Projekty.fxml"));
                        Scene projekty_scene = new Scene(projekty_parent);
                        Stage projekty_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        projekty_stage.setScene(projekty_scene);
                        projekty_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            /**
             * Anonimowa metoda dla tabeli tv_uzytkownicy - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane do zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_uzytkownicy.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_uzytkownicy.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Imie , Nazwisko, Mail, Telefon, idUzytkownika from uzytkownicy where Imie ='" + user.getImie() + "'  and Nazwisko ='" + user.getNazwisko() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Imie = result1.getString(1);
                            Nazwisko = result1.getString(2);
                            Mail = result1.getString(3);
                            Telefon = result1.getString(4);
                            ID = result1.getInt(5);

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
                            alert.setContentText("Błąd");
                            alert.showAndWait();
                        }
                    }
                }
            });

            /**
             * Anonimowa metoda dla tabeli tv_zadania - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_zadania.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        Tasks user = (Tasks) tv_zadania.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select Nazwa , Opis,Status_zadania, idZadania,projekt from zadania where Nazwa ='" + user.getNazwa() + "'  and Opis ='" + user.getOpis() + "'and Status_zadania = '" + user.getStatus() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            Nazwa = result1.getString(1);
                            Opis = result1.getString(2);
                            Status_zadania = result1.getString(3);
                            IDzadan = result1.getInt(4);
                            Projekt = result1.getString(5);

                        }
                        System.out.println(IDzadan);
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
                            alert.setContentText("Błąd");
                            alert.showAndWait();
                        }
                    }
                }
            });

            /**
             * Anonimowa metoda dla przycisku b_usun - usuwa zadanie lub
             * uzytkownika z projektu i otwiera widok Projekt_X.fxml
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             */
            b_usun.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjayesno()) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
                            PreparedStatement statment;
                            if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Użytkownicy")) {
                                System.out.println("nasz string ludzi : " + string);
                                List lista1 = new ArrayList();
                                String wynik1[] = string.split(",");
                                String abc = Integer.toString(ID);
                                for (int i = 0; i < wynik1.length; i++) {

                                    lista1.add(wynik1[i]);

                                }
                                lista1.remove(abc);
                                String ludziki = "";
                                for (int i = 0; i < lista1.size(); i++) {
                                    if (i == 0) {
                                        ludziki = ludziki + lista1.get(i);
                                    } else {
                                        ludziki = ludziki + "," + lista1.get(i);
                                    }

                                }

                                ProjektyController pro = new ProjektyController();
                                statment = con.prepareStatement("update projekty set ludzie ='" + ludziki + "' where idProjektu = '" + pro.IDprojektu + "'");
                                statment.executeUpdate();
                            } else if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Zadania")) {
                                statment = con.prepareStatement("delete from zadania where idZadania = '" + IDzadan + "'");
                                statment.executeUpdate();

                            }

                            Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                            Scene projektx_scene = new Scene(projektx_parent);
                            Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            projektx_stage.setScene(projektx_scene);
                            projektx_stage.show();

                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });

            /**
             * Anonimowa metoda dla przycisku b_edytuj - otwiera widok
             * ZadanieX2.fxml gdzie mozna dodac nowe zadanie do projektu
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_edytuj1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent Dodaj_zadanie_parent = FXMLLoader.load(getClass().getResource("ZadanieX2.fxml"));
                        Scene Dodaj_zadanie_scene = new Scene(Dodaj_zadanie_parent);
                        Stage Dodaj_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        Dodaj_zadanie_stage.setScene(Dodaj_zadanie_scene);
                        Dodaj_zadanie_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean walidacjayesno() {
        int dialogButton = JOptionPane.YES_NO_OPTION;

        String[] options = new String[2];
        options[0] = new String("Tak");
        options[1] = new String("Nie");
        int dialogResult = JOptionPane.showOptionDialog(null, "Czy chcesz usunąć zadanie " + Nazwa + " !?", "Opuść zadanie", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        if (dialogResult == 0) {
            return true;
        } else {
            return false;
        }

    }

}
