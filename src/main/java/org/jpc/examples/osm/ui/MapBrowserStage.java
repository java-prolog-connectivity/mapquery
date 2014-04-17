package org.jpc.examples.osm.ui;

import static org.jpc.engine.provider.PrologEngineProviderManager.setPrologEngineProvider;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.PrologEngines;
import org.jpc.engine.provider.SimpleEngineProvider;
import org.jpc.examples.osm.MapQuery;
import org.jpc.examples.osm.model.jpcconverters.OsmContext;
import org.jpc.term.Term;
import org.jpc.util.JavaFXLauncher;


public class MapBrowserStage extends Stage {

	private MapBrowser mapBrowser;
	
	public MapBrowserStage() {
		setTitle("World Map");
		mapBrowser = new MapBrowser();
		//Scene scene = new Scene(root, 300, 250);
		Scene scene = new Scene(mapBrowser);
        setScene(scene);
	}
	
	
	public static MapBrowser launch() {
		MapBrowserStage stage = (MapBrowserStage) JavaFXLauncher.show(MapBrowserStage.class); //the Java application will not end after closing the window. This is desirable, since this call is designed to be executed from the Prolog side.
		return stage.mapBrowser;
	}
	
	
	/**
	 * Testing how the MapBrowser looks when opened from the Prolog side.
	 * @param args
	 */
	public static void main(String[] args) {
		//launch();
		PrologEngine prologEngine = PrologEngines.getPrologEngineById("swi_jpl");
		setPrologEngineProvider(new SimpleEngineProvider(prologEngine));
		if(!MapQuery.loadAll()) //load logic files
			throw new RuntimeException();
		
		new OsmContext();
		Term mapTerm = prologEngine.query("class([org,jpc,examples,osm,ui],['MapBrowserStage'])::launch returns weak(jref(Map))").oneSolutionOrThrow().get("Map");
		//System.out.println(mapTerm);
		
		prologEngine.query("map::load_osm(file('/Users/sergioc/Documents/workspaces/heal/mapquery/src/main/resources/org/jpc/examples/osm/brussels_center_filtered.osm'))").oneSolutionOrThrow();

		
//		try {
//			Thread.sleep(5000);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
		
		prologEngine.query("city(brussels)::coordinates(BRU), "+"java::eval("+mapTerm+"::goTo(BRU))").oneSolutionOrThrow();
		
//		try {
//			Thread.sleep(50);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
		
		//prologEngine.query("osm::node(Node), Node::has_tags([railway-station]), Node::has_tags(['name:fr'-'Bruxelles-Central']), Node::coordinates(Coordinates), osm::way(Way), Way::near(Coordinates, 0.1)").oneSolutionOrThrow();

		prologEngine.query("osm::node(Node), Node::has_tags([railway-station]), Node::has_tags(['name:fr'-'Bruxelles-Central']), Node::coordinates(Coordinates), osm::way(Way), Way::near(Coordinates, 0.1), " + mapTerm + "::draw(['Node':Node, 'Way':Way])").oneSolutionOrThrow();
	}
	
}
