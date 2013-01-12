package org.jpc.examples.osm.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene scene;
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("MapQuery");
        BorderPane borderPane = new BorderPane();
        //VBox vbox = new VBox();
        //vbox.setPadding(new Insets(10));
        //vbox.setSpacing(8);
        OsmBrowser browser = new OsmBrowser();
        LogicConsole logicConsole = new LogicConsole(browser.getEngine());
        borderPane.setTop(logicConsole);
        borderPane.setBottom(browser);
        scene = new Scene(borderPane,Color.web("#CAE1FF"));
        
        stage.setScene(scene);  

        
        stage.show();
    }
 
    public static void main(String[] args){
        launch(args);
    }
}