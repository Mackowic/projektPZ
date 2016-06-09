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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Maciek
 */
public class Dodaj_projektController implements Initializable {

    @FXML
    private Button b_dodaj;
    @FXML
    private Button b_anuluj;
    @FXML
    private TextField f_nazwa;
    @FXML
    private TextField f_poczatek;
    @FXML
    private TextField f_koniec;
    @FXML
    private TextArea f_opis;

    public String ludzie = "";

    int i = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /**
         * Anonimowa metoda dla przycisku b_anuluj - cofa do poprzedniwgo widoku
         * Tworzy stage i scene do widoku Projekty.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         */
        b_anuluj.setOnAction(new EventHandler<ActionEvent>() {
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
         * Anonimowa metoda dla przycisku b_dodaj - dodaje nowy projekt Tworzy
         * stage i scene do widoku Projekty.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         */
        b_dodaj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (walidacjaPola() && walidacjaNazwa() && walidacjaData() && walidacjaData1() && walidacjaData2() && walidacjaData3() && walidacjaData4()) {
                        try {

                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            loginController login = new loginController();
                            System.out.println(login.id);
                            PreparedStatement statment = con.prepareStatement("insert into zadania (Nazwa,idUprawnienia,idUzytkownika,Opis,projekt,Status_zadania) VALUES ('Dodaj nowych uzytownikow i stworz nowe zadania',1,'" + login.uzytkownikID + "','O to twoje pierwsze zadanie!!','" + f_nazwa.getText() + "','Aktualne')");
                            statment.executeUpdate();
                            statment = con.prepareStatement("select idZadania from zadania where idUzytkownika='" + login.uzytkownikID + "'");
                            ResultSet result = statment.executeQuery();

                            if (result.next()) {
                                System.out.println(result.getString(1)); // do testowania
                            }

                            statment = con.prepareStatement("insert into projekty (Nazwa,Opis,Poczatek,Koniec, idZadania, idUzytkownika,ludzie) VALUES ('" + f_nazwa.getText() + "','" + f_opis.getText() + "','" + f_poczatek.getText() + "','" + f_koniec.getText() + "','" + result.getString(1) + "','" + login.uzytkownikID + "','" + login.uzytkownikID + "')");
                            statment.executeUpdate();
                            if (login.uzytkownikID.equals("1")) {
                            } else {
                                statment = con.prepareStatement("update uzytkownicy set ranga='1' where idUzytkownika='" + login.uzytkownikID + "'");
                                statment.executeUpdate();
                            }
//trzeba poprawic komende sql
                            Parent projekty_parent = FXMLLoader.load(getClass().getResource("Projekty.fxml"));
                            Scene projekty_scene = new Scene(projekty_parent);
                            Stage projekty_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            projekty_stage.setScene(projekty_scene);
                            projekty_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Dodaj_projektController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Dodaj_projektController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Dodaj_projektController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Dodaj_projektController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    /**
     * Metoda sprawdzająca poprawnośc daty rozpoczecia- czy wprowadzono poprawno
     * date miesięcy
     *
     * @return wartość true lub false
     */
    private boolean walidacjaData() {

        String tab[] = f_poczatek.getText().split("-");

        if (Integer.parseInt(tab[1]) <= 12) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Zla data");
            alert.setHeaderText(null);
            alert.setContentText("Nie ma takiego miesiaca!!");
            alert.showAndWait();

            return false;
        }
    }

    /**
     * Metoda sprawdzająca poprawność daty rozpoczecia - czy wprowadzono
     * poprawna date do miesiecy luty i miesiecy ktore mają 30 dni
     *
     * @return wartość true lub false
     */
    private boolean walidacjaData1() {

        String tab[] = f_poczatek.getText().split("-");

        if ((tab[1].equals("02") && Integer.parseInt(tab[2]) <= 28) || ((tab[1].equals("01") || tab[1].equals("03") || tab[1].equals("05") || tab[1].equals("07") || tab[1].equals("08") || tab[1].equals("10") || tab[1].equals("12")) && Integer.parseInt(tab[2]) <= 31) || ((tab[1].equals("04") || tab[1].equals("06") || tab[1].equals("09") || tab[1].equals("11")) && Integer.parseInt(tab[2]) <= 30)) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Zla data");
            alert.setHeaderText(null);
            alert.setContentText("Podales zla date");
            alert.showAndWait();

            return false;
        }

    }

    /**
     * Metoda sprawdzająca poprawność daty zakonczenia- czy wprowadzono poprawną
     * date miesięcy
     *
     * @return wartośc true lub false
     */
    private boolean walidacjaData2() {

        String tab[] = f_koniec.getText().split("-");

        if (Integer.parseInt(tab[1]) <= 12) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Zla data");
            alert.setHeaderText(null);
            alert.setContentText("Nie ma takiego miesiaca!!");
            alert.showAndWait();

            return false;
        }
    }

    /**
     * Metoda sprawdzająca poprawność daty zakonczenia - czy wprowadzono
     * poprawna date do miesiecy luty i miesiecy ktore mają 30 dni
     *
     * @return wartość true lub false
     */
    private boolean walidacjaData3() {

        String tab[] = f_koniec.getText().split("-");

        if ((tab[1].equals("02") && Integer.parseInt(tab[2]) <= 28) || ((tab[1].equals("01") || tab[1].equals("03") || tab[1].equals("05") || tab[1].equals("07") || tab[1].equals("08") || tab[1].equals("10") || tab[1].equals("12")) && Integer.parseInt(tab[2]) <= 31) || ((tab[1].equals("04") || tab[1].equals("06") || tab[1].equals("09") || tab[1].equals("11")) && Integer.parseInt(tab[2]) <= 30)) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Zla data");
            alert.setHeaderText(null);
            alert.setContentText("Podales zla date");
            alert.showAndWait();

            return false;
        }

    }

    /**
     * Metoda sprawdzająca poprawność daty - czy data rozpoczecia nie jest
     * pozniejsza niz zakonczenia
     *
     * @return wartośc true lub false
     */
    private boolean walidacjaData4() {

        String tab2 = f_poczatek.getText().replace("-", "");
        int a = Integer.parseInt(tab2);

        String tab3 = f_koniec.getText().replace("-", "");
        int b = Integer.parseInt(tab3);
        if (a < b) {
            return true;

        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Zla data");
            alert.setHeaderText(null);
            alert.setContentText("Data poczatkowa jest pozniej niz data zakonczenia");
            alert.showAndWait();

            return false;
        }

    }

    /**
     *
     * Metoda sprawdzająca czy taki projekt juz istnieje
     *
     * @author Artur
     * @return true lib false
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private boolean walidacjaNazwa() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz", "root", "");
        PreparedStatement statment = con.prepareStatement("SELECT Nazwa FROM projekty where Nazwa like '" + f_nazwa.getText() + "'");
        ResultSet result = statment.executeQuery();

        if (result.next()) {
            System.out.println(result.getString(1) + " "); // do testowania
        } else {
            return true;
        }

        if (f_nazwa.getText().equals(result.getString(1))) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStylesheets().add(
                    getClass().getResource("/css/myDialogs.css").toExternalForm());
            dialogPane.getStyleClass().add("myDialog");
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Taki projekt już istnieje");
            alert.showAndWait();
            return false;

        }

        return true;
    }

    /**
     *
     * Metoda sprawdzająca czy wymagane pola nie sa puste
     *
     * @author Artur
     * @return true lib false
     */
    private boolean walidacjaPola() {
        if (f_nazwa.getText().isEmpty() | f_poczatek.getText().isEmpty()
                | f_koniec.getText().isEmpty() | f_opis.getText().isEmpty()) {

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

}
