package org.jpc.examples.osm.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Scene scene;
    private ConsolePane logicConsole;
    private OsmBrowser browser;
    
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("MapQuery");
        BorderPane borderPane = new BorderPane();
        //VBox vbox = new VBox();
        //vbox.setPadding(new Insets(10));
        //vbox.setSpacing(8);
        browser = new OsmBrowser();
        logicConsole = new ConsolePane(browser.getEngine());
        borderPane.setTop(logicConsole);
        borderPane.setBottom(browser);
        scene = new Scene(borderPane,Color.web("#CAE1FF"));
        stage.setScene(scene);  
        stage.show();
    }
 
    @Override public void stop() {
    	//a call to super is not needed since according to the documentation: "The implementation of this method provided by the Application class does nothing."
    	logicConsole.freeResources();
    }
    
    public static void main(String[] args){
        launch(args);
    }
}