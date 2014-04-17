package org.jpc.examples.osm;

import static org.jpc.engine.provider.PrologEngineProviderManager.setPrologEngine;

import org.jpc.engine.prolog.PrologEngine;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The tests in this test suite are commented out since in order to be executed a Prolog engine driver needs to be configured first.
 * In order to execute the unit tests follow these instructions:
 * - Uncomment the tests in the SuiteClasses annotation.
 * - Initialize a prologEngine according to one of the driver configurations defined in the jpc.settings file (see examples commented out below).
 * - Add a dependency in the POM to the selected driver (e.g., the artifact "jpc-jpl" for the jpl-based driver, or "jpc-drivers" for all the known jpc drivers). 
 * To use other drivers than the jpl-based one, refer to the JPC installation guide.
 * 
 * @author sergioc
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({/*OsmTest.class*/})
public class OsmTestSuite {
	
	public static final Logger logger = LoggerFactory.getLogger(OsmTestSuite.class);
	
	@BeforeClass
    public static void oneTimeSetUp() {
		PrologEngine prologEngine = null;
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("swi_jpl");
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("yap_jpl");
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("swi_pdt");
		//PrologEngine prologEngine = PrologEngines.getPrologEngineById("xsb_interprolog");
		if(prologEngine != null) {
			setPrologEngine(prologEngine);
			if(!MapQuery.loadAll()) //load logic files
				throw new RuntimeException();
			MapQuery.importData(); //import data to the logic database from text file
			//prologEngine.query("map::load_osm(file('/Users/sergioc/Documents/workspaces/heal/mapquery/src/main/resources/org/jpc/examples/osm/brussels_center_filtered.osm'))").oneSolutionOrThrow();
		} else
			logger.warn("No JPC driver has been configured. Skipping tests.");
    }
}
