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
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import java.util.function.Predicate;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Klaudia
 */
public class UzytkownicyController implements Initializable {

    @FXML
    private Button b_dodaj;
    @FXML
    private Button b_edytuj;
    @FXML
    private Button b_usun;
    @FXML
    private Button b_wstecz;
    @FXML
    private TableView<User> tv_wszyscy;
    @FXML
    private TableView<User> tv_vip;
    @FXML
    private TableColumn<User, String> tc_nazwisko;
    @FXML
    private TableColumn<User, String> tc_imie;
    @FXML
    private TableColumn<User, String> tc_nazwisko1;
    @FXML
    private TableColumn<User, String> tc_imie1;
    @FXML
    private TableColumn<User, String> tc_status;
    @FXML
    private TableColumn<User, String> tc_projekty;
    @FXML
    private TableView<User> tv_pracowincy;
    @FXML
    private TableColumn<User, String> tc_nazwisko2;
    @FXML
    private TableColumn<User, String> tc_imie2;
    @FXML
    private TableColumn<User, String> tc_projekty2;

    public static String Imie, Nazwisko, Mail, Telefon;
    public static int ID = 0;
    @FXML
    private TextField f_szukaj;
    @FXML
    private Tab Wszyscy;
    @FXML
    private Tab VIP;
    @FXML
    private Tab Pracownicy;
    @FXML
    private TabPane tabpane;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            final ObservableList<User> data = FXCollections.observableArrayList();
            final ObservableList<User> data1 = FXCollections.observableArrayList();
            final ObservableList<User> data2 = FXCollections.observableArrayList();
            tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            tc_nazwisko1.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie1.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            tc_nazwisko2.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie2.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy");
            ResultSet result = statment.executeQuery();

            while (result.next()) {
                if (result.getString("Nazwisko").equals("Brak uzytkownika") || result.getString("Nazwisko").equals("Admin")) {
                    continue;
                } else {
                    data.add(new User(
                            result.getString("Nazwisko"),
                            result.getString("Imie")
                    ));
                    tv_wszyscy.setItems(data);
                }
            }

            //test
            statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy where ranga = '1'");
            result = statment.executeQuery();

            while (result.next()) {
                data1.add(new User(
                        result.getString("Nazwisko"),
                        result.getString("Imie")
                ));
                tv_vip.setItems(data1);
            }

            statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy where ranga = '3'");
            result = statment.executeQuery();

            while (result.next()) {
                data2.add(new User(
                        result.getString("Nazwisko"),
                        result.getString("Imie")
                ));
                tv_pracowincy.setItems(data2);
            }
            /**
             * Anonimowa metoda dla tabeli tv_wszyscy - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_wszyscy.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_wszyscy.getSelectionModel().getSelectedItem();
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
             * Anonimowa metoda dla tabeli tv_vip - za pomaca fetchingu zbiera
             * dane z kliknietego wiersza tabeli, wysyla zapytanie do bazy i
             * zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_vip.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_vip.getSelectionModel().getSelectedItem();
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
             * Anonimowa metoda dla tabeli tv_pracownicy - za pomaca fetchingu
             * zbiera dane z kliknietego wiersza tabeli, wysyla zapytanie do
             * bazy i zapisuje dane od zmiennych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_pracowincy.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_pracowincy.getSelectionModel().getSelectedItem();
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
                            alert.setContentText("błąd");
                            alert.showAndWait();
                        }
                    }
                }
            });

            FilteredList<User> filteredData = new FilteredList<>(data, e -> true);
            FilteredList<User> filteredData1 = new FilteredList<>(data1, e -> true);
            FilteredList<User> filteredData2 = new FilteredList<>(data2, e -> true);
            /**
             * Metoda lambda dla pola f_szukaj - ktora filtruje wyniki w
             * tabelach
             *
             *
             */
            f_szukaj.setOnKeyReleased(e -> {
                if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Wszyscy")) {
                    f_szukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

                        filteredData.setPredicate((Predicate<? super User>) user -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();
                            // tu moze byc blad
                            if (user.getImie().contains(newValue)) {
                                return true;
                            } else if (user.getNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });

                    });

                    System.out.println("aaaa " + tabpane.getSelectionModel().getSelectedItem().getText());
                    SortedList<User> sortedData = new SortedList<>(filteredData);
                    sortedData.comparatorProperty().bind(tv_wszyscy.comparatorProperty());
                    tv_wszyscy.setItems(sortedData);

                } else if (tabpane.getSelectionModel().getSelectedItem().getText().equals("VIP")) {

                    f_szukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

                        filteredData1.setPredicate((Predicate<? super User>) user -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();
                            // tu moze byc blad
                            if (user.getImie().contains(newValue)) {
                                return true;
                            } else if (user.getNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });

                    });

                    System.out.println("bbbb " + tabpane.getSelectionModel().getSelectedItem().getText());
                    SortedList<User> sortedData1 = new SortedList<>(filteredData1);
                    sortedData1.comparatorProperty().bind(tv_vip.comparatorProperty());
                    tv_vip.setItems(sortedData1);
                } else if (tabpane.getSelectionModel().getSelectedItem().getText().equals("Pracownicy")) {

                    f_szukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

                        filteredData2.setPredicate((Predicate<? super User>) user -> {
                            if (newValue == null || newValue.isEmpty()) {
                                return true;
                            }
                            String lowerCaseFilter = newValue.toLowerCase();
                            // tu moze byc blad
                            if (user.getImie().contains(newValue)) {
                                return true;
                            } else if (user.getNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                                return true;
                            }
                            return false;
                        });

                    });

                    System.out.println("cccc " + tabpane.getSelectionModel().getSelectedItem().getText());
                    SortedList<User> sortedData2 = new SortedList<>(filteredData2);
                    sortedData2.comparatorProperty().bind(tv_pracowincy.comparatorProperty());
                    tv_pracowincy.setItems(sortedData2);

                }

            });
            /**
             * Anonimowa metoda dla przycisku b_zadania - otwiera widok
             * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
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
             * Anonimowa metoda dla przycisku b_dodaj - otwiera widok
             * dodaj_uzytkownika.fxml gdzie mozna dodac nowego uzytkownika
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_dodaj.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent dodaj_uzytkownika_parent = FXMLLoader.load(getClass().getResource("dodaj_uzytkownika.fxml"));
                        Scene dodaj_uzytkownika_scene = new Scene(dodaj_uzytkownika_parent);
                        Stage dodaj_uzytkownika_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        dodaj_uzytkownika_stage.setScene(dodaj_uzytkownika_scene);
                        dodaj_uzytkownika_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });
            /**
             * Anonimowa metoda dla przycisku b_edytuje - otwiera widok
             * Edytuj_uzytkownika.fxml gdzie mozna edytowac dane uzytkownika
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_edytuj.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjaTV()) {
                        try {

                            Parent edytuj_uzytkownika_parent = FXMLLoader.load(getClass().getResource("Edytuj_uzytkownika.fxml"));
                            Scene edytuj_uzytkownika_scene = new Scene(edytuj_uzytkownika_parent);
                            Stage edytuj_uzytkownika_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            edytuj_uzytkownika_stage.setScene(edytuj_uzytkownika_scene);
                            edytuj_uzytkownika_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            });
            /**
             * Anonimowa metoda dla przycisku b_usun - usuwa zaznaczonego
             * uzytkownika i updatuje baze danych ,otwiera widok uzytkonicy.fxml
             * gdzie mozna dodac,usunac, edytowac, uzytkownikow
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
                    if (walidacjaTV() && walidacjayesno()) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("delete from uzytkownicy where idUzytkownika = '" + ID + "'");
                            statment.executeUpdate();

                            Parent uzytkownicy_parent = FXMLLoader.load(getClass().getResource("uzytkownicy.fxml"));
                            Scene uzytkownicy_scene = new Scene(uzytkownicy_parent);
                            Stage uzytkownicy_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            uzytkownicy_stage.setScene(uzytkownicy_scene);
                            uzytkownicy_stage.show();

                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
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
                                alert.setContentText("Bład");
                                alert.showAndWait();
                            }
                        } catch (IOException ex) {
                            Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            });

            // TODO
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
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
                alert.setContentText("Bład");
                alert.showAndWait();
            }
        }
    }

    private boolean walidacjaTV() {
        if (ID == 0) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Wybierz użytkownika");
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrano użytkownika do edycji lub usunięcia");
            alert.showAndWait();

            return false;
        }
        return true;
    }

    private boolean walidacjayesno() {
        int dialogButton = JOptionPane.YES_NO_OPTION;

        String[] options = new String[2];
        options[0] = new String("Tak");
        options[1] = new String("Nie");
        int dialogResult = JOptionPane.showOptionDialog(null, "Usunąć użytkownika  " + Imie + " " + Nazwisko + " !?", "Usuń użytkownika", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        if (dialogResult == 0) {
            return true;
        } else {
            return false;
        }

    }
}
