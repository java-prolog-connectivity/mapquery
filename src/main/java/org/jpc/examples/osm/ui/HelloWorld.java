package org.jpc.examples.osm.ui;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {
	
	public static void launch() {
//		javafx.application.Platform.runLater(new Runnable() {
//
//			@Override
//			public void run() {
//				Application.launch();
//			}
//			 
//		 });
//		
//		new Thread() {
//			@Override
//			public void run() {
//				System.out.println("Starting");
//				Application.launch();
//			}
//		}.start();
		
		Application.launch();
	}
	
    public static void main(String[] args) {
        launch();
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Hello World!");
        Button btn = new Button();
        btn.setText("Say 'Hello World'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
 
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        primaryStage.setScene(new Scene(root, 300, 250));
        primaryStage.show();
    }
}