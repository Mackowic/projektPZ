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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Dodaj_zadanieController implements Initializable {

    @FXML
    private TableView<User> tv_uzytkownicy;
    @FXML
    private TableColumn<User, String> tc_nazwisko;
    @FXML
    private TableColumn<User, String> tc_imie;
    @FXML
    private TableColumn<User, Button> tc_przycisk;
    @FXML
    private Button b_zapisz;
    @FXML
    private Button b_wstecz;
    @FXML
    private TextField f_nazwa;
    @FXML
    private TextField f_projekt;
    @FXML
    private TextField f_szukaj;
    @FXML
    private TextArea t_opis;

    public static int IDuzytkownika = 0;
    String string;

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            ProjektyController projekt = new ProjektyController();
            f_projekt.setText(ProjektyController.Nazwa);

            final ObservableList<User> data = FXCollections.observableArrayList();
            tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment = con.prepareStatement("select ludzie from projekty where idProjektu = '" + projekt.IDprojektu + "'");
            ResultSet result = statment.executeQuery();
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
                    data.add(new User(
                            result.getString("Nazwisko"),
                            result.getString("Imie")
                    ));
                    tv_uzytkownicy.setItems(data);
                }
            }

            FilteredList<User> filteredData = new FilteredList<>(data, e -> true);
            /**
             * Metoda labda dla textfielda ktora filtruje dane w tabeli
             *
             * @return true lub false
             */
            f_szukaj.setOnKeyReleased(e -> {
                f_szukaj.textProperty().addListener((observableValue, oldValue, newValue) -> {

                    filteredData.setPredicate((Predicate<? super User>) user -> {
                        if (newValue == null || newValue.isEmpty()) {
                            return true;
                        }
                        String lowerCaseFilter = newValue.toLowerCase();
                        if (user.getImie().contains(newValue)) {
                            return true;
                        } else if (user.getNazwisko().toLowerCase().contains(lowerCaseFilter)) {
                            return true;
                        }
                        return false;
                    });

                });

                SortedList<User> sortedData = new SortedList<>(filteredData);
                sortedData.comparatorProperty().bind(tv_uzytkownicy.comparatorProperty());
                tv_uzytkownicy.setItems(sortedData);

            });
            /**
             * Anonimowa metoda obslugujaca fetching danych z tabeli po
             * kliknieciu na danym wierszu
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_uzytkownicy.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_uzytkownicy.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select idUzytkownika from uzytkownicy where Imie ='" + user.getImie() + "'  and Nazwisko ='" + user.getNazwisko() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            IDuzytkownika = result1.getInt(1);

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
            });//           
            /**
             * Anonimowa metoda dla przycisku b_zapisz - dodaje nowe zadanie i
             * otwiera widok Projekt_X.fxml
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             * @exception IOException ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjaPola() && walidacjaTV()) {
                        try {
                            ProjektyController projekt = new ProjektyController();
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("insert into zadania (Nazwa,idUprawnienia,idUzytkownika, Opis, projekt, Status_zadania) VALUES ('" + f_nazwa.getText() + "',1,'" + IDuzytkownika + "','" + t_opis.getText() + "','" + projekt.Nazwa + "','Aktualne')");
                            statment.executeUpdate();

                            Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                            Scene projektx_scene = new Scene(projektx_parent);
                            Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            projektx_stage.setScene(projektx_scene);
                            projektx_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Dodaj_zadanieController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Dodaj_zadanieController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });
            /**
             * Anonimowa metoda dla przycisku b_wstecz - otwiera widok
             * Projekt_X.fxml
             *
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             */
            b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {

                    try {

                        Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                        Scene projektx_scene = new Scene(projektx_parent);
                        Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                        projektx_stage.setScene(projektx_scene);
                        projektx_stage.show();

                    } catch (IOException ex) {
                        Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                    }

                }
            });

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Dodaj_zadanieController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Dodaj_zadanieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean walidacjaPola() {
        if (f_nazwa.getText().isEmpty() | t_opis.getText().isEmpty()) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Puste pole");
            alert.setHeaderText(null);
            alert.setContentText("Pola nie mogą być puste");
            alert.showAndWait();

            return false;
        }
        return true;
    }

    private boolean walidacjaTV() {
        if (IDuzytkownika == 0) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Wybierz użytkownika");
            alert.setHeaderText(null);
            alert.setContentText("Nie wybrano użytkownika");
            alert.showAndWait();

            return false;
        }
        return true;
    }

}
