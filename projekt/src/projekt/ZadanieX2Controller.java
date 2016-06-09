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
import java.util.function.Predicate;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class ZadanieX2Controller implements Initializable {

    @FXML
    private Label l_zadanie;
    @FXML
    private Button b_wstecz;
    @FXML
    private Button b_zatwierdz;
    @FXML
    private TextArea a_opis;
    @FXML
    private ListView<String> lv_komentarze;
    @FXML
    private Button b_skomentuj;
    @FXML
    private TextField f_uzytkownik;

    public static String Imie, Nazwisko, Mail, Telefon, Nazwa, Opis, Status_zadania;
    public static int IDzad2 = 0;
    @FXML
    private TableView<User> tv_uzytkownicy;
    @FXML
    private TableColumn<User, String> tc_nazwisko;
    @FXML
    private TableColumn<User, String> tc_imie;
    @FXML
    private TextField f_szukaj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            Projekt_XController moje = new Projekt_XController();
            a_opis.setText(moje.Opis);
            loginController login = new loginController();
            // tu cos zmienic
            l_zadanie.setText(moje.Nazwa);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment;
            ResultSet result;

            final ObservableList<String> data = FXCollections.observableArrayList();
            statment = con.prepareStatement("select Opis  from komentarze where idZadania = '" + moje.IDzadan + "'");
            result = statment.executeQuery();

            while (result.next()) {

                data.add(
                        result.getString("Opis")
                );
                lv_komentarze.setItems(data);

            }
            final ObservableList<User> data3 = FXCollections.observableArrayList();
            tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy");
            result = statment.executeQuery();

            while (result.next()) {
                if (result.getString("Nazwisko").equals("Brak uzytkownika") || result.getString("Nazwisko").equals("Admin")) {
                    continue;
                } else {
                    data3.add(new User(
                            result.getString("Nazwisko"),
                            result.getString("Imie")
                    ));
                    tv_uzytkownicy.setItems(data3);
                }
            }

            FilteredList<User> filteredData2 = new FilteredList<>(data3, e -> true);
            /**
             * Metoda lambda dla pola f_szukaj - ktora filtruje wyniki w
             * tabelach
             *
             *
             */
            f_szukaj.setOnKeyReleased(e -> {

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

                SortedList<User> sortedData = new SortedList<>(filteredData2);
                sortedData.comparatorProperty().bind(tv_uzytkownicy.comparatorProperty());
                tv_uzytkownicy.setItems(sortedData);

            });

            statment = con.prepareStatement("SELECT Mail FROM `uzytkownicy`, (SELECT zadania.idUzytkownika FROM zadania WHERE idZadania = '" + moje.IDzadan + "') x WHERE uzytkownicy.idUzytkownika=x.idUzytkownika");
            result = statment.executeQuery();
            if (result.next()) {
                f_uzytkownik.setText(result.getString("Mail"));
            }

            /**
             * Anonimowa metoda dla przycisku b_wstecz - otwiera widok
             * Moje_zadania.fxml gdzie mozna zobaczyc swoje zadania
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
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
                            IDzad2 = result1.getInt(5);

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
             * Anonimowa metoda dla przycisku b_zmien_haslo - zmienia status
             * zadania i updatuje zmiane w bazie danych
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             */
            b_zatwierdz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjaTV() && walidacjayesno()) {
                        try {
                            System.out.println("wyswietl id " + IDzad2 + " nazwe " + moje.Nazwa + " idzadania " + moje.IDzadan);
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("UPDATE zadania SET idUzytkownika ='" + IDzad2 + "' , Opis ='" + a_opis.getText() + "' WHERE idZadania = '" + moje.IDzadan + "'");
                            statment.executeUpdate();

                            Alert alert = new Alert(Alert.AlertType.WARNING);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("/css/myDialogs.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setTitle("Info");
                            alert.setHeaderText(null);
                            alert.setContentText("Zmieniles uzytkownika i opis");
                            alert.showAndWait();

                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });

            /**
             * Anonimowa metoda dla przycisku b_skomentuj - otwiera widok
             * komenty.fxml gdzie mozna dodac komentarz
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_skomentuj.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("komenty.fxml"));
                        Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                        Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        moje_zadanie_stage.setScene(moje_zadanie_scene);
                        moje_zadanie_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

            // TODO
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean walidacjayesno() {
        int dialogButton = JOptionPane.YES_NO_OPTION;

        String[] options = new String[2];
        options[0] = new String("Tak");
        options[1] = new String("Nie");
        int dialogResult = JOptionPane.showOptionDialog(null, "Czy chcesz zmienić użytkownika: " + Imie + " " + Nazwisko + " !?", "Zmień użytkownika", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        if (dialogResult == 0) {
            return true;
        } else {
            return false;
        }

    }

    private boolean walidacjaTV() {
        try {
            if (Imie.equals("null") | Nazwisko.equals("null")) {
                return false;
            }
            return true;
        } catch (NullPointerException ex) {
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
    }

}
