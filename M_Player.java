/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m_player;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.sql.*;
import javafx.event.EventHandler;
//import javafx.event.EventHandler;
import static javafx.scene.input.KeyCode.F;
import static javafx.scene.input.KeyCode.P;
import static javafx.scene.input.KeyCode.SPACE;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
//import javafx.scene.input.KeyEvent;
/**
 *
 * @author Gaurav
 */
public class M_Player extends Application{
    Scene scene;
    int even=1,space=1,dc=1;
    int sp=0;
    Stage c;
    FXMLDocumentController obj=new FXMLDocumentController();
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        scene = new Scene(root);
        //stage.setMaximized(true);
        scene.setOnKeyPressed(event->{
        //c=stage;
            if(event.getCode()==F && (even%2!=0))
            {
                System.out.println("F IS PRESSED");
                stage.setMaximized(true);
                even++;
            }
            else if(event.getCode()==F && (even%2)==0)
            {
                System.out.println("F IS PRESSED");
                stage.setMaximized(false);
                even++;
            }
            });
          scene.setOnMouseClicked(new EventHandler<MouseEvent>() {
    @Override
    public void handle(MouseEvent mouseEvent) {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2){
                {
                    if(dc%2!=0)
                    {
                        dc++;
                        stage.setMaximized(true);
                    }
                    else
                    {
                        dc++;
                        stage.setMaximized(false);
                    }
                }
            }
        }
    }
});
         
        stage.setScene(scene);
        stage.setTitle("AWESOME PLAYER");
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
       }
    
}
