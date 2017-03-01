package org.jpc.examples.osm.model.jpcconverters;

import static java.util.Arrays.asList;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.jpc.Jpc;
import org.jpc.mapping.converter.FromTermConverter;
import org.jpc.mapping.converter.ToTermConverter;
import org.jpc.examples.osm.model.Way;
import org.jpc.examples.osm.model.imp.WayJpc;
import org.jpc.term.Compound;

public class WayJpcConverter implements ToTermConverter<Way, Compound>, FromTermConverter<Compound, Way> {

	public static final String WAY_FUNCTOR_NAME = "way";
	
	@Override
	public Way fromTerm(Compound term, Type type, Jpc jpc) {
		long id = jpc.fromTerm(term.arg(1));
		List<Long> nodesIds = jpc.fromTerm(term.arg(2));
		Map<String, String> tags = jpc.fromTerm(term.arg(3), Map.class);
		return new WayJpc(id, nodesIds, tags);
	}

	@Override
	public Compound toTerm(Way way, Class<Compound> termClass, Jpc jpc) {
		return jpc.toCompound(WAY_FUNCTOR_NAME, asList(way.getId(), way.getNodesIds(), way.getTags()));
	}

}
