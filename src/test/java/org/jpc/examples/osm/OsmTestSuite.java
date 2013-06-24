package org.jpc.examples.osm;

import static junit.framework.Assert.assertTrue;
import static org.jpc.engine.provider.PrologEngineProviderManager.setPrologEngineProvider;

import org.jpc.engine.interprolog.InterPrologXsbDriver;
import org.jpc.engine.profile.LogtalkEngineProfile;
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
		//AbstractPrologEngineDriver prologEngineConfiguration = new JplSwiDriver();
		//AbstractPrologEngineDriver prologEngineConfiguration = new PdtConnectorDriver();
		AbstractPrologEngineDriver prologEngineConfiguration = new InterPrologXsbDriver();
		setPrologEngineProvider(new SimpleEngineProvider(new LogtalkEngineProfile(prologEngineConfiguration).createPrologEngine()));
		assertTrue(MapQuery.loadAll()); //load logic files
		MapQuery.importData(); //import data to the logic database from text file
    }
}
