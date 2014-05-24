package org.jpc.examples.osm.ui;

import static java.util.Arrays.asList;

import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import netscape.javascript.JSObject;

import org.jpc.examples.osm.model.Coordinates;
import org.jpc.examples.osm.model.Node;
import org.jpc.examples.osm.model.Osm;
import org.jpc.examples.osm.model.Taggeable;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.gsonconverters.CoordinatesGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.NodeGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.OsmGsonConverter;
import org.jpc.examples.osm.model.gsonconverters.WayGsonConverter;
import org.jpc.examples.osm.model.imp.OsmFragment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.gson.GsonBuilder;

public class MapBrowser extends Region {

	Logger logger = LoggerFactory.getLogger(MapBrowser.class);
	
	public static final String WEB_PAGE = "brussels_demo.html";
	public static final String NODE_VARIABLE_NAME = "Node";
	public static final String WAY_VARIABLE_NAME = "Way";
	public static final String JAVA_SCRIPT_INTERFACE_VARIABLE = "java"; //the id of the javascript variable that will be created in the browser to refer to methods in this class
	
	final WebView browser = new WebView();
    final WebEngine webEngine = browser.getEngine();
    
	private class BrowserInterface {
//		public void query(String queryString) {
//			ConsolePane.this.query(queryString);
//		}
	}
	
    public MapBrowser() {
    	
        //apply the styles
        //getStyleClass().add("browser");
        // load the web page
        //webEngine.load("http://www.google.com");
    	
    	final URL urlBrusselsDemo = getClass().getResource(WEB_PAGE);
        webEngine.load(urlBrusselsDemo.toExternalForm());

        
		webEngine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(ObservableValue<? extends State> ov, State oldState, State newState) {
				if (newState == State.SUCCEEDED) {
					JSObject win = (JSObject)webEngine.executeScript("window"); //this and the following instruction should be executed only after the web page has been completely loaded
					win.setMember(JAVA_SCRIPT_INTERFACE_VARIABLE, new BrowserInterface());
					
					//zoomTo(1);
					//goToCoordinates(new CoordinatesJpc(3,45));
				}
			}
		});
        
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
        //return 525;
    	return 615;
    }
 
    @Override protected double computePrefHeight(double width) {
        //return 650;
    	return 645;
    }
    
    public WebEngine getEngine() {
    	return webEngine;
    }
    
  
	private void drawSolutionMultimap(ListMultimap<String, Object> mapQueryResult) {		
		List nodes = mapQueryResult.get(NODE_VARIABLE_NAME);
		List ways = mapQueryResult.get(WAY_VARIABLE_NAME);
		
		
//		int numberNodes = nodes.size();
//		int numberWays = ways.size();
//		System.out.println("Number of nodes: " + numberNodes);
//		System.out.println("Number of ways: " + numberWays);
		
		
		Osm osm = new OsmFragment(nodes, ways);
		
		GsonBuilder gson = new GsonBuilder();
		gson.registerTypeAdapter(Coordinates.class, new CoordinatesGsonConverter());
		gson.registerTypeAdapter(Node.class, new NodeGsonConverter());
		gson.registerTypeAdapter(Way.class, new WayGsonConverter());
		gson.registerTypeAdapter(OsmFragment.class, new OsmGsonConverter());
		
		//gson.setPrettyPrinting();
		
		final String osmJson = gson.create().toJson(osm);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logger.debug("OSM Json: " + osmJson);
				logger.debug("Preparing to draw...");
				webEngine.executeScript("g_drawGeoJson("+osmJson+")");
				logger.debug("Done drawing! ...");
			}
		});
	}
	
	private void addSolutions(ListMultimap<String, Object> solutionsMultimap, List<? extends Map<String, Object>> solutions) {
		for(Map<String, Object> solution : solutions) {
			addSolution(solutionsMultimap, solution);
		}
	}
	
	private void addSolution(ListMultimap<String, Object> solutionsMultimap, Map<String, Object> solution) {
		for(Entry<String, Object> entry : solution.entrySet()) {
			solutionsMultimap.put(entry.getKey(), entry.getValue());
		}
	}
	
	public void drawSolution(Map<String, Object> solution) {
		ListMultimap<String, Object> solutionsMultimap = ArrayListMultimap.create();
		addSolution(solutionsMultimap, solution);
		drawSolutionMultimap(solutionsMultimap);
	}
	
	public void drawSolutions(List<? extends Map<String, Object>> solutions) {
		ListMultimap<String, Object> solutionsMultimap = ArrayListMultimap.create();
		addSolutions(solutionsMultimap, solutions);
		drawSolutionMultimap(solutionsMultimap);
	}
	
	public void draw(Taggeable taggeable) {
		draw(asList(taggeable));
	}
	
	public void draw(List<Taggeable> taggeables) {
		//This implementation should be improved performance wise. 
		//It is implemented in this way in order to profit from the existing drawSolutionMultimap() method, designed to draw solutions generated from the query console in the Java side.
		ListMultimap<String, Object> solutionsMultimap = ArrayListMultimap.create();
		for(Taggeable taggeable : taggeables) {
			if(taggeable instanceof Node)
				solutionsMultimap.put(NODE_VARIABLE_NAME, taggeable);
			else if(taggeable instanceof Way)
				solutionsMultimap.put(WAY_VARIABLE_NAME, taggeable);
			else
				throw new RuntimeException("Unrecognized Taggeable : " + taggeable + ".");
		}
		drawSolutionMultimap(solutionsMultimap);
	}
	
	public void zoomTo(int zoomLevel) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logger.debug("Zooming to: " + zoomLevel);
				webEngine.executeScript("g_zoomTo("+zoomLevel+")");
				logger.debug("Done zooming! ...");
			}
		});
	}
	
	public void goTo(Coordinates coordinates) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				logger.debug("Moving to coordinates: " + coordinates);
				webEngine.executeScript("g_goToCoordinates("+coordinates.getLon()+", "+coordinates.getLat()+")");
				logger.debug("Done moving to coordinates! ...");
			}
		});
	}

}
