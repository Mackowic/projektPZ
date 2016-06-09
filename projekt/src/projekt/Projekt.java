/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package projekt;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 *
 * @author Maciek
 */

public class Projekt extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));

        Scene scene = new Scene(root);
        Image applicationIcon = new Image(getClass().getResourceAsStream("/img/logo.jpg"));
        stage.getIcons().add(applicationIcon);
        
        stage.setTitle("Task Project Menager");
        stage.setScene(scene);
         stage.centerOnScreen();
         //stage.setY(stage.getY() * 3f / 2f);
        stage.show();
stage.setResizable(false);
        
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
