package org.jpc.examples.osm;

import static org.jpc.engine.provider.PrologEngineProviderManager.setPrologEngine;

import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.PrologEngines;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * @author sergioc
 *
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({OsmTest.class})
public class OsmTestSuite {
	@BeforeClass
    public static void oneTimeSetUp() {
		PrologEngine prologEngine = PrologEngines.getPrologEngineById("swi_jpl");
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("swi_pdt");
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("xsb_interprolog");
		setPrologEngine(prologEngine);
		if(!MapQuery.loadAll()) //load logic files
			throw new RuntimeException();
		MapQuery.importData(); //import data to the logic database from text file
		//prologEngine.query("map::load_osm(file('/Users/sergioc/Documents/workspaces/heal/mapquery/src/main/resources/org/jpc/examples/osm/brussels_center_filtered.osm'))").oneSolutionOrThrow();
    }
}
