package org.jpc.examples.osm;

import static org.jpc.engine.provider.PrologEngineProviderManager.setPrologEngineProvider;

import org.jpc.engine.jpl.JplSwiDriver;
import org.jpc.engine.profile.LogtalkEngineProfile;
import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.prolog.driver.AbstractPrologEngineDriver;
import org.jpc.engine.provider.SimpleEngineProvider;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({OsmTest.class})
public class OsmTestSuite {
	@BeforeClass
    public static void oneTimeSetUp() {
		AbstractPrologEngineDriver prologEngineConfiguration = new JplSwiDriver();
		PrologEngine prologEngine = new LogtalkEngineProfile(prologEngineConfiguration).createPrologEngine();
		//AbstractPrologEngineDriver prologEngineConfiguration = new PdtConnectorDriver();
		//AbstractPrologEngineDriver prologEngineConfiguration = new InterPrologXsbDriver();
		setPrologEngineProvider(new SimpleEngineProvider(prologEngine));
		if(!MapQuery.loadAll()) //load logic files
			throw new RuntimeException();
		prologEngine.query("map::load_osm(file('/Users/sergioc/Documents/workspaces/heal/mapquery/src/main/resources/org/jpc/examples/osm/brussels_center_filtered.osm'))").oneSolutionOrThrow();
		//MapQuery.importData(); //import data to the logic database from text file
    }
}
