package org.jpc.examples.osm.model.jpcconverters;

import org.jpc.Jpc;
import org.jpc.JpcBuilder;
import org.jpc.converter.catalog.map.MapConverter.MapToTermConverter;

public class OsmContext {

	private static Jpc context;
	
	public static Jpc getOsmContext() {
		if(context == null) {
			context = JpcBuilder.create()
					.register(new MapToTermConverter("-"))
					.register(new OsmJpcConverter())
					.register(new CoordinateJpcConverter())
					.register(new NodeJpcConverter())
					.register(new WayJpcConverter())
					.build();
		}
		return context;
	}
	
}
