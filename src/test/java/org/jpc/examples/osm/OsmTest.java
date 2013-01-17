package org.jpc.examples.osm;

import java.util.HashMap;
import java.util.Map;

import org.jpc.examples.osm.model.imp.OsmServiceJpc;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;

public class OsmTest {

	@Test
	public void testTags() {
		Map<String, String> tags = new HashMap<String, String>() {{
			put("amenity", "restaurant");
		}};
		assertEquals(2, new OsmServiceJpc().getNodes(tags).size());
	}

}
