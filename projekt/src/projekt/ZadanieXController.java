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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 * FXML Controller class
 *
 * @author Klaudia
 */
public class ZadanieXController implements Initializable {

    @FXML
    private Label l_zadanie;
    @FXML
    private Button b_opusc;
    @FXML
    private Button b_wstecz;
    @FXML
    private Button b_zatwierdz;
    @FXML
    private ComboBox<String> c_status;
    @FXML
    private TextArea a_opis;
    @FXML
    private TextField f_uzytkownik;
    @FXML
    private ListView<String> lv_komentarze;
    @FXML
    private Button b_skomentuj;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        try {
            Moje_zadaniaController moje = new Moje_zadaniaController();
            a_opis.setText(moje.Opis);
            loginController login = new loginController();
            f_uzytkownik.setText(login.ble); // tu cos zmienic
            l_zadanie.setText(moje.Nazwa);

            Class.forName("com.mysql.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
            PreparedStatement statment = con.prepareStatement("select Status_zadania from zadania where Nazwa='" + moje.Nazwa + "' and Opis='" + moje.Opis + "' and projekt = '" + moje.Projekt + "'");
            ResultSet result = statment.executeQuery();

            if (result.next()) {
                System.out.println(result.getString(1)); // do testowania
            }
            c_status.setPromptText(result.getString(1));

            ObservableList<String> options
                    = FXCollections.observableArrayList(
                            "Aktualne",
                            "FORTEST"
                    );
            c_status.setItems(options);

            final ObservableList<String> data = FXCollections.observableArrayList();
            statment = con.prepareStatement("select Opis  from komentarze where idZadania = '" + moje.IDzad + "'");
            result = statment.executeQuery();

            while (result.next()) {

                data.add(
                        result.getString("Opis")
                );
                lv_komentarze.setItems(data);

            }

            /**
             * Anonimowa metoda dla przycisku b_opusc - pozwala uzytkownikowy na
             * opuszczenie zadania ,otwiera widok Opusc_zadanie.fxml gdzie
             * trzeba podac komentarz/ powod dlaczego opuszczamy zadanie
             *
             * @exception IOExeption ex - wyjatek odnoscie otwierania i
             * znajdywania plików fmxl
             */
            b_opusc.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    if (walidacjayesno()) {
                        try {

                            Parent moje_zadanie_parent = FXMLLoader.load(getClass().getResource("Opusc_zadanie.fxml"));
                            Scene moje_zadanie_scene = new Scene(moje_zadanie_parent);
                            Stage moje_zadanie_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                            moje_zadanie_stage.setScene(moje_zadanie_scene);
                            moje_zadanie_stage.show();

                        } catch (IOException ex) {
                            Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                        }

                    }
                }
            });
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

                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz?characterEncoding=utf8", "root", "");
                        PreparedStatement statment = con.prepareStatement("UPDATE zadania SET Status_zadania='" + c_status.getValue() + "' WHERE  Nazwa='" + moje.Nazwa + "' and Opis='" + moje.Opis + "' and projekt = '" + moje.Projekt + "' and idZadania = '" + moje.IDzad + "'");
                        statment.executeUpdate();

                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.getStylesheets().add(
                                getClass().getResource("/css/myDialogs.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setTitle("Info");
                        alert.setHeaderText(null);
                        alert.setContentText("Zmieniles status zadania");
                        alert.showAndWait();

                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (SQLException ex) {
                        Logger.getLogger(ZadanieXController.class.getName()).log(Level.SEVERE, null, ex);
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
        int dialogResult = JOptionPane.showOptionDialog(null, "Czy chcesz opuścić zadanie " + a_opis.getText() + " !?", "Opuść zadanie", 0, JOptionPane.INFORMATION_MESSAGE, null, options, null);

        if (dialogResult == 0) {
            return true;
        } else {
            return false;
        }

    }

}
