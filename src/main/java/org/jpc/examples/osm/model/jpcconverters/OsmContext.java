package org.jpc.examples.osm.model.jpcconverters;

import static org.jpc.term.Functor.functor;

import org.jpc.Jpc;
import org.jpc.JpcBuilder;
import org.jpc.mapping.converter.catalog.map.MapConverter.MapToTermConverter;

public class OsmContext {

//	private static Jpc context;
//	
//	public static Jpc getOsmContext() {
//		if(context == null) {
//			context = JpcBuilder.create()
//					.register(new MapToTermConverter("-"))
//					.register(new OsmJpcConverter())
//					.register(new CoordinatesJpcConverter())
//					.register(new NodeJpcConverter())
//					.register(new WayJpcConverter())
//					.build();
//		}
//		return context;
//	}
	
	static {
		Jpc context = JpcBuilder.create()
				.register(new MapToTermConverter("-"))
				.register(new OsmJpcConverter())
				.register(new CoordinatesJpcConverter(), functor(CoordinatesJpcConverter.COORDINATE_FUNCTOR_NAME, 2).asTerm())
				.register(new NodeJpcConverter(), functor(NodeJpcConverter.NODE_FUNCTOR_NAME, 3).asTerm())
				.register(new WayJpcConverter(), functor(WayJpcConverter.WAY_FUNCTOR_NAME, 3).asTerm())
				.build();
		Jpc.setDefault(context);
	}
	
	public static Jpc getOsmContext() {
		return Jpc.getDefault();
	}
	
}
