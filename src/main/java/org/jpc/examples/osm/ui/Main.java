package org.jpc.examples.osm.ui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import org.jpc.commons.prologbrowser.ui.QueryBrowserPane;
import org.jpc.engine.provider.PrologEngineProviderManager;
import org.jpc.util.DriverUtil;

public class Main extends Application {
    private Scene scene;
    private MapQueryPane mapQueryPane;
    private MapBrowser browser;
    
    @Override public void start(Stage stage) {
        // create the scene
        stage.setTitle("MapQuery");
        
        //VBox vbox = new VBox();
        //vbox.setPadding(new Insets(10));
        //vbox.setSpacing(8);
        browser = new MapBrowser();
        QueryBrowserPane hydraPane = new QueryBrowserPane(this, null);
        PrologEngineProviderManager.setPrologEngineProvider(hydraPane.getLogicConsolePane().getPrologEngineChoiceModel());
        hydraPane.getSettingsPane().getModel().preloadLogtalkProperty().set(true);
        //queryBrowserPane.showSettings(false);
        mapQueryPane = new MapQueryPane(browser, hydraPane);
        BorderPane borderPane = new BorderPane();
        borderPane.setLeft(mapQueryPane);
        borderPane.setRight(browser);
//        borderPane.setTop(logicConsole);
//        borderPane.setBottom(browser);
        scene = new Scene(borderPane,Color.web("#CAE1FF"));
        stage.setScene(scene);  
        stage.show();
    }
 
    @Override public void stop() {
    	//a call to super is not needed since according to the documentation: "The implementation of this method provided by the Application class does nothing."
    	mapQueryPane.stop();
    }
    
    /**
     * Just to facilitate opening the interface from the Prolog side.
     */
    public static void launch(){
    	//DriverUtil.reportFoundDrivers();
    	Application.launch();
    }
    
    public static void main(String[] args){
        launch(args);
    }

}
