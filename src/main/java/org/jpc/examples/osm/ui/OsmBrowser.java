package org.jpc.examples.osm.ui;

import java.net.URL;

import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class OsmBrowser extends Region {

	public static final String WEB_PAGE = "brussels_demo.html";
	
	final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
    public OsmBrowser() {
    	
        //apply the styles
        //getStyleClass().add("browser");
        // load the web page
        //webEngine.load("http://www.google.com");
    	final URL urlBrusselsDemo = getClass().getResource(WEB_PAGE);
        webEngine.load(urlBrusselsDemo.toExternalForm());
        //add the web view to the scene
        getChildren().add(browser);
 
    }
    
//    private Node createSpacer() {
//        Region spacer = new Region();
//        HBox.setHgrow(spacer, Priority.ALWAYS);
//        return spacer;
//    }
 
    @Override protected void layoutChildren() {
        double w = getWidth();
        double h = getHeight();
        layoutInArea(browser,0,0,w,h,0, HPos.CENTER, VPos.CENTER);
    }
 
    @Override protected double computePrefWidth(double height) {
        return 1000;
    }
 
    @Override protected double computePrefHeight(double width) {
        return 650;
    }
    
    public WebEngine getEngine() {
    	return webEngine;
    }
    
    
}
