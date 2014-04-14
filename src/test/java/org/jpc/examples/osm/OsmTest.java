package org.jpc.examples.osm;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.jpc.engine.prolog.PrologEngine;
import org.jpc.engine.provider.PrologEngineProviderManager;
import org.jpc.examples.osm.model.imp.OsmServiceJpc;
import org.junit.Test;

public class OsmTest {

	@Test
	public void testBrowser() {
		PrologEngine prologEngine = PrologEngineProviderManager.getPrologEngine();
		prologEngine.query("class([org,jpc,examples,osm,ui],['MapBrowserStage'])::launch returns weak(jref(Map))").oneSolutionOrThrow();
	}
	
	@Test
	public void testTags() {
		Map<String, String> tags = new HashMap<String, String>() {{
			put("amenity", "restaurant");
		}};
		assertEquals(2, new OsmServiceJpc().getNodes(tags).size());
	}

}
