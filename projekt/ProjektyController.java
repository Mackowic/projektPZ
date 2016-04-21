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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Klaudia
 */
public class ProjektyController implements Initializable {
    @FXML
    private Button b_dodaj;
    @FXML
    private Button b_wstecz;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        try {
         final ObservableList <Project> data = FXCollections.observableArrayList();
         final ObservableList <Project> data1 = FXCollections.observableArrayList();
         tc_nazwa.setCellValueFactory(new PropertyValueFactory<Project,String>("Nazwa"));
         tc_opis.setCellValueFactory(new PropertyValueFactory<Project,String>("Opis"));
         tc_poczatek.setCellValueFactory(new PropertyValueFactory<Project,String>("Poczatek"));
         tc_koniec.setCellValueFactory(new PropertyValueFactory<Project,String>("Koniec"));
         
         tc_nazwa1.setCellValueFactory(new PropertyValueFactory<Project,String>("Nazwa"));
         tc_opis1.setCellValueFactory(new PropertyValueFactory<Project,String>("Opis"));
         tc_poczatek1.setCellValueFactory(new PropertyValueFactory<Project,String>("Poczatek"));
         tc_koniec1.setCellValueFactory(new PropertyValueFactory<Project,String>("Koniec"));
         
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
         PreparedStatement statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty");
         ResultSet result = statment.executeQuery();
         
         while(result.next()){
             data.add(new Project(
             result.getString("Nazwa"),
             result.getString("Opis"),
             result.getString("Poczatek"),
             result.getString("Koniec") 
             ));
             tv_aktualne.setItems(data);
         }
         
         //test
         
         statment = con.prepareStatement("select Nazwa , Opis, Poczatek, Koniec from projekty where Koniec > CURRENT_DATE");
         result = statment.executeQuery();
         
         while(result.next()){
             data1.add(new Project(
             result.getString("Nazwa"),
             result.getString("Opis"),
             result.getString("Poczatek"),
             result.getString("Koniec") 
             ));
             tv_zakonczone.setItems(data1);
         }
   
        
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
          b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
    @Override
    public void handle(ActionEvent actionEvent) {
         
        try {
            
            Parent main_parent = FXMLLoader.load(getClass().getResource("main.fxml"));
            Scene main_projekt_scene = new Scene(main_parent);
            Stage main_projekt_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            main_projekt_stage.setScene(main_projekt_scene);
            main_projekt_stage.show();
            
        } catch (IOException ex) {
            Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
        }
 
    }
});
       
        // TODO
    }   catch (ClassNotFoundException ex) {    
            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ProjektyController.class.getName()).log(Level.SEVERE, null, ex);
        }    
    
}}
