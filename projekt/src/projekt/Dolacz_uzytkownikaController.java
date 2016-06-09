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
import javafx.stage.Stage;
import static projekt.loginController.uzytkownikID;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Dolacz_uzytkownikaController implements Initializable {

    @FXML
    private TableView<User> tv_vip;
    @FXML
    private TableColumn<User, String> tc_nazwisko;
    @FXML
    private TableColumn<User, String> tc_imie;
    @FXML
    private TableView<User> tv_pracownik;
    @FXML
    private Button b_dolacz;
    @FXML
    private Button b_anuluj;
    @FXML
    private TableColumn<User, String> tc_nazwisko1;
    @FXML
    private TableColumn<User, String> tc_imie1;

    int ID = 0;
    static String ludzie, ludzie1;

    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {

            final ObservableList<User> data1 = FXCollections.observableArrayList();
            final ObservableList<User> data2 = FXCollections.observableArrayList();
            tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            tc_nazwisko1.setCellValueFactory(new PropertyValueFactory<User, String>("Nazwisko"));
            tc_imie1.setCellValueFactory(new PropertyValueFactory<User, String>("Imie"));

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment;
            ResultSet result;
            ProjektyController projekt = new ProjektyController();
            statment = con.prepareStatement("select ludzie from projekty where idProjektu='" + projekt.IDprojektu + "'");
            result = statment.executeQuery();
            if (result.next()) {
                ludzie1 = result.getString(1);
            }

            statment = con.prepareStatement("select Nazwisko , Imie ,idUzytkownika from uzytkownicy where ranga = '1'");
            result = statment.executeQuery();

            while (result.next()) {

                if (result.getString("idUzytkownika").equals(uzytkownikID) || (ludzie1.contains("," + result.getString("idUzytkownika")) || ludzie1.contains(result.getString("idUzytkownika") + ","))) {
                    continue;
                } else {
                    data1.add(new User(
                            result.getString("Nazwisko"),
                            result.getString("Imie")
                    ));
                    tv_vip.setItems(data1);
                }
            }

            statment = con.prepareStatement("select Nazwisko , Imie, idUzytkownika from uzytkownicy where ranga = '3'");
            result = statment.executeQuery();

            while (result.next()) {
                if (result.getString("idUzytkownika").equals(uzytkownikID) || (ludzie1.contains("," + result.getString("idUzytkownika")) || ludzie1.contains(result.getString("idUzytkownika") + ","))) {
                    continue;
                } else {
                    data2.add(new User(
                            result.getString("Nazwisko"),
                            result.getString("Imie")
                    ));
                    tv_pracownik.setItems(data2);
                }
            }
            /**
             * Anonimowa metoda dla tabeli vip - za pomoza fetchingu zczytuje
             * dane z wiersza w tablicy ktory zostanie kliniety
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_vip.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_vip.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select idUzytkownika from uzytkownicy where Imie ='" + user.getImie() + "'  and Nazwisko ='" + user.getNazwisko() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            ID = result1.getInt(1);

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
             * Anonimowa metoda dla tabeli pracownik - za pomoza fetchingu
             * zczytuje dane z wiersza w tablicy ktory zostanie kliniety
             *
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             */
            tv_pracownik.setOnMouseClicked(new EventHandler<MouseEvent>() {

                public void handle(MouseEvent e) {
                    try {
                        User user = (User) tv_pracownik.getSelectionModel().getSelectedItem();
                        PreparedStatement statment1 = con.prepareStatement("select idUzytkownika from uzytkownicy where Imie ='" + user.getImie() + "'  and Nazwisko ='" + user.getNazwisko() + "' ");
                        ResultSet result1 = statment1.executeQuery();

                        while (result1.next()) {
                            ID = result1.getInt(1);

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
             * Anonimowa metoda dla przycisku b_dodaj - dolacza nowego
             * uzytkownika z tabeli do projektu i otwiera widok Projekt_X.fxml
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
             * danych
             * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy
             * nie mozna odnalezc klasy
             */
            b_dolacz.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    ProjektyController projekt = new ProjektyController();
                    if (walidacjaTV()) {
                        try {
                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("select ludzie from projekty where idProjektu='" + projekt.IDprojektu + "'");
                            ResultSet result = statment.executeQuery();
                            if (result.next()) {
                                ludzie = result.getString(1);
                            }
                            statment = con.prepareStatement("update projekty set ludzie='" + (ludzie + "," + ID) + "' where idProjektu = '" + projekt.IDprojektu + "' ");
                            statment.executeUpdate();

                            Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                            Scene projektx_scene = new Scene(projektx_parent);
                            Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            projektx_stage.setScene(projektx_scene);
                            projektx_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Dolacz_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Dolacz_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            });
            /**
             * Anonimowa metoda dla przycisku b_anuluj - otwiera widok
             * Projekt_X.fxml
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
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
            Logger.getLogger(Dolacz_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Dolacz_uzytkownikaController.class.getName()).log(Level.SEVERE, null, ex);
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
            alert.setContentText("Nie wybrano użytkownika");
            alert.showAndWait();

            return false;
        }
        return true;
    }

}
