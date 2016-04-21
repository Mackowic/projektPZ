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

/**
 * FXML Controller class
 *
 * @author Klaudia
 */
public class UzytkownicyController implements Initializable {
 @FXML
    private Button b_dodaj;
 @FXML
    private Button b_szukaj;
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
    
    public static String  Imie , Nazwisko, Mail, Telefon;
    public static int ID;
    @FXML
    private TextField f_szukaj;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
     try {
         final ObservableList <User> data = FXCollections.observableArrayList();
         final ObservableList <User> data1 = FXCollections.observableArrayList();
         final ObservableList <User> data2 = FXCollections.observableArrayList();
         tc_nazwisko.setCellValueFactory(new PropertyValueFactory<User,String>("Nazwisko"));
         tc_imie.setCellValueFactory(new PropertyValueFactory<User,String>("Imie"));
         
         tc_nazwisko1.setCellValueFactory(new PropertyValueFactory<User,String>("Nazwisko"));
         tc_imie1.setCellValueFactory(new PropertyValueFactory<User,String>("Imie"));
         
         tc_nazwisko2.setCellValueFactory(new PropertyValueFactory<User,String>("Nazwisko"));
         tc_imie2.setCellValueFactory(new PropertyValueFactory<User,String>("Imie"));
         
         Class.forName("com.mysql.jdbc.Driver");
         Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
         PreparedStatement statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy");
         ResultSet result = statment.executeQuery();
         
         while(result.next()){
             data.add(new User(
             result.getString("Nazwisko"),
             result.getString("Imie") 
             ));
             tv_wszyscy.setItems(data);
         }
         
         //test
         
         statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy where Nazwisko like 'B%'");
         result = statment.executeQuery();
         
         while(result.next()){
             data1.add(new User(
             result.getString("Nazwisko"),
             result.getString("Imie") 
             ));
             tv_vip.setItems(data1);
         }
         
         statment = con.prepareStatement("select Nazwisko , Imie from uzytkownicy where Nazwisko like 'B%'");
         result = statment.executeQuery();
         
         while(result.next()){
             data2.add(new User(
             result.getString("Nazwisko"),
             result.getString("Imie") 
             ));
             tv_pracowincy.setItems(data2);
         }
   
         
         tv_wszyscy.setOnMouseClicked(new EventHandler<MouseEvent>() {

             public void handle(MouseEvent e) {
                 try {
                     User user = (User)tv_wszyscy.getSelectionModel().getSelectedItem();
                     PreparedStatement statment1 = con.prepareStatement("select Imie , Nazwisko, Mail, Telefon, idUzytkownika from uzytkownicy where Imie ='"+user.getImie()+"'  and Nazwisko ='"+user.getNazwisko()+"' ");
                     ResultSet result1 = statment1.executeQuery();
                     
                     while(result1.next()){
                         Imie = result1.getString(1);
                         Nazwisko = result1.getString(2);
                         Mail = result1.getString(3);
                         Telefon = result1.getString(4);
                         ID = result1.getInt(5);
            
         }  } catch (SQLException ex) {
                       if(ex.getCause() instanceof Exception){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd połączenia z bazą danych");
                alert.showAndWait();
     }else{ 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd");
                alert.showAndWait();
     }
                 }
             }
         });//
                     
         tv_vip.setOnMouseClicked(new EventHandler<MouseEvent>() {

             public void handle(MouseEvent e) {
                 try {
                     User user = (User)tv_vip.getSelectionModel().getSelectedItem();
                     PreparedStatement statment1 = con.prepareStatement("select Imie , Nazwisko, Mail, Telefon, idUzytkownika from uzytkownicy where Imie ='"+user.getImie()+"'  and Nazwisko ='"+user.getNazwisko()+"' ");
                     ResultSet result1 = statment1.executeQuery();
                     
                     while(result1.next()){
                         Imie = result1.getString(1);
                         Nazwisko = result1.getString(2);
                         Mail = result1.getString(3);
                         Telefon = result1.getString(4);
                         ID = result1.getInt(5);
            
         }  } catch (SQLException ex) {
                       if(ex.getCause() instanceof Exception){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd połączenia z bazą danych");
                alert.showAndWait();
     }else{ 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd");
                alert.showAndWait();
     }
                 }
             }
         });//
          tv_pracowincy.setOnMouseClicked(new EventHandler<MouseEvent>() {

             public void handle(MouseEvent e) {
                 try {
                     User user = (User)tv_wszyscy.getSelectionModel().getSelectedItem();
                     PreparedStatement statment1 = con.prepareStatement("select Imie , Nazwisko, Mail, Telefon, idUzytkownika from uzytkownicy where Imie ='"+user.getImie()+"'  and Nazwisko ='"+user.getNazwisko()+"' ");
                     ResultSet result1 = statment1.executeQuery();
                     
                     while(result1.next()){
                         Imie = result1.getString(1);
                         Nazwisko = result1.getString(2);
                         Mail = result1.getString(3);
                         Telefon = result1.getString(4);
                         ID = result1.getInt(5);
            
         }
                     
                 } catch (SQLException ex) {
                       if(ex.getCause() instanceof Exception){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd połączenia z bazą danych");
                alert.showAndWait();
     }else{ 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("błąd");
                alert.showAndWait();
     }
                 }
             }
         });
          
          
          FilteredList<User> filteredData = new FilteredList<>(data, e -> true);
          
          f_szukaj.setOnKeyReleased(e ->{
              f_szukaj.textProperty().addListener((observableValue, oldValue, newValue) ->{
          
          filteredData.setPredicate((Predicate<? super User>) user->{
          if(newValue == null || newValue.isEmpty()){
          return true;
          }
          String lowerCaseFilter = newValue.toLowerCase();
          // tu moze byc blad
          if(user.getImie().contains(newValue)){
          return true;
          }else 
          if(user.getNazwisko().toLowerCase().contains(lowerCaseFilter)){
          return true;
          }
              return false;
          });
  
          });
          
              SortedList<User> sortedData = new SortedList<>(filteredData);
              sortedData.comparatorProperty().bind(tv_wszyscy.comparatorProperty());
              tv_wszyscy.setItems(sortedData);
              
         });
          
          
          
         
         b_wstecz.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 
                 try {
                     
                     Parent main_parent = FXMLLoader.load(getClass().getResource("main.fxml"));
                     Scene main_scene = new Scene(main_parent);
                     Stage main_stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                     main_stage.setScene(main_scene);
                     main_stage.show();
                     
                 } catch (IOException ex) {
                     Logger.getLogger(mainController.class.getName()).log(Level.SEVERE, null, ex);
                 }
                 
             }
         });
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
         b_edytuj.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 
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
         });
         
         b_usun.setOnAction(new EventHandler<ActionEvent>() {
             @Override
             public void handle(ActionEvent actionEvent) {
                 
                 try {
                     Class.forName("com.mysql.jdbc.Driver");
                     Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/pz","root","");
                     PreparedStatement statment = con.prepareStatement("delete from uzytkownicy where idUzytkownika = '"+ID+"'");
                     statment.executeUpdate();
                 } catch (ClassNotFoundException ex) {
                     Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
                 } catch (SQLException ex) {
                       if(ex.getCause() instanceof Exception){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd połączenia z bazą danych");
                alert.showAndWait();
     }else{ 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Bład");
                alert.showAndWait();
     }
                 }
                 
             }
         });
         
         // TODO
     } catch (ClassNotFoundException ex) {
         Logger.getLogger(UzytkownicyController.class.getName()).log(Level.SEVERE, null, ex);
     } catch (SQLException ex) {
           if(ex.getCause() instanceof Exception){
           Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Błąd połączenia z bazą danych");
                alert.showAndWait();
     }else{ 
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Bład");
                alert.showAndWait();
     }
     }
    }    
    
}
