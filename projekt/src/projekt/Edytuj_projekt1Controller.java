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
public class Edytuj_projekt1Controller implements Initializable {

    @FXML
    private Button b_zapisz;
    @FXML
    private Button b_anuluj;
    @FXML
    private TextField f_nazwa;
    @FXML
    private TextArea f_opis;
    @FXML
    private TextField f_poczatek;
    @FXML
    private TextField f_koniec;

    String stara_nazwa;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        ProjektyController pro = new ProjektyController();

        f_nazwa.setText(pro.Nazwa);
        f_poczatek.setText(pro.Poczatek);
        f_koniec.setText(pro.Koniec);
        f_opis.setText(pro.Opis);
        stara_nazwa = f_nazwa.getText();

        /**
         * Anonimowa metoda dla przycisku b_zapisz - zatwierdza dokonane zmiany
         * w projekcie , updatuje baze i otwiera widok Projekt_X.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
         * @exception SQLException ex - wyjatek zajmujący się obsługą bazy
         * danych
         * @exception ClassNotFoundException ex - wyjatek wystepujacy kiedy nie
         * mozna odnalezc klasy
         */
        b_zapisz.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (walidacjaPola() && walidacjaNazwa() && walidacjaData() && walidacjaData1() && walidacjaData2() && walidacjaData3() && walidacjaData4()) {
                        try {

                            Class.forName("com.mysql.jdbc.Driver");
                            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                            PreparedStatement statment = con.prepareStatement("UPDATE projekty SET Nazwa='" + f_nazwa.getText() + "', Poczatek='" + f_poczatek.getText() + "', Koniec='" + f_koniec.getText() + "' ,Opis='" + f_opis.getText() + "' WHERE  idProjektu='" + pro.IDprojektu + "'");
                            statment.executeUpdate();
                            statment = con.prepareStatement("UPDATE zadania SET projekt='" + f_nazwa.getText() + "' WHERE  projekt='" + stara_nazwa + "'");
                            statment.executeUpdate();
                            pro.Nazwa = f_nazwa.getText();
                            Parent projektx_parent = FXMLLoader.load(getClass().getResource("Projekt_X.fxml"));
                            Scene projektx_scene = new Scene(projektx_parent);
                            Stage projektx_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            projektx_stage.setScene(projektx_scene);
                            projektx_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(Projekt_XController.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (ClassNotFoundException ex) {
                            Logger.getLogger(Edytuj_projekt1Controller.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (SQLException ex) {
                            Logger.getLogger(Edytuj_projekt1Controller.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                } catch (ClassNotFoundException ex) {
                    Logger.getLogger(Edytuj_projekt1Controller.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SQLException ex) {
                    Logger.getLogger(Edytuj_projekt1Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        /**
         * Anonimowa metoda dla przycisku b_anuluj - cofa do widoku
         * Projekt_X.fxml
         *
         * @exception IOExeption ex - wyjatek odnoscie otwierania i znajdywania
         * plików fmxl
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

    private boolean walidacjaNazwa() throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
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
